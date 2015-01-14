package com.turkishdelight.taxe.scenes;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.LabelButton;
import com.turkishdelight.taxe.routing.AiSprite;
import com.turkishdelight.taxe.routing.Carriage;
import com.turkishdelight.taxe.routing.Connection;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.routing.Route;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.worldobjects.Junction;
import com.turkishdelight.taxe.worldobjects.RouteLocation;
import com.turkishdelight.taxe.worldobjects.Station;

public class GameScene extends GameGUIScene {

	Texture mapText;
	SpriteComponent map;
	public ShopScene shopScene;
	public GoalsScene goalsScene;
	public CurrentResourcesScene resourceScene;
	
	private ArrayList<CurvedPath> curvedPaths = new ArrayList<CurvedPath>();						// collection of curved paths (only one way for each path) for drawing
	private ArrayList<RouteLocation> routeLocations = new ArrayList<RouteLocation>();			// collection of routelocations (junction/station)
	
	private ArrayList<AiSprite> previousCollisions;												// used for remembering the collisions that occured in the previous turn, ensures not multiple collisions detected
	
	private LabelButton confirmRouteSelectionButton;
	private LabelButton routeSelectionButton;
	protected boolean isSelectingRoute = false;													// boolean that says whether the playyer is currently in route selection mode
	private ArrayList<Train> selectedTrains = new ArrayList<Train>();							// the train that is being used to use in route selection mode -- TODO change to array for multiple trains on same location
	private ArrayList<RouteLocation> newRoute = new ArrayList<RouteLocation>();					// the (potentially incomplete) route at that point
	private int newRouteDistance;																// TODO currently only used to print- should be diplayed
	private ArrayList<RouteLocation> previousConnectedLocations = new ArrayList<RouteLocation>();

	public GameScene(Player player1In, Player player2In){
		super(player1In, player2In, false);
		nextTurn();
		player1Go = true;
		delayedCreate();
	}
	
	public Train getSelectedTrain(){
		if (selectedTrains.size() > 0){
			return selectedTrains.get(0);
		} else {
			return null;
		}
	}
	
	public void nextTurn() {
		if (map != null)
			if (getCollisions().size() > 0) {
				calculateCollisions();
			}
	}
	
	@Override
	public void Update() {
		if (selectedTrains.size()  > 1){
			// send dialog to select trains
		}
	}
	
	public void delayedCreate() {
		// map setup
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.backgroundZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(map);
		
		shopScene = new ShopScene(this, this.player1, this.player2);
		goalsScene = new GoalsScene(this, this.player1, this.player2);
		resourceScene = new CurrentResourcesScene(this, this.player1, this.player2);

		//Locations setup
		curvedPaths = new ArrayList<CurvedPath>();
		Station london = createStation(this, "London", 210, 390);
		Station rome = createStation(this, "Rome", 415, 168);
		Station moscow = createStation(this, "Moscow", 800, 450);
		Station lisbon = createStation(this, "Lisbon", 30, 120);
		Station paris = createStation(this, "Paris", 300, 340);
		Station berlin = createStation(this, "Berlin", 410, 400);
		Station madrid = createStation(this, "Madrid", 120, 150);
		Station budapest = createStation(this, "Budapest", 510, 290);
		Station krakow = createStation(this, "Krakow", 520, 350);
		System.out.println("Locations created");
		Junction junction1 = createJunction(this, "Junction1", 352, 268);
		Junction junction2 = createJunction(this, "Junction2", 479, 331);
		
		// setup connections
		connectRouteLocations(london, paris); 
		connectRouteLocations(paris, junction1);
		connectRouteLocations(rome, junction1);
		connectRouteLocations(rome, budapest);
		connectRouteLocations(london, lisbon);
		connectRouteLocations(madrid, junction1);
		connectRouteLocations(junction1, junction2);
		connectRouteLocations(krakow, junction2);
		connectRouteLocations(paris, berlin);
		connectRouteLocations(berlin, junction2);
		connectRouteLocations(budapest, junction2);
		connectRouteLocations(moscow, krakow);
		connectRouteLocations(moscow, berlin);

		// add trains
		Texture trainTexture = new Texture("traincropped.png"); // traincropped added to allow more accurate collision detection
		createTrainAndCarriage(trainTexture, 1, london, player1);
		createTrainAndCarriage(trainTexture, 2, moscow, player2);
		
		// create route (with dotted line)
		Texture text = new Texture("route.png");
		final int divider = 10; // distance between 2 dots
		// go to every x * divider, find closest distance value from curvedPath array and use that to get corresponding position
		for (CurvedPath curvedPath: curvedPaths){
			for (int i = 0; i < curvedPath.getFinalDistance(); i+=divider) {
				int x = curvedPath.closestIndex(i, curvedPath.getDistances());
				SpriteComponent route = new SpriteComponent(this, text, Game.backgroundZ);
				route.setSize(2, 2);
				Vector2 point = curvedPath.getPoint(x);
				route.setPosition(point.x+2, point.y+2);
				Add(route);
			}
		}
		
		Texture clearButtonTexture = new Texture("Clear_Button.png");
		routeSelectionButton = new LabelButton(this, clearButtonTexture , 100 , 40, Label.genericFont(Color.WHITE, 20)) {
			@Override
			public void onClickEnd()
			{
				if (!isSelectingRoute) {
					startSelectingRoute();
					isSelectingRoute = true;
				} else {
					endSelectingRoute();
					isSelectingRoute = false;
				}
			}
		};
		routeSelectionButton.setPosition(Game.targetWindowsWidth/2, Game.targetWindowsHeight - 200);
		routeSelectionButton.setText("Select Route");
		routeSelectionButton.setAlignment(0);
		Add(routeSelectionButton);
		
		confirmRouteSelectionButton = new LabelButton(this, clearButtonTexture, 100 , 40, Label.genericFont(Color.WHITE, 20)) {
			@Override
			public void onClickEnd()
			{
				if (isSelectingRoute) {
					if (newRoute.size() > 1 && (newRoute.get(newRoute.size()-1).getClass() == Station.class)) {
						selectedTrains.get(0).setPath(new Route(newRoute));
						System.out.println("Route completed: " + newRoute.toString());
						endSelectingRoute();
						isSelectingRoute = false;
					}
				} 
			}
		};
		confirmRouteSelectionButton.setPosition(Game.targetWindowsWidth -150, Game.targetWindowsHeight -200);
		confirmRouteSelectionButton.setSize(0, 0);
		confirmRouteSelectionButton.setText("");
		confirmRouteSelectionButton.setAlignment(0);
		Add(confirmRouteSelectionButton);
	}
	
	private void createTrainAndCarriage(Texture trainTexture, int weight, Station station, final Player player) {
		// create a train, carriage and connect the 2
		// TODO have the train take a player variable
		Train train = new Train(this, trainTexture, station, weight) {
			@Override
			public void onClickEnd()
			{
				if (isRouteSelecting() && activePlayer().equals(player) && (this.getStation()!= null) && newRoute.size() == 0){
					// if the train is clicked on when at a station and in route selection mode, start the route from here
					System.out.println("train selected");
					selectedTrains.add(this);
					selectStartingLocation(getStation());
				}
			}
		};
		Add(train);
		Carriage carriage = new Carriage(this, trainTexture, station, train);
		Add(carriage);
		player.addAiSprite(train);
		player.addAiSprite(carriage);
		train.setCarriage(carriage);
	}

	private Station createStation(GameScene parentScene, String locationName, int x , int y) {
		Station routeLocation = new Station(parentScene, locationName, x,y);
		Add(routeLocation);
		routeLocations.add(routeLocation);
		return (Station) routeLocation;
		
	}	
	
	private Junction createJunction(GameScene parentScene, String locationName, int x , int y) {
		Junction routeLocation = new Junction(parentScene, locationName, x,y);
		Add(routeLocation);
		routeLocations.add(routeLocation);
		return (Junction) routeLocation;
		}
	
	private void connectRouteLocations(RouteLocation l1, RouteLocation l2){
		// connects 2 routelocations (junctions + stations) 
		HashMap<String, CurvedPath> paths = getPaths();
		CurvedPath path1 = paths.get(l1.getName() + l2.getName());
		CurvedPath path2 = paths.get(l2.getName() + l1.getName());
		l1.addConnection(l2, path1);
		l2.addConnection(l1, path2); 
	}
	
	private void calculateCollisions() {
		// main method which gets any collisions, decides what to do with collisions
		boolean previousCollision = false;
		ArrayList<AiSprite> collisions = getCollisions();
		for (int i=0; i<collisions.size(); i+=2){
			AiSprite p1Train = collisions.get(i);
			AiSprite p2Train = collisions.get(i+1);
			for (int x=0; x<previousCollisions.size(); x+=2){
				if (previousCollisions.get(x).equals(p1Train) && previousCollisions.get(x+1).equals(p2Train)){
					previousCollision = true;
				} 
			}
			if (!previousCollision){
				if (p1Train.getClass() == Carriage.class) { // if player1's aiSprite is a carriage
					((Carriage) p1Train).decreaseCarriageCount();
				} else if (p2Train.getClass() == Carriage.class){ // if player2's aiSprite is a carriage
					((Carriage) p2Train).decreaseCarriageCount();
				} else { // 2 trains colliding
					resolveTrainCollision(p1Train, p2Train);
				}
			}
		}
		previousCollisions = collisions;
	}
	
	private void resolveTrainCollision(AiSprite p1Train, AiSprite p2Train){
		//special method for resolving train-train collisions only
		if ((p1Train.getWeight()*p1Train.getSpeed()) < (p2Train.getWeight()*p2Train.getSpeed())){
			System.out.println("p1 wins!");
			Carriage carriage = ((Train) p2Train).getCarriage();
			carriage.decreaseCarriageCount();
			
		} else if ((p1Train.getWeight()*p1Train.getSpeed()) > (p2Train.getWeight()*p2Train.getSpeed())){
			System.out.println("p2 wins!");
			Carriage carriage = ((Train) p1Train).getCarriage();
			carriage.decreaseCarriageCount();
			
		} else {
			System.out.println("its a draw!");
		}
	}
	
	private ArrayList<AiSprite> getCollisions(){
		// returns array where every even element is player1 collided train, odd element is player2 collided train
		ArrayList<AiSprite> collidedAiSprites = new ArrayList<AiSprite>();
		for (AiSprite p1AiSprite: player1.getAiSprites()) {
			for (AiSprite p2AiSprite: player2.getAiSprites()) {
				if (hasCollisionOccured(p1AiSprite, p2AiSprite)){
					collidedAiSprites.add(p1AiSprite);
					collidedAiSprites.add(p2AiSprite);
				}
			}
		}
		return collidedAiSprites;
	}

	private boolean hasCollisionOccured(AiSprite aiSprite1, AiSprite aiSprite2) {
		// tests whether 2 aiSprites have collided using their respective polygons
		// TODO change so that number-based system used?
		Polygon poly1 = aiSprite1.getPolygon();
		Polygon poly2 = aiSprite2.getPolygon();
		if (!(aiSprite1.getClass() == Carriage.class && aiSprite2.getClass() == Carriage.class)){ // collision cannot occur between 2 carriages
			if (Intersector.overlapConvexPolygons(poly1.getTransformedVertices(), poly2.getTransformedVertices(), null)){
				return true;
			}
		}
		return false;
	}
	
	protected void startSelectingRoute() {
		// change buttons accordingly
		nextGoButton.setSize(0,0);
		confirmRouteSelectionButton.setText("Confirm Route");
		confirmRouteSelectionButton.setSize(100, 40);
		routeSelectionButton.setText("Cancel Routing");
		// enable the routeLocations to be clickable
		newRoute = new ArrayList<RouteLocation>();
		for (RouteLocation routeLocation:routeLocations){
			routeLocation.setSelectingRoute(true);
		}	
		
		// fade other players trains, carriages
		Player otherPlayer;
		if (activePlayer() == player1){
			otherPlayer = player2;
		} else {
			otherPlayer = player1;
		}
		ArrayList<AiSprite> otherAiSprites = otherPlayer.getAiSprites();
		for (AiSprite aiSprite : otherAiSprites){
			aiSprite.setColor(Color.LIGHT_GRAY);
		}
	}
	
	public boolean isRouteSelecting() {
		return isSelectingRoute;
	}
	
	public void selectStartingLocation(RouteLocation startLocation){
		// used when the initial train has been selecting, giving the attahed starting location
		newRoute.add(startLocation);
		((Station) startLocation).setFont(Label.genericFont(Color.BLUE, 20));
		Texture blueLocationTexture = new Texture("blueLocation.png");
		startLocation.setTexture(blueLocationTexture);

		// set up connected location's graphical cues
		ArrayList<Connection> connections = startLocation.getConnections();
		Texture redLocationTexture = new Texture("redLocation.png");
		// give any selectable, connected routeLocations red text
		for (Connection connection: connections) {
			RouteLocation connectedLocation = connection.getTargetLocation();
			previousConnectedLocations.add(connectedLocation);
			connectedLocation.setTexture(redLocationTexture);
			if (connectedLocation.getClass() == Station.class){
				((Station) connectedLocation).setFont(Label.genericFont(Color.RED, 20));
			}
		}
	}
	
	
	public void selectLocation(RouteLocation routeLocation){
		if ((routeLocation.isConnected(newRoute.get(newRoute.size()-1)) && !newRoute.contains(routeLocation))){
			// if the starting location/ train has been selected, if the location is connected to the previous location
			// and if the new location isnt already selected, select the location
			
			// set any old connections to black text
			Texture locationTexture = new Texture("location.png");
			Texture junctionTexture = new Texture("location.png");
			for (RouteLocation tempLocation: previousConnectedLocations){
				if (tempLocation.getClass() == Station.class){
					tempLocation.setTexture(locationTexture);
					((Station) tempLocation).setFont(Label.genericFont(Color.BLACK, 20));
				} else {
					tempLocation.setTexture(junctionTexture);
				}
			}
			previousConnectedLocations = new ArrayList<RouteLocation>();
			
			// set up new locations graphical cues
			if (routeLocation.getClass() == Station.class){
				((Station) routeLocation).setFont(Label.genericFont(Color.BLUE, 20));
				Texture blueLocationTexture = new Texture("blueLocation.png");
				routeLocation.setTexture(blueLocationTexture);
			} else {
				Texture blueJunctionTexture = new Texture("blueLocation.png");
				routeLocation.setTexture(blueJunctionTexture);
			}
			
			// set up connected locations graphical cues
			ArrayList<Connection> connections = routeLocation.getConnections();
			Texture redLocationTexture = new Texture("redLocation.png");
			Texture redJunctionTexture = new Texture("redLocation.png");
			// give any selectable, connected routeLocations red text
			for (Connection connection: connections) {
				RouteLocation connectedLocation = connection.getTargetLocation();
				if (connectedLocation == newRoute.get(newRoute.size()-1)){
					// if the connected location is the previous location, calculate route distance using the path distance
					newRouteDistance += connection.getPath().getFinalDistance();
				}
				if (!(newRoute.contains(connectedLocation))){
					// if the connected location isnt already in newRoute, show the graphical cue
					previousConnectedLocations.add(connectedLocation);
					if (connectedLocation.getClass() == Station.class){
						((Station) connectedLocation).setFont(Label.genericFont(Color.RED, 20));
						connectedLocation.setTexture(redLocationTexture);
					} else {
						connectedLocation.setTexture(redJunctionTexture);
					}
				}
			}
			newRoute.add(routeLocation);
			System.out.println("Current route routeDistance = " + newRouteDistance);
		}
	}

	private void endSelectingRoute(){
		// reverse everything changed in startSelectingRoute wipe routeLocations array
		// reset buttons
		nextGoButton.setSize(83, 44);
		confirmRouteSelectionButton.setText(" ");
		confirmRouteSelectionButton.setSize(0, 0);
		routeSelectionButton.setText("Select Route");
		// reset routeLocations
		Texture locationTexture = new Texture("location.png");
		Texture junctionTexture = new Texture("location.png");
		for (RouteLocation routeLocation:routeLocations){
			routeLocation.setSelectingRoute(false);
			if (routeLocation.getClass() == Station.class) {
				((Station) routeLocation).setFont(Label.genericFont(Color.BLACK, 20));
				routeLocation.setTexture(locationTexture);
			} else {
				routeLocation.setTexture(junctionTexture);
			}
		}	
		
		// reset train colours
		Player otherPlayer;
		if (player1Go){
			otherPlayer = player2;
		} else {
			otherPlayer = player1;
		}
		
		ArrayList<AiSprite> otherAiSprites = otherPlayer.getAiSprites();
		for (AiSprite aiSprite : otherAiSprites){
			aiSprite.setColor(Color.WHITE);
		}
		
		previousConnectedLocations = new ArrayList<RouteLocation>();
		newRoute = new ArrayList<RouteLocation>();
		newRouteDistance = 0;
		selectedTrains = null;
	}

	private HashMap<String, CurvedPath> getPaths() {
		// this creates all of the paths on the map and returns them
		HashMap<String, CurvedPath> paths = new HashMap<String, CurvedPath>();
		// the string key is the two routelocation string representations concaenated int he order that correspons to the path
		// eg key "ParisRome" corresponds to path that goes from paris to rome
		// used to make route creation easier
		
		// first, last control points must be same due to catmullromspline maths
		Vector2[] dataSet1 = new Vector2[5];
		dataSet1[0] = (new Vector2(210, 390));
		dataSet1[1] = (new Vector2(210, 390));
		dataSet1[2] = (new Vector2(255, 345));
		dataSet1[3] = (new Vector2(300, 340));
		dataSet1[4] = (new Vector2(300, 340));
		CurvedPath londonParis = new CurvedPath(dataSet1, false);
		paths.put("LondonParis", londonParis);
		curvedPaths.add(londonParis);
		
		Vector2[] rdataSet1 = reverseDataset(dataSet1);
		CurvedPath parisLondon = new CurvedPath(rdataSet1, false);
		paths.put("ParisLondon" , parisLondon);
		

		Vector2[] dataSet2 = new Vector2[5];
		dataSet2[0] = (new Vector2(300, 340));
		dataSet2[1] = (new Vector2(300, 340));
		dataSet2[2] = (new Vector2(320, 300));
		dataSet2[3] = (new Vector2(352, 268));
		dataSet2[4] = (new Vector2(352, 268));
		CurvedPath parisJunction1 = new CurvedPath(dataSet2, false);
		paths.put("ParisJunction1", parisJunction1);
		curvedPaths.add(parisJunction1);
		
		Vector2[] rdataSet2 = reverseDataset(dataSet2);	
		CurvedPath junction1Paris = new CurvedPath(rdataSet2, false);
		paths.put("Junction1Paris", junction1Paris);

		
		Vector2[] dataSet3 = new Vector2[5];
		dataSet3[0] = (new Vector2(415,168));
		dataSet3[1] = (new Vector2(415,168));
		dataSet3[2] = (new Vector2(382,238));
		dataSet3[3] = (new Vector2(352,268));
		dataSet3[4] = (new Vector2(352,268));
		CurvedPath romeJunction1 = new CurvedPath(dataSet3, false);
		paths.put("RomeJunction1", romeJunction1);
		curvedPaths.add(romeJunction1);
		
		Vector2[] rdataSet3 = reverseDataset(dataSet3);	
		CurvedPath junction1Rome = new CurvedPath(rdataSet3, false);
		paths.put("Junction1Rome", junction1Rome);

		Vector2[] dataSet4 = new Vector2[6];
		dataSet4[0] = (new Vector2(415, 168));
		dataSet4[1] = (new Vector2(415, 168));
		dataSet4[2] = (new Vector2(405, 245));
		dataSet4[3] = (new Vector2(450, 270));
		dataSet4[4] = (new Vector2(510, 290));
		dataSet4[5] = (new Vector2(510, 290));
		CurvedPath romeBudapest = new CurvedPath(dataSet4, false);
		paths.put("RomeBudapest", romeBudapest);
		curvedPaths.add(romeBudapest);
		
		Vector2[] rdataSet4 = reverseDataset(dataSet4);
		CurvedPath budapestRome = new CurvedPath(rdataSet4, false);
		paths.put("BudapestRome", budapestRome);

		

		Vector2[] dataSet5 = new Vector2[7];
		dataSet5[0] = (new Vector2(210, 390));
		dataSet5[1] = (new Vector2(210, 390));
		dataSet5[2] = (new Vector2(210, 365));
		dataSet5[3] = (new Vector2(180, 200));
		dataSet5[4] = (new Vector2(80, 220));
		dataSet5[5] = (new Vector2(30, 120));
		dataSet5[6] = (new Vector2(30, 120));
		CurvedPath londonLisbon = new CurvedPath(dataSet5, false);
		paths.put("LondonLisbon", londonLisbon);
		curvedPaths.add(londonLisbon);
		
		Vector2[] rdataSet5 = reverseDataset(dataSet5);
		CurvedPath lisbonLondon = new CurvedPath(rdataSet5, false);
		paths.put("LisbonLondon", lisbonLondon);
		

		Vector2[] dataSet6 = new Vector2[4];
		dataSet6[0] = (new Vector2(30, 120));
		dataSet6[1] = (new Vector2(30, 120));
		dataSet6[2] = (new Vector2(120, 150));
		dataSet6[3] = (new Vector2(120, 150));
		CurvedPath lisbonMadrid = new CurvedPath(dataSet6, false);
		paths.put("LisbonMadrid", lisbonMadrid);
		curvedPaths.add(lisbonMadrid);
		
		Vector2[] rdataSet6 = reverseDataset(dataSet6);
		CurvedPath madridLisbon = new CurvedPath(rdataSet6, false);
		paths.put("MadridLisbon", madridLisbon);

		

		Vector2[] dataSet7 = new Vector2[5];
		dataSet7[0] = (new Vector2(300, 340));
		dataSet7[1] = (new Vector2(300, 340));
		dataSet7[2] = (new Vector2(350, 340));
		dataSet7[3] = (new Vector2(410,400));
		dataSet7[4] = (new Vector2(410,400));
		CurvedPath parisBerlin = new CurvedPath(dataSet7, false);
		paths.put("ParisBerlin", parisBerlin);
		curvedPaths.add(parisBerlin);

		Vector2[] rdataSet7 = reverseDataset(dataSet7);
		CurvedPath berlinParis = new CurvedPath(rdataSet7, false);
		paths.put("BerlinParis", berlinParis);

		

		Vector2[] dataSet8 = new Vector2[4];
		dataSet8[0] = (new Vector2(410, 400));
		dataSet8[1] = (new Vector2(410, 400));
		dataSet8[2] = (new Vector2(479, 331));
		dataSet8[3] = (new Vector2(479, 331));
		CurvedPath berlinJunction2 = new CurvedPath(dataSet8, false);
		paths.put("BerlinJunction2", berlinJunction2);
		curvedPaths.add(berlinJunction2);
		
		Vector2[] rdataSet8 = reverseDataset(dataSet8);
		CurvedPath junction2Berlin = new CurvedPath(rdataSet8, false);
		paths.put("Junction2Berlin", junction2Berlin);

		Vector2[] dataSet9 = new Vector2[4];
		dataSet9[0] = (new Vector2(479, 331));
		dataSet9[1] = (new Vector2(479, 331));
		dataSet9[2] = (new Vector2(510, 290));
		dataSet9[3] = (new Vector2(510, 290));
		CurvedPath Junction2Budapest = new CurvedPath(dataSet9, false);
		paths.put("Junction2Budapest", Junction2Budapest);
		curvedPaths.add(Junction2Budapest);
		
		Vector2[] rdataSet9 = reverseDataset(dataSet8);
		CurvedPath budapestJunction2 = new CurvedPath(rdataSet9, false);
		paths.put("BudapestJunction2", budapestJunction2);
		
		
		Vector2[] dataSet10 = new Vector2[6];
		dataSet10[0] = (new Vector2(520, 350));
		dataSet10[1] = (new Vector2(520, 350));
		dataSet10[2] = (new Vector2(602, 370));
		dataSet10[3] = (new Vector2(705, 383));
		dataSet10[4] = (new Vector2(800, 450));
		dataSet10[5] = (new Vector2(800, 450));
		CurvedPath krakowMoscow = new CurvedPath(dataSet10, false);
		paths.put("KrakowMoscow", krakowMoscow);
		curvedPaths.add(krakowMoscow);

		Vector2[] rdataSet10 = reverseDataset(dataSet10);
		CurvedPath moscowKrakow = new CurvedPath(rdataSet10, false);
		paths.put("MoscowKrakow", moscowKrakow);

		
		Vector2[] dataSet11 = new Vector2[5];
		dataSet11[0] = (new Vector2(410, 400));
		dataSet11[1] = (new Vector2(410, 400));
		dataSet11[2] = (new Vector2(550, 420));
		dataSet11[3] = (new Vector2(800, 450));
		dataSet11[4] = (new Vector2(800, 450));
		CurvedPath berlinMoscow = new CurvedPath(dataSet11, false);
		paths.put("BerlinMoscow", berlinMoscow);
		curvedPaths.add(berlinMoscow);
		
		Vector2[] rdataSet11 = reverseDataset(dataSet11);
		CurvedPath moscowBerlin = new CurvedPath(rdataSet11, false);
		paths.put("MoscowBerlin", moscowBerlin);
		
		
		Vector2[] dataSet12 = new Vector2[4];
		dataSet12[0] = (new Vector2(520, 350));
		dataSet12[1] = (new Vector2(520, 350));
		dataSet12[2] = (new Vector2(479, 331));
		dataSet12[3] = (new Vector2(479, 331));
		CurvedPath krakowJunction2 = new CurvedPath(dataSet12, false);
		paths.put("KrakowJunction2", krakowJunction2);
		curvedPaths.add(krakowJunction2);
		
		Vector2[] rdataSet12 = reverseDataset(dataSet12);
		CurvedPath junction2Krakow = new CurvedPath(rdataSet12, false);
		paths.put("Junction2Krakow", junction2Krakow);
		
		Vector2[] dataSet13 = new Vector2[4];
		dataSet13[0] = (new Vector2(479, 331));
		dataSet13[1] = (new Vector2(479, 331));
		dataSet13[2] = (new Vector2(352, 268));
		dataSet13[3] = (new Vector2(352, 268));
		CurvedPath junction2Junction1 = new CurvedPath(dataSet13, false);
		paths.put("Junction2Junction1", junction2Junction1);
		curvedPaths.add(junction2Junction1);
		
		Vector2[] rdataSet13 = reverseDataset(dataSet13);
		CurvedPath junction1Junction2 = new CurvedPath(rdataSet13, false);
		paths.put("Junction1Junction2", junction1Junction2);
		
		Vector2[] dataSet14 = new Vector2[4];
		dataSet14[0] = (new Vector2(352, 268));
		dataSet14[1] = (new Vector2(352, 268));
		dataSet14[2] = (new Vector2(120, 150));
		dataSet14[3] = (new Vector2(120, 150));
		CurvedPath junction1Madrid = new CurvedPath(dataSet14, false);
		paths.put("Junction1Madrid", junction1Madrid);
		curvedPaths.add(junction1Madrid);
		
		Vector2[] rdataSet14 = reverseDataset(dataSet14);
		CurvedPath madridJunction1 = new CurvedPath(rdataSet14, false);
		paths.put("MadridJunction1", madridJunction1);
		return paths;
	}

	private Vector2[] reverseDataset(Vector2[] dataSet){
		Vector2[] rdataSet1 = new Vector2[dataSet.length];
		for (int i=0; i < rdataSet1.length; i++){
			rdataSet1[rdataSet1.length-i-1] = dataSet[i];
		}
		return rdataSet1;
	}
	
	@Override
	public void player1Active()
	{
		super.player1Active();
		player1.updateTurn(true);
		player2.updateTurn(false);
	}
	
	@Override
	public void player2Active()
	{
		super.player2Active();
		player2.updateTurn(true);
		player1.updateTurn(false);
	}
	
	private Player activePlayer() {
		if (player1Go){
			return player1;
		} else {
			return player2;
		}
	}
	
	@Override
	public void nextGoPressed()
	{
		super.nextGoPressed();
		if (!isSelectingRoute)
			nextTurn();
	}

	@Override
	public void goalsToolbarPressed() 
	{
		System.out.println("goalsToolbarPressed");
		if (!isSelectingRoute)
			Game.pushScene(goalsScene);
	}
	
	@Override
	public void shopToolbarPressed() 
	{
		System.out.println("shopToolbarPressed");
		if (!isSelectingRoute)
			Game.pushScene(shopScene);
			//Switch to shop scene
		
	}
	
	@Override
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		if (!isSelectingRoute)
			Game.pushScene(resourceScene);
	}
	
	@Override
	public void Draw(SpriteBatch batch) {
		super.Draw(batch);

		// for testing collision detection by drawing hitbox
		/*ShapeRenderer sr = new ShapeRenderer();

		Polygon p = train1.getPolygon();
		Polygon p2 = train2.getPolygon();

		sr.begin(ShapeType.Line);
		sr.polygon(p.getTransformedVertices());
		sr.polygon(p2.getTransformedVertices());
		sr.end();*/
	}
}

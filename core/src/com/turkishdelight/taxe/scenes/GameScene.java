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
	private SelectionScene trainSelectionScene;
	
	public int numberTurns = 0;
	private ArrayList<CurvedPath> curvedPaths = new ArrayList<CurvedPath>();					// collection of curved paths (only one way for each path) for drawing
	private ArrayList<RouteLocation> routeLocations = new ArrayList<RouteLocation>();			// collection of routelocations (junction/station)
	
	private ArrayList<AiSprite> previousCollisions = new ArrayList<AiSprite>();					// used for remembering the collisions that occured in the previous turn, ensures not multiple collisions detected
	private int prevCollision;
	
	private LabelButton confirmRouteSelectionButton;
	private LabelButton routeSelectionButton;
	protected boolean isSelectingRoute = false;													// boolean that says whether the playyer is currently in route selection mode
	private ArrayList<Train> selectedTrains = new ArrayList<Train>();							// the train that is being used to use in route selection mode 
	private ArrayList<RouteLocation> newRoute = new ArrayList<RouteLocation>();					// the (potentially incomplete) route at that point
	private int newRouteDistance;			
	
	

	public GameScene(Player player1In, Player player2In){
		super(player1In, player2In, false);
		nextTurn();
		player1Go = true;
		player1In.setFuel(700);
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
		
		if (map != null){
			numberTurns++;
			ArrayList<AiSprite> collisions = getCollisions();
			if (collisions.size() > 0) {
				calculateCollisions(collisions);
				prevCollision = numberTurns;
			} else if (numberTurns - prevCollision > 2) {
				// stops repeated collisions if one players train is following the other players train
				previousCollisions = new ArrayList<AiSprite>();
			}
		}
	}
	
	@Override
	public void Update() {
		if (selectedTrains.size() > 1){
			trainSelectionScene.setElements(selectedTrains);
			Game.pushScene(trainSelectionScene);
			
		} 
		if (newRoute.size() == 0 && selectedTrains.size() > 0){
			selectStartingLocation(selectedTrains.get(0).getStation());
		}
	}
	
	public void delayedCreate() {
		// map setup
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.backgroundZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(map);
		
		// scene setup
		shopScene = new ShopScene(this, this.player1, this.player2);
		goalsScene = new GoalsScene(this, this.player1, this.player2);
		resourceScene = new CurrentResourcesScene(this, this.player1, this.player2);
		trainSelectionScene = new SelectionScene() {
			@Override
			public void onSelectionEnd() {
				selectedTrains = new ArrayList<Train>();
				selectedTrains.add(0, (Train) elements.get(selectedElementIndex));
				Game.popScene();
			}
		};
	
		//Locations setup
		curvedPaths = new ArrayList<CurvedPath>();
		Station london = createStation(this, "London", 210, 390);
		Station rome = createStation(this, "Rome", 415, 168);
		Station moscow = createStation(this, "Moscow", 800, 450);
		Station lisbon = createStation(this, "Lisbon", 30, 120);
		Station paris = createStation(this, "Paris", 300, 340);
		Station berlin = createStation(this, "Berlin", 410, 400);
		Station madrid = createStation(this, "Madrid", 120, 150);
		Station budapest = createStation(this, "Budapest", 520, 280);
		Station krakow = createStation(this, "Krakow", 520, 350);
		Junction junction1 = createJunction(this, "J1", 352, 268);
		Junction junction2 = createJunction(this, "J2", 455, 321);
		System.out.println("Locations created");
		
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
		connectRouteLocations(lisbon, madrid);

		// add trains
		Texture trainTexture = new Texture("elec1.png"); 
		Texture carriageTexture = new Texture("elec1Carriage.png");
		createTrainAndCarriage(player1, "train1", london, trainTexture, carriageTexture, 1, 20, 1 , 0.0001f);
		createTrainAndCarriage(player1, "train2", london, trainTexture, carriageTexture, 1, 50, 1 , 0.0001f);
		Train train = createTrainAndCarriage(player2, "train3", lisbon, trainTexture, carriageTexture, 2, 100, 1 , 0.0001f);
		Route route1 = restoreRoute("LondonParisBerlin");
		train.restoreRoute(route1, 1, 1f);
		
		// create route (with dotted line)
		Texture text = new Texture("route.png");
		final int divider = 10; // distance between 2 dots
		// go to every x * divider, find closest distance value from path array and use that to get corresponding position
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
						// route must be at least 2 locations, finish on a station
						selectedTrains.get(0).setRoute(new Route(newRoute));
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

	private Train createTrainAndCarriage(final Player player, String trainName, Station station, Texture trainTexture, Texture carriageTexture, int weight, int speed, int fuelEfficiency, float reliability) {
		// create a train, carriage and connect them
		Train train = new Train(this, player, trainName, trainTexture, station, weight, speed, fuelEfficiency, reliability) ;
		Add(train);
		Carriage carriage = new Carriage(this, carriageTexture, player, station, train);
		Add(carriage);
		player.addAiSprite(train);
		player.addAiSprite(carriage);
		train.setCarriage(carriage);
		return train;
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
	
	private Route restoreRoute(String string) {
		// creates a route from a string of form "Routelocation1Routelocation2"
		// Assumes valid input string
		if (string.length() == 0){
			return null;
		}
		// parse the string to get the seperate locations
		ArrayList<String> locations = new ArrayList<String>();
		int startSubString = 0;
		for (int i = 1; i< string.length();i++){
			if (i == string.length()-1){
				locations.add(string.substring(startSubString, i+1));
			}
			if (Character.isUpperCase(string.charAt(i))) {
				locations.add(string.substring(startSubString, i));
				startSubString = i;	
			} 
		}
		
		// create the new route by getting the station with the corresponding text from the routelocations array
		ArrayList<RouteLocation> newRouteLocations = new ArrayList<RouteLocation>();
		for (RouteLocation routeLocation: routeLocations){
			if (locations.contains(routeLocation.getName())) {
				newRouteLocations.add(routeLocation);
			}
		}
		Route tempRoute = new Route(newRouteLocations);
		return tempRoute;
	}
	
	private void calculateCollisions(ArrayList<AiSprite> collisions) {
		// main method that resolves valid collisions
		ArrayList<AiSprite> currentCollisions = new ArrayList<AiSprite>(); // collisions that have been resolved, tracked to stop repeated collisions
		for (int i = 0; i < collisions.size(); i+=2){
			boolean previousCollision = false;
			AiSprite p1AiSprite = collisions.get(i);
			AiSprite p2AiSprite = collisions.get(i+1);
			int collisionType = getCollisionType(p1AiSprite, p2AiSprite);
			previousCollision = isPreviousCollision(previousCollisions, p1AiSprite, p2AiSprite, collisionType) 
					|| isPreviousCollision(currentCollisions, p1AiSprite, p2AiSprite, collisionType);
			// calculates if collision previously occurred and stops multiple collisions if train/ carriage overlaps
			
			// resolve the collisions based on collision type
			if (!previousCollision){
				switch(collisionType){
				case(1):
					((Carriage) p2AiSprite).decreaseCarriageCount();
				break;
				case(2):
					((Carriage) p1AiSprite).decreaseCarriageCount();
				break;
				case(3):
					resolveTrainCollision((Train) p1AiSprite, (Train) p2AiSprite);
				break;
				default:
					break;
				}
				currentCollisions.add(p1AiSprite);
				currentCollisions.add(p2AiSprite);
			}
		}
		previousCollisions = collisions;
	}

	private boolean isPreviousCollision(ArrayList<AiSprite> aiSprites, AiSprite p1AiSprite, AiSprite p2AiSprite, int collisionType) {
		// calculates if a collision has occurred between 2 sprites or their respective connected trains/carriages in aiSprites array
		for (int x=0; x<aiSprites.size(); x+=2){
			AiSprite p1CollidedSprite = aiSprites.get(x);
			AiSprite p2CollidedSprite = aiSprites.get(x+1);
			if (p1CollidedSprite.equals(p1AiSprite) && p2CollidedSprite.equals(p2AiSprite)){
				// if the 2 sprites have collided
				return true;
			} else if (collisionType == 1){
				// p1 train, p2 carriage
				if (p1CollidedSprite.equals(p1AiSprite) && p2CollidedSprite.equals(((Carriage) p2AiSprite).getTrain())){
					// if the p1 train collided with the p2 train
					return true;
				} else if (p1CollidedSprite.equals(((Train) p1AiSprite).getCarriage()) && p2CollidedSprite.equals(((Carriage) p2AiSprite).getTrain())){
					// if the p1 carriage collided with p2 train 
					return true;
				}
			} else if (collisionType == 2){
				// p1 carriage, p2 train
				if (p1CollidedSprite.equals(((Carriage) p1AiSprite).getTrain()) && p2CollidedSprite.equals(p2AiSprite)){
					// if the p1 train collided with the p2 train
					return true;
				} else if (p1CollidedSprite.equals(((Carriage) p1AiSprite).getTrain()) && p2CollidedSprite.equals(((Train) p1AiSprite).getCarriage())){
					// if the p1 train collided with p2 carriage
					return true;
				}
			} else if (collisionType == 3){
				// p1 train, p2 train
				if (p1CollidedSprite.equals(p1AiSprite) && p2CollidedSprite.equals(((Train) p2AiSprite).getCarriage())){
					// if the p1 train collided with the p2 train
					return true;
				} else if (p1CollidedSprite.equals(((Train) p1AiSprite).getCarriage()) && p2CollidedSprite.equals(p2AiSprite)){
					// if the p1 carriage collided with p2 train
					return true;
				}
			}
		}
		return false;
	}
	
	private void resolveTrainCollision(Train p1Train, Train p2Train){
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
		// returns array where every even element is player1 collided aiSprite, odd element is player2 collided aiSprite
		ArrayList<AiSprite> collidedAiSprites = new ArrayList<AiSprite>();
		for (AiSprite p1AiSprite: player1.getAiSprites()) {
			for (AiSprite p2AiSprite: player2.getAiSprites()) {
				if (getCollisionType(p1AiSprite, p2AiSprite) >0){
					collidedAiSprites.add(p1AiSprite);
					collidedAiSprites.add(p2AiSprite);
				}
			}
		}
		return collidedAiSprites;
	}

	private int getCollisionType(AiSprite aiSprite1, AiSprite aiSprite2) {
		// tests whether 2 aiSprites have collided using their respective polygons, retuns number corresponding to collision type
		Polygon poly1 = aiSprite1.getPolygon();
		Polygon poly2 = aiSprite2.getPolygon();
		// if collision has already occurred
		if (!(Intersector.overlapConvexPolygons(poly1.getTransformedVertices(), poly2.getTransformedVertices(), null))){
			// no collision
			return -1;
		// otherwise a collision has occured
		} else if (aiSprite1.getClass() == Train.class && aiSprite2.getClass() == Carriage.class){
			// train->carriage collision
			return 1;
		} else if (aiSprite1.getClass() == Carriage.class && aiSprite1.getClass() == Train.class){
			// carriage->train collision
			return 2;
		} else if (aiSprite1.getClass() == Train.class && aiSprite2.getClass() == Train.class) {
			// train->train collision
			return 3;
		} else {
			// carriage-> carriage collision
			return 0;
		}
		
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
			aiSprite.setAlpha(0.2f);
			if (aiSprite.getClass() == Carriage.class){
				((Carriage) aiSprite).setLabelAlpha(0.2f);
			}
		}
		
		// change alpha of own players carriages so can see locations the carriage covers
		ArrayList<AiSprite> playerAiSprites = activePlayer().getAiSprites();
		for (AiSprite aiSprite : playerAiSprites){
			if (aiSprite.getClass() == Carriage.class){
				aiSprite.setAlpha(0.5f);
				((Carriage) aiSprite).setLabelAlpha(0.5f);
			}
		}
	}
	
	public void addSelectedTrain(Train train) {
		selectedTrains.add(train);
	}
	
	public ArrayList<RouteLocation> getNewRoute() {
		return newRoute;
	}

	public boolean isRouteSelecting() {
		return isSelectingRoute;
	}
	
	public void selectStartingLocation(RouteLocation startLocation){
		// used when the initial train has been selecting, given the attached starting location
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
			//previousConnectedLocations.add(connectedLocation);
			connectedLocation.setTexture(redLocationTexture);
			if (connectedLocation.getClass() == Station.class){
				((Station) connectedLocation).setFont(Label.genericFont(Color.RED, 20));
			}
		}
		System.out.println("Selected trains: " + selectedTrains.size());
		
		// all other trains have alpha lowered to see locations, ensure player knows which train is selected
		ArrayList<AiSprite> aiSprites = activePlayer().getAiSprites();
		for (AiSprite aiSprite: aiSprites){
			if (aiSprite != selectedTrains.get(0)){
				aiSprite.setAlpha(0.7f);
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
			ArrayList<Connection> previousConnectedLocations = newRoute.get(newRoute.size()-1).getConnections();
			for (Connection tempConnection: previousConnectedLocations){
				RouteLocation tempLocation = tempConnection.getTargetLocation();
				if (!(newRoute.contains(tempLocation))){
					if (tempLocation.getClass() == Station.class){
						tempLocation.setTexture(locationTexture);
						((Station) tempLocation).setFont(Label.genericFont(Color.BLACK, 20));
					} else {
						tempLocation.setTexture(junctionTexture);
					}
				}
			}
			
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
					//previousConnectedLocations.add(connectedLocation);
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
		// reverse everything changed in startSelectingRoute 
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
		// reset the other players aiSprites
		ArrayList<AiSprite> otherAiSprites = otherPlayer.getAiSprites();
		for (AiSprite aiSprite : otherAiSprites){
			aiSprite.setColor(Color.WHITE);
			aiSprite.setAlpha(1f);
			if (aiSprite.getClass() == Carriage.class){
				((Carriage) aiSprite).setLabelAlpha(1f);
			}
		}
		
		// reset alpha of players other trains and carriages
		ArrayList<AiSprite> aiSprites = activePlayer().getAiSprites();
		for (AiSprite aiSprite: aiSprites){
			aiSprite.setAlpha(1f);
			if (aiSprite.getClass() == Carriage.class){
				((Carriage) aiSprite).setLabelAlpha(1f);
			}
		}
		
		selectedTrains = new ArrayList<Train>();
		newRoute = new ArrayList<RouteLocation>();
		newRouteDistance = 0;
	}

	private HashMap<String, CurvedPath> getPaths() {
		// this creates all of the paths on the map and returns them
		HashMap<String, CurvedPath> paths = new HashMap<String, CurvedPath>();
		// the string key is the two routelocation string representations concatenated in the order that corresponds to the path
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
		paths.put("ParisJ1", parisJunction1);
		curvedPaths.add(parisJunction1);
		
		Vector2[] rdataSet2 = reverseDataset(dataSet2);	
		CurvedPath junction1Paris = new CurvedPath(rdataSet2, false);
		paths.put("J1Paris", junction1Paris);

		
		Vector2[] dataSet3 = new Vector2[5];
		dataSet3[0] = (new Vector2(415,168));
		dataSet3[1] = (new Vector2(415,168));
		dataSet3[2] = (new Vector2(382,238));
		dataSet3[3] = (new Vector2(352,268));
		dataSet3[4] = (new Vector2(352,268));
		CurvedPath romeJunction1 = new CurvedPath(dataSet3, false);
		paths.put("RomeJ1", romeJunction1);
		curvedPaths.add(romeJunction1);
		
		Vector2[] rdataSet3 = reverseDataset(dataSet3);	
		CurvedPath junction1Rome = new CurvedPath(rdataSet3, false);
		paths.put("J1Rome", junction1Rome);

		
		Vector2[] dataSet4 = new Vector2[6];
		dataSet4[0] = (new Vector2(415, 168));
		dataSet4[1] = (new Vector2(415, 168));
		dataSet4[2] = (new Vector2(405, 245));
		dataSet4[3] = (new Vector2(450, 270));
		dataSet4[4] = (new Vector2(520, 280));
		dataSet4[5] = (new Vector2(520, 280));
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
		dataSet8[2] = (new Vector2(455, 321));
		dataSet8[3] = (new Vector2(455, 321));
		CurvedPath berlinJunction2 = new CurvedPath(dataSet8, false);
		paths.put("BerlinJ2", berlinJunction2);
		curvedPaths.add(berlinJunction2);
		
		Vector2[] rdataSet8 = reverseDataset(dataSet8);
		CurvedPath junction2Berlin = new CurvedPath(rdataSet8, false);
		paths.put("J2Berlin", junction2Berlin);

		
		Vector2[] dataSet9 = new Vector2[4];
		dataSet9[0] = (new Vector2(459, 321));
		dataSet9[1] = (new Vector2(459, 321));
		dataSet9[2] = (new Vector2(520, 280));
		dataSet9[3] = (new Vector2(520, 280));
		CurvedPath Junction2Budapest = new CurvedPath(dataSet9, false);
		paths.put("J2Budapest", Junction2Budapest);
		curvedPaths.add(Junction2Budapest);
		
		Vector2[] rdataSet9 = reverseDataset(dataSet9);
		CurvedPath budapestJunction2 = new CurvedPath(rdataSet9, false);
		paths.put("BudapestJ2", budapestJunction2);
		
		
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
		dataSet11[2] = (new Vector2(594, 463));
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
		dataSet12[2] = (new Vector2(455, 321));
		dataSet12[3] = (new Vector2(455, 321));
		CurvedPath krakowJunction2 = new CurvedPath(dataSet12, false);
		paths.put("KrakowJ2", krakowJunction2);
		curvedPaths.add(krakowJunction2);
		
		Vector2[] rdataSet12 = reverseDataset(dataSet12);
		CurvedPath junction2Krakow = new CurvedPath(rdataSet12, false);
		paths.put("J2Krakow", junction2Krakow);
		
		
		Vector2[] dataSet13 = new Vector2[4];
		dataSet13[0] = (new Vector2(455, 321));
		dataSet13[1] = (new Vector2(455, 321));
		dataSet13[2] = (new Vector2(352, 268));
		dataSet13[3] = (new Vector2(352, 268));
		CurvedPath junction2Junction1 = new CurvedPath(dataSet13, false);
		paths.put("J2J1", junction2Junction1);
		curvedPaths.add(junction2Junction1);
		
		Vector2[] rdataSet13 = reverseDataset(dataSet13);
		CurvedPath junction1Junction2 = new CurvedPath(rdataSet13, false);
		paths.put("J1J2", junction1Junction2);
		
		
		Vector2[] dataSet14 = new Vector2[4];
		dataSet14[0] = (new Vector2(352, 268));
		dataSet14[1] = (new Vector2(352, 268));
		dataSet14[2] = (new Vector2(120, 150));
		dataSet14[3] = (new Vector2(120, 150));
		CurvedPath junction1Madrid = new CurvedPath(dataSet14, false);
		paths.put("J1Madrid", junction1Madrid);
		curvedPaths.add(junction1Madrid);
		
		Vector2[] rdataSet14 = reverseDataset(dataSet14);
		CurvedPath madridJunction1 = new CurvedPath(rdataSet14, false);
		paths.put("MadridJ1", madridJunction1);
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
	
	public Player activePlayer() {
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

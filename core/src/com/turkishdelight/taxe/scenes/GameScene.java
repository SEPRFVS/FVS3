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
import com.turkishdelight.taxe.worldobjects.Location;

public class GameScene extends GameGUIScene {

	Texture mapText;
	SpriteComponent map;
	public ShopScene shopScene;
	public GoalsScene goalsScene;
	public CurrentResourcesScene resourceScene;
	
	private Location london;
	private Location rome;
	private Location moscow;
	private Location lisbon;
	private Location paris;
	private Location berlin;
	private Location madrid;
	private Location budapest;
	private Location krakow;
	protected boolean isSelectingRoute;
	private ArrayList<Location> newRoute;					
	private int newRouteDistance;							// TODO currently only used to print- should be diplayed
	private Train selectedTrain;
	private LabelButton confirmRouteSelectionButton;
	private LabelButton routeSelectionButton;

	private ArrayList<CurvedPath> curvedPaths;				// collection of curved paths (only one way for wach path) for drawing
	private ArrayList<Location> locations;
	private ArrayList<AiSprite> previousCollisions;
	private ArrayList<Location> previousConnections;

	public GameScene(Player player1In, Player player2In){
		super(player1In, player2In, false);
		nextTurn();
		player1Go = true;
		delayedCreate();
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
	
	public void nextTurn() {
		if (map != null)
			if (getCollisions().size() > 0) {
				calculateCollisions();
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

		locations = new ArrayList<Location>();
		previousCollisions = new ArrayList<AiSprite>();
		isSelectingRoute = false;
		previousConnections = new ArrayList<Location>();
		//Locations setup
		curvedPaths = new ArrayList<CurvedPath>();
		london = createLocation(this, "London", 210, 390);
		rome = createLocation(this, "Rome", 415, 168);
		moscow = createLocation(this, "Moscow", 800, 450);
		lisbon = createLocation(this, "Lisbon", 30, 120);
		paris = createLocation(this, "Paris", 300, 340);
		berlin = createLocation(this, "Berlin", 410, 400);
		madrid = createLocation(this, "Madrid", 120, 150);
		budapest = createLocation(this, "Budapest", 510, 290);
		krakow = createLocation(this, "Krakow", 520, 350);
		System.out.println("Locations created");
		
		// setup connections
		connectLocations(london, paris); 
		connectLocations(paris, rome);
		connectLocations(rome, budapest);
		connectLocations(london, lisbon);
		connectLocations(madrid, krakow);
		connectLocations(lisbon, madrid);
		connectLocations(paris, berlin);
		connectLocations(berlin, budapest);
		connectLocations(krakow, moscow);
		connectLocations(moscow, budapest);

		// add trains
		Texture trainTexture = new Texture("traincropped.png"); // traincropped added to allow more accurate collision detection
		createTrainAndCarriage(trainTexture, 1, london, player1);
		createTrainAndCarriage(trainTexture, 2, moscow, player2);
		
		// create route (with dotted line)
		Texture text = new Texture("route.png");
		final int divider = 10; // routeDistance between 2 dots
		// go to every x * divider, find closest routeDistance value from curvedPath array and use that to get corresponding position
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
					if (newRoute.size() > 1) {
						selectedTrain.setPath(new Route(newRoute));
						System.out.println("Route completed: " + newRoute.toString());
						endSelectingRoute();
						isSelectingRoute = false;
					}
				} 
			}
		};
		confirmRouteSelectionButton.setPosition(Game.targetWindowsWidth -150, Game.targetWindowsHeight -200);
		confirmRouteSelectionButton.setSize(0, 0);
		confirmRouteSelectionButton.setText(" ");
		confirmRouteSelectionButton.setAlignment(0);
		Add(confirmRouteSelectionButton);
	}
	
	private Player activePlayer() {
		if (player1Go){
			return player1;
		} else {
			return player2;
		}
	}

	protected void startSelectingRoute() {
		// change buttons accordingly
		nextGoButton.setSize(0,0);
		confirmRouteSelectionButton.setText("Confirm Route");
		confirmRouteSelectionButton.setSize(100, 40);
		routeSelectionButton.setText("Cancel Routing");
		// enable the locations to be clickable
		newRoute = new ArrayList<Location>();
		for (Location location:locations){
			location.setSelectingRoute(true);
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
	
	public void selectLocation(Location location){
		if ((newRoute.size() > 0) && (location.isConnected(newRoute.get(newRoute.size()-1)) && !newRoute.contains(location))){
			// if the starting location/ train has been selected, if the location is connected to the previous location
			// and if the new location isnt already selected, select the location
			Texture locationTexture = new Texture("location.png");
			for (Location tempLocation: previousConnections){
				tempLocation.setTexture(locationTexture);
				tempLocation.setFont(Label.genericFont(Color.BLACK, 20));
			}
			newRoute.add(location);
			location.setFont(Label.genericFont(Color.BLUE, 20));
			updateRouteLabel(location);
			Texture blueLocationTexture = new Texture("blueLocation.png");
			location.setTexture(blueLocationTexture);
			
			ArrayList<Connection> connections = location.getConnections();
			previousConnections = new ArrayList<Location>();
			Texture redLocationTexture = new Texture("redLocation.png");
			// give any selectable, connected locations red text
			for (Connection connection: connections) {
				Location connectedLocation = connection.getLocation();
				if (connectedLocation.equals(newRoute.get(newRoute.size()-2))){
					newRouteDistance += connection.getPath().getFinalDistance();
				}
				if (!newRoute.contains(connectedLocation)){
					connectedLocation.setFont(Label.genericFont(Color.RED, 20));
					connectedLocation.setTexture(redLocationTexture);
					previousConnections.add(connectedLocation);
				}
			}
			System.out.println("Current route routeDistance = " + newRouteDistance);
		}
	}

	private void endSelectingRoute(){
		// reverse everything changed in startSelectingRoute wipe locations array
		// reset buttons
		nextGoButton.setSize(83, 44);
		confirmRouteSelectionButton.setText(" ");
		confirmRouteSelectionButton.setSize(0, 0);
		routeSelectionButton.setText("Select Route");
		// reset locations
		Texture locationTexture = new Texture("location.png");
		for (Location location:locations){
			location.setSelectingRoute(false);
			location.setFont(Label.genericFont(Color.BLACK, 20));
			location.setTexture(locationTexture);
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
		
		newRoute = new ArrayList<Location>();
		newRouteDistance = 0;
	}
	
	private void updateRouteLabel(Location location){
		Label label = new Label(this, new Texture("Clear_Button.png"), Label.genericFont(Color.BLACK, 20));
		label.setText(location.getName());
		label.setPosition(Game.targetWindowsWidth - 100, ((Game.targetWindowsHeight - 200) - (newRoute.size()*30)));
		Add(label);
	}
	
	public boolean isRouteSelecting() {
		return isSelectingRoute;
	}
	
	private void createTrainAndCarriage(Texture trainTexture, int weight, Location location, final Player player) {
		// create a train, carriage and connect the 2
		// select multiple trains on same location, arraylist of trains, check in update if the arraylist is >1
		Train train = new Train(this, trainTexture, location, weight) {
			@Override
			public void onClickEnd()
			{
				if (isRouteSelecting() && activePlayer().equals(player) && (this.getStation()!= null) && newRoute.size() == 0){
					// if the train is clicked on when at a station and in route selection mode, start the route from here
					selectedTrain = this;
					Location startLocation = getStation();
					newRoute.add(startLocation);
					startLocation.setFont(Label.genericFont(Color.BLUE, 20));
					updateRouteLabel(startLocation);
				}
			}
		};
		Add(train);
		Carriage carriage = new Carriage(this, trainTexture, location, train);
		Add(carriage);
		player.addAiSprite(train);
		player.addAiSprite(carriage);
		train.setCarriage(carriage);
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
				if (collisionOccured(p1AiSprite, p2AiSprite)){
					collidedAiSprites.add(p1AiSprite);
					collidedAiSprites.add(p2AiSprite);
				}
			}
		}
		return collidedAiSprites;
	}

	private boolean collisionOccured(AiSprite aiSprite1, AiSprite aiSprite2) {
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

	private Location createLocation(GameScene parentScene, String locationName, int x , int y) {
		Location location = new Location(parentScene, locationName, x,y);
		location.setPosition(x, y);
		Add(location);
		locations.add(location);
		return location;
	}	

	private void connectLocations(Location l1, Location l2){
		HashMap<String, CurvedPath> paths = getPaths();
		CurvedPath path1 = paths.get(l1.getName() + l2.getName());
		CurvedPath path2 = paths.get(l2.getName() + l1.getName());
		l1.addConnection(l2, path1);
		l2.addConnection(l1, path2); 
	}
	

	private HashMap<String, CurvedPath> getPaths() {
		// this creates all of the paths on the map
		// TODO currently requires making a route each way, clean up the process
		HashMap<String, CurvedPath> paths = new HashMap<String, CurvedPath>();

		// first last control points must be same due to catmullromspline maths
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
		


		Vector2[] dataSet2 = new Vector2[6];
		dataSet2[0] = (new Vector2(300, 340));
		dataSet2[1] = (new Vector2(300, 340));
		dataSet2[2] = (new Vector2(320, 300));
		dataSet2[3] = (new Vector2(360, 250));
		dataSet2[4] = (new Vector2(415, 168));
		dataSet2[5] = (new Vector2(415, 168));
		CurvedPath parisRome = new CurvedPath(dataSet2, false);
		paths.put("ParisRome", parisRome);
		curvedPaths.add(parisRome);
		
		Vector2[] rdataSet2 = reverseDataset(dataSet2);	
		CurvedPath romeParis = new CurvedPath(rdataSet2, false);
		paths.put("RomeParis", romeParis);

		

		Vector2[] dataSet3 = new Vector2[6];
		dataSet3[0] = (new Vector2(415, 168));
		dataSet3[1] = (new Vector2(415, 168));
		dataSet3[2] = (new Vector2(405, 245));
		dataSet3[3] = (new Vector2(450, 270));
		dataSet3[4] = (new Vector2(510, 290));
		dataSet3[5] = (new Vector2(510, 290));
		CurvedPath romeBudapest = new CurvedPath(dataSet3, false);
		paths.put("RomeBudapest", romeBudapest);
		curvedPaths.add(romeBudapest);
		
		Vector2[] rdataSet3 = reverseDataset(dataSet3);
		CurvedPath budapestRome = new CurvedPath(rdataSet3, false);
		paths.put("BudapestRome", budapestRome);

		

		Vector2[] dataSet4 = new Vector2[7];
		dataSet4[0] = (new Vector2(210, 390));
		dataSet4[1] = (new Vector2(210, 390));
		dataSet4[2] = (new Vector2(210, 365));
		dataSet4[3] = (new Vector2(180, 200));
		dataSet4[4] = (new Vector2(80, 220));
		dataSet4[5] = (new Vector2(30, 120));
		dataSet4[6] = (new Vector2(30, 120));
		CurvedPath londonLisbon = new CurvedPath(dataSet4, false);
		paths.put("LondonLisbon", londonLisbon);
		curvedPaths.add(londonLisbon);
		
		Vector2[] rdataSet4 = reverseDataset(dataSet4);
		CurvedPath lisbonLondon = new CurvedPath(rdataSet4, false);
		paths.put("LisbonLondon", lisbonLondon);
		
		

		Vector2[] dataSet5 = new Vector2[4];
		dataSet5[0] = (new Vector2(520, 350));
		dataSet5[1] = (new Vector2(520, 350));
		dataSet5[2] = (new Vector2(120, 150));
		dataSet5[3] = (new Vector2(120, 150));
		CurvedPath krakowMadrid = new CurvedPath(dataSet5, false);
		paths.put("KrakowMadrid", krakowMadrid);
		curvedPaths.add(krakowMadrid);
		
		Vector2[] rdataSet5 = reverseDataset(dataSet5);
		CurvedPath madridKrakow = new CurvedPath(rdataSet5, false);
		paths.put("MadridKrakow", madridKrakow);

		

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
		dataSet8[2] = (new Vector2(510, 290));
		dataSet8[3] = (new Vector2(510, 290));
		CurvedPath berlinBudapest = new CurvedPath(dataSet8, false);
		paths.put("BerlinBudapest", berlinBudapest);
		curvedPaths.add(berlinBudapest);
		
		Vector2[] rdataSet8 = reverseDataset(dataSet8);
		CurvedPath budapestBerlin = new CurvedPath(rdataSet8, false);
		paths.put("BudapestBerlin", budapestBerlin);

		

		Vector2[] dataSet9 = new Vector2[5];
		dataSet9[0] = (new Vector2(520, 350));
		dataSet9[1] = (new Vector2(520, 350));
		dataSet9[2] = (new Vector2(700, 370));
		dataSet9[3] = (new Vector2(800, 450));
		dataSet9[4] = (new Vector2(800, 450));
		CurvedPath krakowMoscow = new CurvedPath(dataSet9, false);
		paths.put("KrakowMoscow", krakowMoscow);
		curvedPaths.add(krakowMoscow);

		Vector2[] rdataSet9 = reverseDataset(dataSet9);
		CurvedPath moscowKrakow = new CurvedPath(rdataSet9, false);
		paths.put("MoscowKrakow", moscowKrakow);

		

		Vector2[] dataSet10 = new Vector2[6];
		dataSet10[0] = (new Vector2(510, 290));
		dataSet10[1] = (new Vector2(510, 290));
		dataSet10[2] = (new Vector2(600, 290));
		dataSet10[3] = (new Vector2(700, 420));
		dataSet10[4] = (new Vector2(800, 450));
		dataSet10[5] = (new Vector2(800, 450));
		CurvedPath budapestMoscow = new CurvedPath(dataSet10, false);
		paths.put("BudapestMoscow", budapestMoscow);
		curvedPaths.add(budapestMoscow);
		
		Vector2[] rdataSet10 = reverseDataset(dataSet10);
		CurvedPath moscowBudapest = new CurvedPath(rdataSet10, false);
		paths.put("MoscowBudapest", moscowBudapest);
		
		
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
}

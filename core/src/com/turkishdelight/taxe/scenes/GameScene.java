package com.turkishdelight.taxe.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.goals.ArrivalObjective;
import com.turkishdelight.taxe.goals.EmptyObjective;
import com.turkishdelight.taxe.goals.EventHandler;
import com.turkishdelight.taxe.goals.Goal;
import com.turkishdelight.taxe.goals.Objective;
import com.turkishdelight.taxe.goals.RouteObjective;
import com.turkishdelight.taxe.guiobjects.Button;
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

	//Resource variables
	int fuel = 10000;
	int crPer100Fuel = 10;
	
	//This arraylist tracks the active goals of the game
	public ArrayList<Goal> activeGoals = new ArrayList<Goal>();
	public ArrayList<Goal> goalsToDrop = new ArrayList<Goal>();
	Texture mapText;
	SpriteComponent map;
	public ShopScene shopScene;
	public GoalsScene goalsScene;
	public CurrentResourcesScene resourceScene;
	public DialogueScene dialogueScene;
	private SelectionScene trainSelectionScene;
	private PauseMenuScene pauseScene;
	
	public int numberTurns = 0;
	private ArrayList<CurvedPath> curvedPaths = new ArrayList<CurvedPath>();					// collection of curved paths (only one way for each path) for drawing
	private ArrayList<RouteLocation> routeLocations = new ArrayList<RouteLocation>();			// collection of routelocations (junction/station)
	
	private ArrayList<AiSprite> previousCollisions = new ArrayList<AiSprite>();					// used for remembering the collisions that occured in the previous turn, ensures not multiple collisions detected
	private int prevCollision;
	
	private LabelButton confirmRouteSelectionButton;
	private LabelButton routeSelectionButton;
	protected boolean isSelectingRoute = false;													// boolean that says whether the player is currently in route selection mode
	private ArrayList<Train> selectedTrains = new ArrayList<Train>();							// the train that is being used to use in route selection mode 
	private ArrayList<RouteLocation> newRoute = new ArrayList<RouteLocation>();					// the (potentially incomplete) route at that point
	private int newRouteDistance;		
	public EventHandler events;
	private HashMap<String, CurvedPath> paths;
	

	public GameScene(Player player1In, Player player2In){
		super(player1In, player2In, false, null);
		player1In.setActiveGame(this);
		player2In.setActiveGame(this);
		player1Go = true;
		delayedCreate();
	}
	
	//Constructor for loading a game. While the players are loaded before the game is created, further data must be loaded afterwards
	public GameScene(Player loadedPlayer, Player loadedPlayer2, String gameData)
	{
		this(loadedPlayer, loadedPlayer2);
		System.out.println("Loading game!");
		loadData(gameData);
	}
	
	public Train.Type getTrainInstanceByName(String name)
	{
		for(Train.Type t : Train.Type.getEnumConstants())
		{
			if(t.getName().equals(name))
			{
				return t;
			}
		}
		return null;
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
		// used to allow multiple train on same lcoation, select one of them
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
		shopScene = new ShopScene(this, this.getPlayer1(), this.getPlayer2());
		goalsScene = new GoalsScene(this, this.getPlayer1(), this.getPlayer2());
		resourceScene = new CurrentResourcesScene(this, this.getPlayer1(), this.getPlayer2());
		trainSelectionScene = new SelectionScene(new Texture("trainselection.png")) {
			@Override
			public void onSelectionEnd() {
				selectedTrains = new ArrayList<Train>();
				selectedTrains.add(0, (Train) elements.get(selectedElementIndex));
				Game.popScene();
			}
		};
		pauseScene = new PauseMenuScene(this);
	
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
		
		paths = getPaths();
		
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
		generateTrainAndCarriage(getPlayer1(), (Station)getStationByName(getPlayer1().getStartLocation()), Train.Type.STEAM);
		generateTrainAndCarriage(getPlayer2(), (Station)getStationByName(getPlayer2().getStartLocation()), Train.Type.STEAM);

		
		// draw route (with dotted line)
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
		shopScene = new ShopScene(this, this.getPlayer1(), this.getPlayer2());
		goalsScene = new GoalsScene(this, this.getPlayer1(), this.getPlayer2());
		resourceScene = new CurrentResourcesScene(this, this.getPlayer1(), this.getPlayer2());
		dialogueScene = new DialogueScene("");
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
		confirmRouteSelectionButton = new LabelButton(this, clearButtonTexture, 100 , 40, Label.genericFont(Color.WHITE, 20)){
			@Override
			public void onClickEnd()
			{
				if (isSelectingRoute) {
					if (newRoute.size() > 1 && (newRoute.get(newRoute.size()-1).getClass() == Station.class)) {
						// route must be at least 2 locations, finish on a station
						Train trainSelected = selectedTrains.get(0);
						trainSelected.setRoute(new Route(newRoute));
						trainSelected.getCarriage().setRoute();
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
		
		//Buttons set up
		Texture buttonText = new Texture("Clear_Button.png");
		//Create settings menu button
		Button settingsButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.pushScene(pauseScene);
			}
		};
		settingsButton.setZ(Game.guiZ);
		settingsButton.setPosition(924, 0);
		settingsButton.setSize(100, 100);
		settingsButton.setTexture(buttonText);
		Add(settingsButton);
		//Create settings menu button
		
		Button leaderButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.pushScene(makeDialogueScene("Leaderboard coming soon!"));
			}
		};
		leaderButton.setZ(Game.guiZ);
		leaderButton.setPosition(814, 0);
		leaderButton.setSize(100, 100);
		leaderButton.setTexture(buttonText);
		Add(leaderButton);
		
		events = new EventHandler();
		Objective mainObjective = ArrivalObjective.generate();
		Objective sideObjective = EmptyObjective.generate();
		Goal g = new Goal(this, mainObjective, sideObjective, sideObjective);
		this.activeGoals.add(g);
		mainObjective = RouteObjective.generate();
		sideObjective = EmptyObjective.generate();
		g = new Goal(this, mainObjective, sideObjective, sideObjective);
		this.activeGoals.add(g);
		generateGoals();
		player1Active();
	}
	
	public EventHandler getEventHandler()
	{
		return events;
	}
	
	public ArrayList<RouteLocation> getLocations(){
		return routeLocations;
	}
	
	private Train createTrainAndCarriage(final Player player, String trainName, Station station, Texture trainTexture, Texture carriageTexture, int weight, int speed, int fuelEfficiency, float reliability) {
		// create a train, carriage and connect them
		Train train = new Train(this, player, trainName, trainTexture, station, weight, speed, fuelEfficiency, reliability) ;
		
		Carriage carriage = new Carriage(this, carriageTexture, player, station, train);
		Add(carriage);
		Add(train);
		player.addAiSprite(train);
		player.addAiSprite(carriage);
		train.setCarriage(carriage);
		return train;
	}
	
	public Train generateTrainAndCarriage(Player player, Station station, Train.Type type) {
		if (player == null || station == null){
			return null;
		}
		return this.createTrainAndCarriage(player, type.getName(), station, type.getTrainTexture(), type.getCarraigeTexture(), type.getWeight(), type.getSpeed(), type.getEfficiency(), type.getReliability());
	}

	private Station createStation(GameScene parentScene, String locationName, int x , int y) {
		Station routeLocation = new Station(parentScene, locationName, x,y);
		Add(routeLocation);
		routeLocations.add(routeLocation);
		return (Station) routeLocation;
	}	

	public RouteLocation getStationByName(String stationName) {
		for(RouteLocation station : routeLocations)
		{
			if(station.getName().equals(stationName))
			{
				return station;
			}
		}
		return null;
	}
	
	private Junction createJunction(GameScene parentScene, String locationName, int x , int y) {
		Junction routeLocation = new Junction(parentScene, locationName, x,y);
		Add(routeLocation);
		routeLocations.add(routeLocation);
		return (Junction) routeLocation;
		}
	
	public void connectRouteLocations(RouteLocation l1, RouteLocation l2){
		// connects 2 routelocations (junctions + stations) 
		CurvedPath path1 = paths.get(l1.getName() + l2.getName());
		CurvedPath path2 = paths.get(l2.getName() + l1.getName());
		if (path1 != null && path2 != null){
			l1.addConnection(l2, path1);
			l2.addConnection(l1, path2); 
		}
	}
	
	public void connectRouteLocations(RouteLocation l1, RouteLocation l2, HashMap<String, CurvedPath> paths){
		// used for testing only
		// connects 2 routelocations (junctions + stations) 
		CurvedPath path1 = paths.get(l1.getName() + l2.getName());
		CurvedPath path2 = paths.get(l2.getName() + l1.getName());
		if (path1 != null && path2 != null){
			l1.addConnection(l2, path1);
			l2.addConnection(l1, path2); 
		}
	}

	public Route restoreRoute(String string) {
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
		for (String locationName: locations){
			RouteLocation routeLocation = getStationByName(locationName);
			if (routeLocation != null){
				newRouteLocations.add(getStationByName(locationName));
			} else {
				return null;
			}
		}
		
		
		Route tempRoute;
		try {
			tempRoute = new Route(newRouteLocations);
		} catch(IllegalArgumentException e){
			return null;
		}
		return tempRoute;
	}
	
	public ArrayList<AiSprite> getPreviousCollisions(){
		return this.previousCollisions;
	}
	
	public void calculateCollisions(ArrayList<AiSprite> collisions) {
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
					currentCollisions.add(p1AiSprite);
					currentCollisions.add(p2AiSprite);
				break;
				case(2):
					((Carriage) p1AiSprite).decreaseCarriageCount();
					currentCollisions.add(p1AiSprite);
					currentCollisions.add(p2AiSprite);
				break;
				case(3):
					resolveTrainCollision((Train) p1AiSprite, (Train) p2AiSprite);
					currentCollisions.add(p1AiSprite);
					currentCollisions.add(p2AiSprite);
				break;
				default:
					break;
				}
				
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
		if ((p1Train.getWeight()*p1Train.getSpeed()) > (p2Train.getWeight()*p2Train.getSpeed())){
			System.out.println("p1 wins!");
			Carriage carriage = ((Train) p2Train).getCarriage();
			carriage.decreaseCarriageCount();
			
		} else if ((p1Train.getWeight()*p1Train.getSpeed()) < (p2Train.getWeight()*p2Train.getSpeed())){
			System.out.println("p2 wins!");
			Carriage carriage = ((Train) p1Train).getCarriage();
			carriage.decreaseCarriageCount();
			
		} else {
			System.out.println("its a draw!");
		}
	}
	
	public ArrayList<AiSprite> getCollisions(){
		// returns array where every even element is player1 collided aiSprite, odd element is player2 collided aiSprite
		ArrayList<AiSprite> collidedAiSprites = new ArrayList<AiSprite>();
		for (AiSprite p1AiSprite: getPlayer1().getAiSprites()) {
			for (AiSprite p2AiSprite: getPlayer2().getAiSprites()) {
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
	
	public void startSelectingRoute() {
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
		if (activePlayer() == getPlayer1()){
			otherPlayer = getPlayer2();
		} else {
			otherPlayer = getPlayer1();
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
		isSelectingRoute = true;
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
	
	public void setSelectingTrain(Train train){
		// FOR TESTING ONLY
		this.selectedTrains.add(train);
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
		
		// all other trains have alpha lowered to see locations, ensure player knows which train is selected
		ArrayList<AiSprite> aiSprites = activePlayer().getAiSprites();
		for (AiSprite aiSprite: aiSprites){
			if (aiSprite != selectedTrains.get(0)){
				aiSprite.setAlpha(0.7f);
			}
		}
	}
	
	
	public void selectLocation(RouteLocation routeLocation){
		if (newRoute.size() == 0){
			return;
		}
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

	public void endSelectingRoute(){
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
			otherPlayer = getPlayer2();
		} else {
			otherPlayer = getPlayer1();
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
		isSelectingRoute = false;
	}

	public HashMap<String, CurvedPath> getPaths() {
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

	public Vector2[] reverseDataset(Vector2[] dataSet){
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
		getPlayer1().updateTurn(true);
		getPlayer2().updateTurn(false);
		updateGoals();
		for(Goal g : activeGoals)
		{
			g.nextTurn(getPlayer1());
		}
		updateGoals();
		//We recalculate the fuel price every player 1 turn
		regenerateFuelPrice();
	}
	
	@Override
	public void player2Active()
	{
		super.player2Active();
		getPlayer2().updateTurn(true);
		getPlayer1().updateTurn(false);
		updateGoals();
		for(Goal g : activeGoals)
		{
			g.nextTurn(getPlayer2());
		}
		updateGoals();
	}
	
	public void updateGoals()
	{
		for(Goal g : goalsToDrop)
		{
			activeGoals.remove(g);
		}
		goalsToDrop = new ArrayList<Goal>();
		generateGoals();
	}
	
	public void generateGoals()
	{
		activeGoals.trimToSize();
		while (activeGoals.size() < 3)
		{
			Objective mainObjective;
			if(new Random().nextDouble() > 0.5)
			{
				mainObjective = ArrivalObjective.generate();
			}
			else
			{
				mainObjective = RouteObjective.generate();
			}
			Objective sideObjective = EmptyObjective.generate();
			Goal g = new Goal(this, mainObjective, sideObjective, sideObjective);
			this.activeGoals.add(g);
		}
	}
	
	public Player activePlayer() {
		if (player1Go){
			return getPlayer1();
		} else {
			return getPlayer2();
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
		if (!isSelectingRoute)
			Game.pushScene(goalsScene);
	}
	
	@Override
	public void shopToolbarPressed() 
	{
		if (!isSelectingRoute)
			Game.pushScene(shopScene);
		
	}
	
	@Override
	public void resourcesToolbarPressed() 
	{
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
	
	//This method simply removes the necessary to recreate the dialogue scene by reusing it
	public Scene makeDialogueScene(String text)
	{
		dialogueScene.setText(text);
		return dialogueScene;
	}
	
	//This method extends cleanup to nullify scenes
	public void cleanup()
	{
		this.goalsScene.parentGame = null;
		this.goalsScene = null;
		this.resourceScene.parentGame = null;
		this.resourceScene = null;
		this.shopScene.parentGame = null;
		this.shopScene = null;
		this.dialogueScene = null;
		this.pauseScene.parentGame = null;
		this.pauseScene = null;
		super.cleanup();
	}
	
	//This method is used to regenerate the fuel price each turn
	public void regenerateFuelPrice()
	{
		float priceRatio = 10000.0f / fuel;
		this.crPer100Fuel = (int)(10 * priceRatio);
	}
	
	//-----------//
	//Methods for loading and saving the game
	//-----------//
	
	//This method saves the game
	public void save()
	{
		//We separate our players' data and the game data with "!"
		//Individual items of data within this are comma delimited
		String saveString = getPlayerString(getPlayer1()) + "!" + getPlayerString(getPlayer2());
		//Append whose turn it is
		if(this.activePlayer().equals(getPlayer1()))
		{
			saveString += "!1";
		}
		else
		{
			saveString += "!2";
		}
		//Append game fuel. We do not to store the fuel price as this can be regenerated
		saveString += "," + fuel;
		
		//Get a file location to save to
		String locRoot = ""; //Local root set by LibGDX
		String loadFilePath = ""; //Used by the File Chooser to pass path to libGDX FileHandle
		
		//Create filter so only .taxe files may be saved
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TaxE Saves", "taxe");	
				
		//Instantiate new JFileChooser, default directory the local root
		JFileChooser chooser = new JFileChooser(locRoot);
				
		//Apply filter created above
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter); 
		
		// Open Dialog for File Choosing and assign absolute path to loadFilePath
		int returnVal = chooser.showOpenDialog(chooser);
			  if(returnVal == JFileChooser.APPROVE_OPTION) {
			     loadFilePath = chooser.getSelectedFile().getAbsolutePath();
			  }
			  else {
			  	return;
		   }
		if(!loadFilePath.endsWith(".taxe"))
		{
			loadFilePath += ".taxe";
		}
		   
	    //Load file into libgdx's FileHandle System using absolute path.
	    //Have to create a new FileHandle loadedFromChooser.. Doesn't work without this
		FileHandle loadedFromChooser = Gdx.files.absolute(loadFilePath);
		
		//We save the file
		loadedFromChooser.writeString(saveString, false);
		
		//Finally notify the player

		Game.pushScene(makeDialogueScene("Game Saved!"));
	}
	
	//This method generates a store-able string of data representing a specific player
	public String getPlayerString(Player pl)
	{
		//Firstly we store the name, money, fuel and score of the player in a comma delimited string
		String ret = pl.getName() + "," + pl.getMoney() + "," + pl.getFuel() + "," + pl.getScore();
		//Add a string representation of each train to the return string, still comma delimited
		for(AiSprite p : pl.getAiSprites())
		{
			//A train's data is delimited with "#"
			if(p.getClass().getSimpleName().equals("Train"))
			{
				Train t = (Train)p;
				//Store the type of train
				ret = ret + "," + t.getName();
				if(t.getRoute() != null)
				{
					//If the train has a route, store the route, with a start location, current movement and the waypoint it has reached
					ret = ret + "#" + t.getRoute().getStartLocation().getName() + "#" + t.getCurrent() + "#" + String.valueOf(t.getWaypoint()) + "#" + t.getRouteName();
				}
				else
				{
					//I the train has no route, simply store it's location
					ret = ret + "#" + t.getStation().getName();
				}
			}
		}
		return ret;
	}
	
	//This method is called once the game has been instantiated. It populates the game with trains, and sets the fuel and player turn
	public void loadData(String gameData)
	{
		//Split data into player1, player2 and game, marked in the data by "!"
		String player1Data = gameData.split("!")[0];
		String player2Data = gameData.split("!")[1];
		String coreData = gameData.split("!")[2];
		loadPlayerData(getPlayer1(), player1Data);
		loadPlayerData(getPlayer2(), player2Data);
		//The core data is comma delimited. The first item is the player turn, the second is the fuel reserve of the game. Then fuel price is regenerated
		String[] coreDataArr = coreData.split(",");
		if(coreDataArr[0].equals("0"))
		{
			super.player1Active();
		}
		else
		{
			super.player2Active();
		}
		fuel = Integer.valueOf(coreDataArr[1]);
	}
	
	//This method loads train data into a specific player
	public void loadPlayerData(Player pl, String data)
	{
		//The first 4 items of data have already been used to create the player, whose data is comma delimited
		//The player's possessions are cleared
		pl.clear();
		String[] playerData = data.split(",");
		String[] trainData = new String[playerData.length - 4];
		int i = 4;
		//Take every piece of data past the 4th index and store it in a new array: This is an array of data items for specific trains
		while(i < playerData.length)
		{
			trainData[i - 4] = playerData[i];
			i++;
		}
		for(String train : trainData)
		{
			//A train's data is delimited using the "#" symbol
			String[] specificTrainData = train.split("#");
			for(String d : specificTrainData)
			{
				System.out.println("Train" + d);
			}
			//We have a choice between a train at a station, which can just be generated, and a train on a route, that must be generated, and then positioned along the route
			System.out.println("Creating train at station");
			//If there are only 2 data items for the train, it is at a station, and can be generated The type is stored at 0 index, and start location
			//At 1st index
			Train t = generateTrainAndCarriage(pl, (Station)getStationByName(specificTrainData[1]), getTrainInstanceByName(specificTrainData[0]));
			if(specificTrainData.length > 2)
			{
				//We are working with a train that is along a route, so we must restore it's route. It's start location is at the 1st index
				//And the rest of the route is stored at the 4th index
				Route r = this.restoreRoute(specificTrainData[1] + specificTrainData[4]);
				//The way point is stored at the 3rd index, and the current value is stored at the 2nd index
				int waypoint = Integer.valueOf(specificTrainData[3]);
				float current = Float.valueOf(specificTrainData[2]);
				t.restoreRoute(r, waypoint, current);
			}
		}
	}
	
}

package com.fvs.taxe.routing;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.goals.Event;
import com.fvs.taxe.scenes.DialogueScene;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.worldobjects.Junction;
import com.fvs.taxe.worldobjects.RouteLocation;
import com.fvs.taxe.worldobjects.Station;

public class Train extends AiSprite {
	public enum Type
	{
		STEAM("Steam", new Texture("steam.png"), 1, 25, 20, 0.002f),
		DIESEL("Diesel", new Texture("diesel.png"), 1, 40, 20, 0.001f), 
		ELECTRIC("Electric", new Texture("electric.png"), 1, 160, 10, 0.0005f), 
		NUCLEAR("Nuclear", new Texture("nuclear.png"), 2, 180, 50, 0.0002f), 
		MAG_LEV("Mag", new Texture("Mag.png"), 3, 200, 4, 0.0001f), 
		THE_KING("TheKing", new Texture("TheKing.png"), 4, 350, 2, 0.0002f);

		private String name;
		private Texture train;
		private int weight;
		private int speed;
		private int efficiency;
		private float reliability;
		private Type(String name, Texture trainText, int weightIn, int speedIn, int efficiencyIn, float reliabilityIn)
		{
			this.name = name;
			this.train = trainText;
			this.weight = weightIn;
			this.speed = speedIn;
			this.efficiency = efficiencyIn;
			this.reliability = reliabilityIn;
		}
		public String getName()
		{
			return name;
		}
		public Texture getTrainTexture()
		{
			return train;
		}
		public int getWeight() {
			return weight;
		}
		public int getSpeed()
		{
			return speed;
		}
		public int getEfficiency()
		{
			return efficiency;
		}
		public float getReliability()
		{
			return reliability;
		}

		public static ArrayList<Type> getEnumConstants() {
			ArrayList<Type> types = new ArrayList<Type>();
			types.add(Type.STEAM);
			types.add(Type.DIESEL);
			types.add(Type.ELECTRIC);
			types.add(Type.MAG_LEV);
			types.add(Type.NUCLEAR);
			types.add(Type.THE_KING);
			return types;
		}

	}
	// train class takes a route, follows route by going through paths individually. 
	// TODO if no fuel, alert dialog for out of fuel
	// TODO send event when reaching station 

	private static final double SPEED_UPGRADE = 1.4;
	private static final double RELIABILITY_UPGRADE = 0.8;
	private static final double FUEL_UPGRADE = 0.6;


	protected boolean completed;					// has train completed entire route?
	protected float overshoot;						// amount that the train passes the station by
	protected float previouscurrent = 0;			// the previous current value for the previous turn- used for distance calculation
	protected float overshootDistance;				// amount that the train passes the station by
	Station startLocation;							// The initial location that the trains starts at, when route == null
	private boolean atStation;						// boolean whether train is at station
	private int speed; 							// amount of pixels travelled every turn
	private int fuelEfficiency;						// precise amount of fuel that player loses per turn
	private float reliability;						// probability between 0 and 1 that a train takes the wrong junction
	private boolean fuelUpgrade = false;
	private boolean reliabilityUpgrade = false;
	private boolean speedUpgrade = false;
	private GameScene parentScene;
	private String name;
    private int stationsPassed;

	public Train(GameScene parentScene, Player player, String trainName, Texture text, Station station, int weight, int speed, int fuelEfficiency, float reliability) {
		super(parentScene, text, player, station);
		if (trainName == null || weight <= 0 || speed <= 0|| fuelEfficiency < 0 || reliability <0){
			throw new IllegalArgumentException();
		}
		this.parentScene = parentScene;
		this.weight = weight;
		this.startLocation = station;
		this.atStation = true;
		this.speed = speed;
		this.fuelEfficiency = fuelEfficiency;
		this.reliability = reliability;
		this.name = trainName;
		super.setAIType(AIType.TRAIN);
        stationsPassed = 0;
	}

	@Override
	public void onClickEnd()
	{
		if (parentScene.isRouteSelecting() && parentScene.activePlayer().equals(player) && (this.getStation()!= null) && parentScene.getNewRoute().size() == 0){
			// if the train is clicked on when at a station and in route selection mode, start the route from here
			parentScene.addSelectedTrain(this);
		}
	}

	public int getSpeed(){
		return (int) speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getWeight(){
		return weight;
	}

	public int getWaypoint() {
		return waypoint;
	}

	public String getRouteName(){
		return this.route.getName();
	}

	public Station getStation() {
		// returns current station the train is at, or null if not at one
		if (route == null){
			return startLocation;
		}
		else if (atStation){
			if (waypoint == 0 && current == 0){
				return (Station) route.getStartLocation();
			} else if ((waypoint == 0 && current ==1) || completed){
				return (Station) connection.getTargetLocation();
			} else {
				return (Station) route.getConnection((waypoint-1)).getTargetLocation();
			}
		}
	
		return null;
	}

	public void setUpgrade(int i){
		switch (i){
		case (0):
			speedUpgrade = true;
		break;
		case (1):
			fuelUpgrade = true;
		break;
		case (2):
			reliabilityUpgrade = true;
		break;
		}
	}

	public boolean getUpgrade(int i)
	{
		if((i == 0 && speedUpgrade) || (i == 1 && fuelUpgrade) || (i == 2 && reliabilityUpgrade))
		{
			return true;
		}
		return false;
	}

	public void atStation(){
		/// method called when train is at a station
		Event stationEvent = new Event(this, "Arrival", getStation().getName());
		parentScene.events.pushEvent(stationEvent);
	}

	public void updatePosition() {
		if (route == null){
			return;
		}
		float totalReliability = (float) ((reliabilityUpgrade) ? reliability*RELIABILITY_UPGRADE : reliability);
		if (MathUtils.randomBoolean(totalReliability)) {
			DialogueScene dialogueScene = new DialogueScene("Broken down train!");
			dialogueScene.setText("Broken down train!");
			Game.pushScene(dialogueScene);
			return;
		}

		atStation = false;
		int totalSpeed = (int) ((speedUpgrade) ? speed*SPEED_UPGRADE : speed);
		if ((pathDistance + totalSpeed) >= path.getFinalDistance()){
			// if going to get to waypoint or beyond, set it to next waypoint
			float distanceToStation = (path.getFinalDistance() - pathDistance);
			overshootDistance = (pathDistance+totalSpeed)- path.getFinalDistance();					
			if (waypoint+2 == route.numLocations()){
				// if at final waypoint, fix to that waypoint
				completed = true;
				current = 1;
				pathDistance += distanceToStation;
				routeDistance += distanceToStation;
				atStation = true;
				atStation();
                setRoute(null); //reset route
			} else {
				// if at intermediate waypoint
				if (connection.getTargetLocation().getClass() == Junction.class){
					// if a junction, do not stop at junction
                    Junction junction = (Junction) connection.getTargetLocation();
                    if (junction.hasObstacle()) trainCrashed(junction);

					waypoint++;
					connection = route.getConnection(waypoint);	// get next connection in route
					path = connection.getPath();			// get next route in route
					current = path.getTFromDistance(overshootDistance);
					routeDistance += totalSpeed;
					pathDistance = overshootDistance;
                    stationsPassed++;
				} else {
					// if the routeLocation currently at is a station, fix to station for that turn
					waypoint++; 								// move to next waypoint
					connection = route.getConnection(waypoint);	// get next connection in route
					path = connection.getPath();			// get next route in route
					current = 0;
					pathDistance = 0;
					routeDistance += distanceToStation;
					atStation = true;
					atStation();
                    stationsPassed++;
				}
			}
		} else {
			// if not at any kind of waypoint
			current = path.getTFromDistance(pathDistance+totalSpeed); 
			pathDistance += totalSpeed;
			routeDistance += totalSpeed;
		}
		// calculate fuel to take off of player
		double totalFuelEfficiency = (fuelUpgrade) ? (fuelEfficiency*FUEL_UPGRADE) : fuelEfficiency;
		player.setFuel((int) (player.getFuel()- totalFuelEfficiency)); 
		move();
	}

    public void trainCrashed(Junction junction) {
        player.trainCrashed = true;
        parentScene.obstacles.remove(junction.getObstacle());
        parentScene.Remove(junction.getObstacle());
        parentScene.Remove(this);
        junction.setObstacle(null);
        player.aiSprites.remove(this);
    }

    public void checkObstacles() {
        //Check if obstacles on the route
        if (route == null) return;

        List<RouteLocation> routeLocations = route.getRouteLocations();
        if (routeLocations == null) return;

        routeLocations = routeLocations.subList(stationsPassed + 1, routeLocations.size());
        for (RouteLocation routeLocation : routeLocations) {
            if (routeLocation instanceof Junction) {
                if (((Junction) routeLocation).hasObstacle()) {
                    DialogueScene dialogueScene = new DialogueScene("Obstacle warning for " + this.getName() + " train!");
                    Game.pushScene(dialogueScene);
                    break;
                }
            }
        }
    }

	@Override
	public void updateTurn() {
		if (hasStopped){
			hasStopped = false;
			return;
		}
		if (route != null && !completed) {
			// if the train is on a route
			if (player.getFuel()-fuelEfficiency > 0 ){
				// if theres enough fuel
				updatePosition();
			} else {
				// TODO if theres not enough fuel, send a dialog
				String noFuelText = "You need fuel to move!";
				DialogueScene scene = new DialogueScene(noFuelText);
				scene.setText(noFuelText);
				Game.pushScene(scene);
			}
		}
	}

	public void setRoute(Route route) {
		// sets the route, iff the route begins from trains current station
		if (route == null){
			return;
		}
		if (route.getStartLocation() == getStation()){
			// only set route if the route starts from where the train currently is
			this.route = route;
			waypoint = 0;
			current = 0;
			out = new Vector2(1,1);
			connection= route.getConnection(waypoint);
			path = connection.getPath();
			routeDistance = 0;
			pathDistance =0;
			completed = false;
		} else {
			// shouldnt occur in normal route selection, for debugging only
			System.out.println("Invalid route, must start from trains current station");
        }
	}

	public Route getRoute()
	{
		if(route != null)
		{
			return route;
		}
		else
		{
			return null;
		}
	}

	public void restoreRoute(Route newRoute, int newWaypoint, float newCurrent){
		// put a train partially on a route
		if (newRoute == null){
			return;
		} if (newWaypoint < - 1 || newCurrent > 1 || newCurrent < 0){
			throw new IllegalArgumentException("current must be 0 <= c <= 1 , waypoint must be > -1");
		} else if (newWaypoint+2 > newRoute.getSize() ){
			throw new IllegalArgumentException("waypoint+2 cannot exceed route size");
		}
		System.out.println("Attempting Restore");
		this.route = newRoute;
		this.waypoint = newWaypoint;
		this.current = newCurrent;
		out = new Vector2(1,1);
		connection= newRoute.getConnection(newWaypoint);
		path = connection.getPath();
		completed = false; 

		// set the path/route distances correctly
		pathDistance = path.getDistanceFromT(newCurrent);
		routeDistance = pathDistance;
		for (int i = 0; i < newWaypoint; i++){
			routeDistance += newRoute.getConnection(i).getPath().getFinalDistance();
		}
		move();
		if (newCurrent == 0 || newCurrent ==1){
			atStation = true;
		} else {
			atStation = false;
		}
		
	}

	@Override 
	public String toString(){
		return this.name;
	}

	public String getName() {
		return name;
	}
}


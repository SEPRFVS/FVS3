package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.scenes.GameScene;
import com.turkishdelight.taxe.worldobjects.Junction;
import com.turkishdelight.taxe.worldobjects.Station;

public class Train extends AiSprite {
	// train class takes a route, follows route by going through paths individually. 
	
	// TODO if no fuel, alert dialog for out of fuel
	// TODO send event when reaching station 
	
	private static final double SPEED_UPGRADE = 1.4;
	private static final double RELIABILITY_UPGRADE = 0.8;
	private static final double FUEL_UPGRADE = 0.6;
	
	Carriage carriage;								// carriage train is currently connected to - CANNOT BE NULL
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
	
	public Train(GameScene parentScene, Player player, Texture text, Station station, int weight, int speed, int fuelEfficiency, float reliability) {
		super(parentScene, text, player, station);
		this.parentScene = parentScene;
		this.weight = weight;
		this.startLocation = station;
		this.atStation = true;
		this.speed = speed;
		this.fuelEfficiency = fuelEfficiency;
		this.reliability = reliability;
	}
	
	@Override
	public void onClickEnd()
	{
		if (parentScene.isRouteSelecting() && parentScene.activePlayer().equals(player) && (this.getStation()!= null) && parentScene.getNewRoute().size() == 0){
			// if the train is clicked on when at a station and in route selection mode, start the route from here
			System.out.println("train selected");
			parentScene.addSelectedTrain(this);
		}
	}

	public void setCarriage(Carriage carriage){
		this.carriage = carriage;
	}

	public Carriage getCarriage(){
		return this.carriage;
	}

	public int getSpeed(){
		return (int) speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getWeight(){
		return weight + carriage.getWeight();
	}
	
	public int getWaypoint() {
		return waypoint;
	}
	
	public String getRouteName(){
		return this.route.getName();
	}
	
	public Station getStation() {
		// returns current station the train is at, or null if not at one
		// TODO needs checking.
		if (route == null){
			return startLocation;
		}
		else if (atStation){
			if (waypoint == 0){
				return (Station) route.getStartLocation();
			}
			return (Station) route.getConnection((waypoint)-1).getTargetLocation();
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
	
	public void atStation(){
		/// method called when train is at a station
		System.out.println("train at station");
	}
	
	protected void updatePosition() {
		float totalReliability = (float) ((reliabilityUpgrade) ? reliability*RELIABILITY_UPGRADE : reliability);
		if (MathUtils.randomBoolean(totalReliability)) {
			// TODO send a dialog here
			System.out.println("Train broken down for turn due to reliability");
			return;
		}
		atStation = false;
		int totalSpeed = (int) ((speedUpgrade) ? speed*SPEED_UPGRADE : speed);
		if ((pathDistance + totalSpeed) >= path.getFinalDistance()){
			// if going to get to waypoint or beyond, set it to next waypoint
			float distanceToStation = (path.getFinalDistance() - pathDistance);
			overshootDistance = (pathDistance+totalSpeed)- path.getFinalDistance();						// currently unused
			if (waypoint+2 == route.numLocations()){
				// if at final waypoint, fix to that waypoint
				System.out.println("Final waypoint reached");
				completed = true;
				current = 1;
				waypoint++;
				pathDistance += distanceToStation;
				routeDistance += distanceToStation;
				atStation = true;
				atStation();
			} else {
				// if at intermediate waypoint
				System.out.println("Waypoint reached");
				if (connection.getTargetLocation().getClass() == Junction.class){
					// if a junction, <TODO test whether route changes to a different route based on reliability>, do not stop at junction
					waypoint++;
					connection = route.getConnection(waypoint);	// get next connection in route
					path = connection.getPath();			// get next route in route
					current = path.getTFromDistance(overshootDistance);
					routeDistance += totalSpeed;
					pathDistance = overshootDistance;
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
				}
			}
		} else {
			current = path.getTFromDistance(pathDistance+totalSpeed); 
			pathDistance += totalSpeed;
			routeDistance += totalSpeed;
		}
		// calculate fuel to take off of player
		double totalFuelEfficiency = (fuelUpgrade) ? (fuelEfficiency*FUEL_UPGRADE) : fuelEfficiency;
		player.setFuel((int) (player.getFuel()- totalFuelEfficiency)); 
		move();
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
				System.out.println("Not enough fuel!");
			}
		}
	}
	
	public void setRoute(Route route) {
		// sets the route, iff the route begins from trains current station
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
			carriage.setRoute(route);
		} else {
			// shouldnt occur in normal route selection, for debugging only
			System.out.println("Invalid route, must start from trains current station");
		}
	}
	
	public void restoreRoute(Route route, int waypoint, float current){
		// put a train and carriagepartially on a route
		this.route = route;
		this.waypoint = waypoint;
		this.current = current;
		out = new Vector2(1,1);
		connection= route.getConnection(waypoint);
		path = connection.getPath();
		completed = false; 
		
		// set the path/route distances correctly
		pathDistance = path.getDistanceFromT(current);
		routeDistance = pathDistance;
		for (int i = 0; i < waypoint; i++){
			routeDistance += route.getConnection(i).getPath().getFinalDistance();
		}
		move();
		carriage.restoreRoute(route, waypoint, current);
	}
}

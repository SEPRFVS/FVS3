package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.scenes.GameScene;
import com.turkishdelight.taxe.worldobjects.Location;

public class Train extends AiSprite {
	// train class takes a route, follows route by going through paths individually. 
	
	Carriage carriage;								// carriage train is currently connected to - CANNOT BE NULL
	protected boolean completed;					// has train completed entire route?
	protected float overshoot;						// amount that the train passes the station by
	Location startLocation;							// The initial location that the trains starts at, when route == null
	private boolean atStation;
	
	
	public Train(GameScene parentScene, Texture text, Location location, int weight) {
		super(parentScene, text, location);
		this.weight = weight;
		startLocation = location;
		atStation = true;
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
	
	public Location getStation() {
		// returns current station the train is at, or null if not at one
		// TODO needs checking.
		if (route == null){
			return startLocation;
		}
		else if (atStation){
			if (waypoint == 0){
				return route.getStartLocation();
			}
			return route.getConnection((waypoint)-1).getLocation();
		}
		
		return null;
	}

	
	protected void updatePosition() {
		atStation = false;
		//System.out.println((pathDistance + speed) + " " + curvedPath.getFinalDistance());
		if ((pathDistance + speed) >= curvedPath.getFinalDistance()){
			// if going to get to waypoint or beyond, set it to next waypoint
			float distanceToStation = (curvedPath.getFinalDistance() - pathDistance);
			overshoot = pathDistance+(speed) - distanceToStation;						// currently unused
			routeDistance += distanceToStation;
			if (waypoint+2 == route.numLocations()){
				// if at final waypoint, fix to that waypoint
				System.out.println("Final waypoint reached");
				completed = true;
				current = 1;
				waypoint++;
				pathDistance += distanceToStation;
			} else {
				// if at intermediate waypoint
				System.out.println("Waypoint reached");
				waypoint++; 								// move to next waypoint
				connection = route.getConnection(waypoint);	// get next connection in route
				curvedPath = connection.getPath();			// get next route in route
				current = 0;
				pathDistance = 0;
			}
			atStation = true;
		} else {
			current = curvedPath.getTFromDistance(pathDistance+speed); 
			pathDistance += speed;
			routeDistance += speed;
			//System.out.println("Distance travelled = " + routeDistance);
		}
		move();
	}

	@Override
	public void updateTurn() {
		if (stopped){
			stopped = false;
			return;
		}
		if (route != null && !completed) {
			updatePosition();
		}
	}
	
	public void setPath(Route route) {
		// ASSUMES GIVEN PATH FROM STATION ALREADY AT
		System.out.print("path set");
		this.route = route;
		waypoint = 0;
		current = 0;
		out = new Vector2(1,1);
		connection= route.getConnection(waypoint);
		curvedPath = connection.getPath();
		routeDistance = 0;
		pathDistance =0;
		completed = false;
		carriage.setPath(route);
	}
}

package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Train extends AiSprite {
	public enum Type
	{
		STEAM, DIESEL, ELECTRIC, NUCLEAR, MAG_LEV, THE_KING
	}
	// train class takes a route, follows route by going through paths individually. 
	
	Carriage carriage;								// carriage train is currently connected to - CANNOT BE NULL
	protected boolean completed;					// has train completed entire route?
	protected float overshoot;						// amount that the train passes the station by
	protected float previouscurrent = 0;			// the previous current value for the previous turn- used for distance calculation
	boolean speedBoost = false;
	boolean reliabilityBoost = false;
	boolean efficiencyBoost = false;
	public Train(Scene parentScene, Texture text, Route route, int weight) {
		super(parentScene, text, route);
		this.weight = weight;
	}

	public void addCarriage(Carriage carriage){
		this.carriage = carriage;
	}

	public Carriage getCarriage(){
		return this.carriage;
	}

	public int getSpeed(){
		float speedmod = 1;
		if(speedBoost)
		{
			speedmod = 1.3f;
		}
		return (int) (super.getSpeed() * speedmod);
	}
	
	public int getWaypoint() {
		return waypoint;
	}

	public void setPath(Route route) {
		// ASSUMES GIVEN PATH FROM STATION ALREADY AT
		this.route = route;
		waypoint = 0;
		completed = false;
		current = 0;
		out = new Vector2(1,1);
	}
	
	protected void updatePosition() {
		// TODO overshoot shouldnt use current, if going small route ->  long, a big jump occurs - use route distance somehow?
		// add on, then clear any overshoot if the train stopped at the station previous turn 
		if (overshoot != 0) {
			current += overshoot;
			overshoot = 0;
		}
		
		// for constant velocity, ignoring effect of curves
		Vector2 prevout = new Vector2();
		curvedPath.derivativeAt(prevout, current);

		//curvedPath.derivativeAt(out, current);
		current += getSpeed()*30/prevout.len();    // 30 is just an arbitrary number, works well- increase to increase distance travelled each turn

		// for variable velocity (curves affect movement)
		//current += speed;

		// for testing with animation only
		//current += Gdx.graphics.getDeltaTime() * speed / out.len() * 100;

		if(current >= 0.95) {// if a waypoint is reached (0.95 means no tiny movements!)
			System.out.println("Waypoint reached");

			if(waypoint+2 >= route.size()) { // if reached final waypoint, fix it to final waypoint
				System.out.println("Final waypoint reached");
				completed = true;
				current = 1;
				current=1;
			} else {// otherwise fix it to station, give it overshoot next turn
				distance += (curvedPath.getDistanceFromT(1) - curvedPath.getDistanceFromT(previouscurrent));
				overshoot = current-1;
				waypoint++; 								// move to next waypoint
				connection = route.getConnection(waypoint);	// get next connection in route
				curvedPath = connection.getPath();			// get next route in route
				current = 0;
				previouscurrent = 0;						// reset variables
			}
		}
		distance+= (curvedPath.getDistanceFromT(current) - curvedPath.getDistanceFromT(previouscurrent));
		previouscurrent = current;

		System.out.println("Distance travelled = " + distance);
		move();
	}

	@Override
	public void updateTurn() {
		if (stopped){
			stopped = false;
			return;
		}
		if (!completed ) {
			updatePosition(); 
		}

	}

}

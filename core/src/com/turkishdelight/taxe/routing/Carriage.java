package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Carriage extends AiSprite {
	// carriage moves 50 pxs behind the train it is connected to.
	private Train train;							// train that the carriage is connected to
	private int numCarriages = 1; 					// counts of number of carriages currently carrying ( >= 0)
	private boolean nextRoute;						// boolean to test whether train is on next route 
	private float prevDistances = 0f;				// the distance of the routes that the train has completed

	public Carriage(Scene parentScene, Texture text, Route route, Train train) {
		super(parentScene, text, route);
		this.weight = 1;
		this.train = train;			
	}

	@Override
	public void updateTurn() {
		updatePosition();
	}
	
	@Override
	protected void updatePosition() {
		// use carriages current value to calculate this current, do other fancy stuff based around distance..
		if (train.getDistance() >= 50){
			if (nextRoute){
				waypoint++; // move to next waypoint
				connection = route.getConnection(waypoint);	// get next connection in route
				curvedPath = connection.getPath();					// get next route in route
				current = 0;
				nextRoute = false;
			}
			if (train.getCurrent() != 0){ // if train isnt at start of a waypoint
				current = curvedPath.getTFromDistance(train.getDistance()-prevDistances-50f);	// carriage is always 50 pixels behind train
			} else if (train.getCurrent() == 0 && train.getWaypoint() >= 1) { // if at an intermediate waypoint/ final waypoint
				current = curvedPath.getTFromDistance(curvedPath.getFinalDistance() - 50f); // work out 50 pixels behind waypoint (on previous route)
				prevDistances += curvedPath.getFinalDistance();
				nextRoute = true;
			}
			move();
		}
	}
	
	public void addCarriage(){
		numCarriages+=1;
	}
	
	public void removeCarriage() {
		if (numCarriages > 0) {
			numCarriages-=1;
		}
	}
	
	public int getNumCarriages() {
		return numCarriages;
	}

	@Override
	public void setPath(Route route) {
		this.route = route;
		waypoint = 0;
		current = 0;
		out = new Vector2(1,1);
	}

	

}

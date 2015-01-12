package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.worldobjects.Location;

public class Carriage extends AiSprite {
	private static final int CARRIAGE_WEIGHT = 1;
	// carriage moves 50 pxs behind the train it is connected to.
	private Train train;							// train that the carriage is connected to
	private int carriageCount = 3; 					// counts of number of carriages currently carrying ( >= 0)
	private boolean nextRoute;						// boolean to test whether train is on next route 
	private float prevDistances = 0f;				// the distance of the routes that the train has completed
	private Scene parentScene;
	Label carriageCountLabel;
	
	public Carriage(Scene parentScene, Texture text, Location location, Train train) {
		super(parentScene, text, location);
		this.parentScene = parentScene;
		this.weight = 1;
		this.train = train;	
		Texture player1LabelText = new Texture("Clear_Button.png");
		carriageCountLabel = new Label(parentScene, player1LabelText, Label.genericFont(Color.MAROON, 20));
	}
	
	@Override
	public void updateTurn() {
		if (route != null) {
			updatePosition(); 
		}
	}
	
	@Override
	protected void updatePosition() {
		// use carriages current value to calculate this current, do other fancy stuff based around distance..
		if (train.getDistance() >= 50){
			if (nextRoute){
				prevDistances += curvedPath.getFinalDistance(); // todo check this isnt bbreaking anything
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
				nextRoute = true;
			}
			move();
		}
		
		Vector2 point = curvedPath.getPointFromT(curvedPath.getTFromDistance(train.getDistance()-prevDistances-60f));
		
		carriageCountLabel.setText(Integer.toString(getCarriageCount()));
		carriageCountLabel.setPosition(point.x, point.y);
		
		carriageCountLabel.setRotation(getRotation());
		carriageCountLabel.setSize(0, 0);
		parentScene.Add(carriageCountLabel);
	}
	
	@Override
	public void setPath(Route route) {
		this.route = route;
		waypoint = 0;
		current = 0;
		out = new Vector2(1,1);
		connection = route.getConnection(waypoint);
		curvedPath = connection.getPath();
		distance = 0;
		prevDistances = 0f;
		nextRoute = false;
	}
	
	public int getCarriageCount() {
		return carriageCount;
	}
	public void increaseCarriageCount(){
		carriageCount+=1;
	}
	
	public void decreaseCarriageCount() {
		if (carriageCount > 0) {
			carriageCount-=1;
		}
	}
	
	public int getWeight(){
		return carriageCount * CARRIAGE_WEIGHT;
	}
	
}

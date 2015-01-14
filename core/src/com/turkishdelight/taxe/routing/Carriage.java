package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.worldobjects.Station;

public class Carriage extends AiSprite {
	private static final int CARRIAGE_WEIGHT = 1;
	// carriage moves 50 pxs behind the train it is connected to.
	private Train train;							// train that the carriage is connected to
	private int carriageCount = 3; 					// counts of number of carriages currently carrying ( >= 0)
	private float prevDistances = 0f;				// the distance of the routes that the carriage has completed only
	Label carriageCountLabel;
	private boolean completed;
	
	public Carriage(Scene parentScene, Texture text, Station station, Train train) {
		super(parentScene, text, station);
		this.weight = 1;
		this.train = train;	
		Texture player1LabelText = new Texture("Clear_Button.png");
		carriageCountLabel = new Label(parentScene, player1LabelText, Label.genericFont(Color.MAROON, 20), Game.guiZ);
		parentScene.Add(carriageCountLabel);
		carriageCountLabel.setText(Integer.toString(getCarriageCount()));
	}
	
	@Override
	public void updateTurn() {
		if (route != null && !completed) {
			updatePosition(); 
		}
	}
	
	@Override
	protected void updatePosition() {
		// moves exactly 50 behind the train it is connected to
		if (train.getRouteDistance() >= 50) {
			float nextDistance = train.getRouteDistance()- prevDistances- 50f;
			if ((nextDistance >= curvedPath.getFinalDistance())){
				// if next move will go past waypoint
				prevDistances+= curvedPath.getFinalDistance();
				if (waypoint+2 >= route.numLocations()){
					// if at final waypoint, fix to final waypoint
					System.out.println("Carriage final waypoint reached");
					current = curvedPath.getTFromDistance(curvedPath.getFinalDistance()-50f);
					completed = true;
				} else {
					// if carriage at intermediate waypoint, move to next path and calculate overshootDistance into next route
					System.out.println("Carriage reached waypoint");
					float overshootDistance = nextDistance- curvedPath.getFinalDistance();
					waypoint++;
					connection = route.getConnection(waypoint);
					curvedPath = connection.getPath();
					current = curvedPath.getTFromDistance(overshootDistance);
				}
			}  else {
				// if nothing special, just set to nextDistance
				System.out.println("Normal mode");
				current = curvedPath.getTFromDistance(nextDistance);
			}
			move();	
			}
	
		// setup carriage label
		Vector2 labelVector = curvedPath.getPointFromT(curvedPath.getTFromDistance(train.getRouteDistance()-prevDistances-60f));
		carriageCountLabel.setText(Integer.toString(getCarriageCount()));
		carriageCountLabel.setPosition(labelVector.x, labelVector.y);
		carriageCountLabel.setRotation(getRotation());
	}
	
	@Override
	public void setPath(Route route) {
		this.route = route;
		waypoint = 0;
		current = 0;
		out = new Vector2(1,1);
		connection = route.getConnection(waypoint);
		curvedPath = connection.getPath();
		routeDistance = 0;
		prevDistances = 0f;
		completed = false;
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

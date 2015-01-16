package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.worldobjects.Station;

public class Carriage extends AiSprite {
	private static final int CARRIAGE_WEIGHT = 1;
	// carriage moves SPRITEWIDTH behind the train it is connected to.
	private Train train;							// train that the carriage is connected to
	private int carriageCount = 3; 					// counts of number of carriages currently carrying ( >= 0)
	private float prevDistances = 0f;				// the distance of the routes that the carriage has completed only
	Label carriageCountLabel;
	
	public Carriage(Scene parentScene, Texture text, Player player, Station station, Train train) {
		super(parentScene, text, player, station);
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
		// moves exactly SPRITEWIDTH behind the train it is connected to to look connected 
		if (train.getRouteDistance() >= 50) {
			float nextDistance = train.getRouteDistance()- prevDistances- SPRITEWIDTH;
			if ((nextDistance >= path.getFinalDistance())){
				// if next move will go past waypoint
				prevDistances+= path.getFinalDistance();
				if (waypoint+2 >= route.numLocations()){
					// if at final waypoint, fix to final waypoint
					System.out.println("Carriage final waypoint reached");
					current = path.getTFromDistance(path.getFinalDistance()-SPRITEWIDTH);
					completed = true;
				} else {
					// if carriage at intermediate waypoint, move to next path and calculate overshootDistance into next route
					System.out.println("Carriage reached waypoint");
					float overshootDistance = nextDistance- path.getFinalDistance();
					waypoint++;
					connection = route.getConnection(waypoint);
					path = connection.getPath();
					current = path.getTFromDistance(overshootDistance);
				}
			}  else {
				// if nothing special, just set to nextDistance
				current = path.getTFromDistance(nextDistance);
			}
			move();	
			}
	
		// setup carriage label
		Vector2 labelVector = path.getPointFromT(path.getTFromDistance(train.getRouteDistance()-prevDistances-SPRITEWIDTH));
		carriageCountLabel.setText(Integer.toString(getCarriageCount()));
		carriageCountLabel.setPosition(labelVector.x, labelVector.y);
		carriageCountLabel.setRotation(getRotation());
	}
	
	public void setLabelAlpha(float f){
		carriageCountLabel.setAlpha(f);
	}
	
	public Train getTrain(){
		return this.train;
	}
	
	public int getCarriageCount() {
		return carriageCount;
	}
	
	public void increaseCarriageCount(){
		carriageCount+=1;
		carriageCountLabel.setText(Integer.toString(getCarriageCount()));
	}
	
	public void decreaseCarriageCount() {
		if (carriageCount > 0) {
			carriageCount-=1;
			carriageCountLabel.setText(Integer.toString(getCarriageCount()));
		}
	}
	
	public int getWeight(){
		return carriageCount * CARRIAGE_WEIGHT;
	}

	public void setRoute(Route route) {
		this.route = route;
		waypoint = 0;
		current = 0;
		out = new Vector2(1,1);
		connection = route.getConnection(waypoint);
		path = connection.getPath();
		routeDistance = 0;
		prevDistances = 0f;
		completed = false;
	}
	
	public void restoreRoute(Route route, int trainWaypoint, float current) {
		this.route = route;
		if (train.pathDistance < 50 && train.routeDistance > 50){
			// check if the carriage would be on the previous path of the route
			this.waypoint = trainWaypoint-1;
		} else {
			this.waypoint = trainWaypoint;
		}
		
		out = new Vector2(1,1);
		connection= route.getConnection(waypoint);
		path = connection.getPath();
		completed = false;
		// calculate prevDistances by calculating sum of the completed paths
		prevDistances = 0;
		for (int i = 0; i < waypoint; i++){
			prevDistances += route.getConnection(i).getPath().getFinalDistance();
		}
		
		if (train.routeDistance > 50){
			// if the train has passed the threshold for a carriage to follow it
			current = path.getTFromDistance(train.getRouteDistance() - prevDistances - 50f);
		} else {
			current = 0;
		}
		updatePosition();
	}
}

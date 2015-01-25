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
		if (train == null){
			throw new IllegalArgumentException("Train cannot be null");
		}
		this.weight = 1;
		this.train = train;	
		this.setAIType(AIType.CARRIAGE);
		Texture carriageLabelText = new Texture("Clear_Button.png");
		carriageCountLabel = new Label(parentScene, carriageLabelText, Label.genericFont(Color.MAROON, 20), Game.guiZ);
		parentScene.Add(carriageCountLabel);
		carriageCountLabel.setText(Integer.toString(getCarriageCount()));
	}
	
	@Override
	public void updateTurn() {
		if (route != null && !train.hasCompleted()) {
			updatePosition(); 
		}
	}
	
	@Override
	public void updatePosition() {
		// moves exactly SPRITEWIDTH behind the train it is connected to to look connected 
		if (route == null){
			return;
		}
		if (train.getRouteDistance() >= 50) {
			float nextDistance = train.getRouteDistance()- prevDistances- SPRITEWIDTH;
			if ((nextDistance >= path.getFinalDistance())){
				// if next move will go past waypoint
				prevDistances+= path.getFinalDistance();
				// if carriage at intermediate waypoint, move to next path and calculate overshootDistance into next route
				float overshootDistance = nextDistance- path.getFinalDistance();
				waypoint++;
				connection = route.getConnection(waypoint);
				path = connection.getPath();
				current = path.getTFromDistance(overshootDistance);
				pathDistance = overshootDistance;
				routeDistance += overshootDistance;
			}
			else if (train.hasCompleted()){
				// if at final waypoint, fix to final waypoint
				current = path.getTFromDistance(path.getFinalDistance()-SPRITEWIDTH);
				pathDistance = path.getFinalDistance()-SPRITEWIDTH;
				routeDistance = prevDistances + pathDistance;
				completed = true;
			} else {
				// if nothing special, just set to nextDistance
				current = path.getTFromDistance(nextDistance);
				pathDistance = nextDistance;
				routeDistance = prevDistances + nextDistance;
			}
			move();	
			setLabel();
			}
	
		
	}

	public void setLabel() {
		// setup carriage label
		if (route == null){
			return;
		}
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

	public void setRoute() {
		Route route = train.getRoute();
		if (route == null){
			this.route = null;
			return;
		}
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
	
	public void restoreRoute() {
		// called from train, restores the carriage to the right distance behind the train
		Route newRoute = train.getRoute();
		int trainWaypoint = train.getWaypoint();
		float trainCurrent = train.getCurrent();
		
		if (newRoute == null){
			this.route = newRoute;
			return;
		} else if (newRoute.numLocations() <= waypoint || trainCurrent > 1f){
			this.route = null;
			return;
		}
		
		this.route = newRoute;
		if (train.pathDistance < 50 && train.routeDistance > 50){
			// check if the carriage would be on the previous path of the route
			if (waypoint > 0){
				this.waypoint = trainWaypoint-1;
			} else {
				this.waypoint = 0;
			}
		} else {
			this.waypoint = trainWaypoint;
		}
		
		out = new Vector2(1,1);
		connection= newRoute.getConnection(waypoint);
		path = connection.getPath();
		completed = false;
		// calculate prevDistances by calculating sum of the completed paths
		prevDistances = 0;
		for (int i = 0; i < waypoint; i++){
			prevDistances += newRoute.getConnection(i).getPath().getFinalDistance();
		}
		
		pathDistance = train.getRouteDistance() - prevDistances - 50f;
		routeDistance = prevDistances + pathDistance;
		if (train.getRouteDistance() > 50){
			// if the train has passed the threshold for a carriage to follow it
			this.current = path.getTFromDistance(pathDistance);
		} else {
			this.current = 0;
		}
		
		move();
		setLabel();
	}

	public Label getLabel() {
		return carriageCountLabel;
	}
}

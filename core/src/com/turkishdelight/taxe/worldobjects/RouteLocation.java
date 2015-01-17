package com.turkishdelight.taxe.worldobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.routing.Connection;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.scenes.GameScene;

public abstract class RouteLocation extends Clickable {

	protected GameScene parentScene;
	protected String name;
	protected Vector2 coords;
	protected ArrayList<Connection> connections = new ArrayList<Connection>();  	// connected locations
	protected int numConnections = 0;												// number of connected locations
	protected Boolean selectingRoute = false;
	
	// abstract class for anything that can lie on a route and have multiple routes converging to it
	public RouteLocation(GameScene parentScene, Texture text, String name,  int x, int y) {
		super(parentScene, text, Game.locationZ);
		this.parentScene = parentScene;
		this.name = name;
		coords = new Vector2(x, y);
		setPosition(x, y);
	}
	
	public void addConnection(RouteLocation routeLocation, CurvedPath path) {
		this.connections.add(new Connection(routeLocation, path));
		numConnections++;
	}
	
	public int numConnections() {
		return numConnections;
	}

	public boolean isConnected(RouteLocation routeLocation) {
		connections = getConnections();
		for (Connection connection : connections) {
			if (connection.getTargetLocation().equals(routeLocation)){ 
				return true;
			}
		}
		return false;
	}
	
	public CurvedPath getCurvedRoute(RouteLocation routeLocation) {
		// gets the route connecting this to location
		// currently assumes location is connected
		connections = getConnections();
		for (Connection connection : connections) {
			if (connection.getTargetLocation().equals(routeLocation)){ 
				return connection.getPath();
			}
		}
		return null;
	}
	
	public Vector2 getCoords() {
		return this.coords;
	}

	public ArrayList<Connection> getConnections () {
		return this.connections;
	}

	public String getName() {
		return this.name;
	}
	public void setSelectingRoute(Boolean selectingRoute) {
		this.selectingRoute = selectingRoute;
	}
	
	@Override
	public void onClickEnd(){
		if (selectingRoute && parentScene.getSelectedTrain() != null){
			parentScene.selectLocation(this);
		}
	}
}

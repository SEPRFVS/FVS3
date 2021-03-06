package com.fvs.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Clickable;
import com.fvs.taxe.Game;
import com.fvs.taxe.routing.Connection;
import com.fvs.taxe.routing.CurvedPath;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.worldobjects.obstacles.Obstacle;

import java.util.ArrayList;

public abstract class RouteLocation extends Clickable {

	protected GameScene parentScene;
	protected String name;
	protected Vector2 coords;
	protected ArrayList<Connection> connections = new ArrayList<Connection>();  	// connected locations
	protected int numConnections = 0;												// number of connected locations
	protected Boolean selectingRoute = false;
    protected Obstacle obstacle;
	
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
	
	public CurvedPath getCurvedPath(RouteLocation routeLocation) {
		// gets the route connecting this to location
		// currently assumes location is connected
		if (isConnected(routeLocation)){
			connections = getConnections();
			for (Connection connection : connections) {
				if (connection.getTargetLocation().equals(routeLocation)){ 
					return connection.getPath();
				}
			}
		}
		return null;
	}
	
	public Vector2 getPosition() {
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
	
	public Boolean getSelectingRoute(){
		return this.selectingRoute;
	}

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public boolean hasObstacle() {
        return obstacle != null;
    }
	
	@Override
	public void onClickEnd(){
		if (selectingRoute && parentScene.getSelectedTrain() != null){
			parentScene.selectLocation(this);
		}
	}
	
	@Override
	public boolean equals(Object object){
		if (object.getClass() == RouteLocation.class) {
			if (((RouteLocation) object).getName().equals(this.getName()) && ((RouteLocation) object).getPosition().equals(this.getPosition())){
				return true;
			}
		}
		return false;
	}
}

package com.turkishdelight.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.turkishdelight.taxe.routing.Connection;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Location extends SpriteComponent {

	// Class that is designed to hold information on locations- position and connections- connected locations and path to other locations 
	static Texture text = new Texture("location.png");
	private Vector2 coords;	      						  				// vector location of location
	private Array<Connection> connections = new Array<Connection>();  	// connected locations
	private int numConnections = 0;										// number of connected locations
	
	public Location(Scene parentScene, int x, int y) {
		super(parentScene, text, Game.locationZ);
		this.setSize(10, 10);
		coords = new Vector2(x, y);
	}
	
	public void addConnection(Location location, CatmullRomSpline<Vector2> path) {
		this.connections.add(new Connection(location, path));
		numConnections++;
	}
	
	public int numConnections() {
		return numConnections;
	}

	public boolean isConnected(Location location) {
		connections = getConnections();
		for (Connection connection : connections) {
			if (connection.getLocation().equals(location)){ // TODO need to check .equals works there!
				return true;
			}
		}
		return false;
	}
	
	public CatmullRomSpline<Vector2> getPath(Location location) {
		// gets the path connecting this to location
		// currently assumes location is connected
		connections = getConnections();
		for (Connection connection : connections) {
			if (connection.getLocation().equals(location)){ // TODO need to check .equals works there!
				return connection.getPath();
			}
		}
		return null;
	}
	
	public Vector2 getCoords() {
		return this.coords;
	}

	public Array<Connection> getConnections () {
		return this.connections;
	}

}
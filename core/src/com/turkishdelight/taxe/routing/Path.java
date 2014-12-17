package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.turkishdelight.taxe.worldobjects.Location;

public class Path {
	// Contains information on path from location to another, possibly through multiple locations 
	private Array<Connection> connections;			// collection of connections
	private Location startLocation;					// starting location of path (is connected to first location in connections)
	private int size = 0;

	public Path() {
		this.connections = new Array<Connection>();
	}

	public Path(Location ... locations) { // note locations cannot be null
		this.connections = new Array<Connection>();
		Location previous = null;
		// add start location first, then sort out connections.
		for (Location location : locations){
			if (size == 0) {
				this.startLocation = location;
			} else {
				if (previous.isConnected(location)){
					this.connections.add(new Connection(location, previous.getCurvedRoute(location)));
				} else {
					System.out.println("NOT A VALID ROUTE"); // TODO be stricter here
				}
			}
			previous = location;	
			size++;
		}
	}
	
	public int size(){
		return size;
	}
	
	public Connection getConnection(int i){
		return connections.get(i);
	}
	
	public Location getStartLocation(){
		return this.startLocation;
	}

	public Array<Vector2> getLocationPositions(){ // renaming from location?
		Array<Vector2> positions = new Array<Vector2>();
		for (Connection connection:this.connections){
			positions.add(connection.getLocation().getCoords());
		}
		return positions;
	}

	public void addConnection(Connection connection){
		connections.add(connection);
		size+=1;
	}
	
}
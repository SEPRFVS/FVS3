package com.turkishdelight.taxe.routing;

import java.util.ArrayList;

import com.turkishdelight.taxe.worldobjects.Location;

public class Route {
	// Contains information on route from location to another, possibly through multiple locations via multiple paths
	// Has start location, then array of connections. 
	
	private ArrayList<Connection> connections =new ArrayList<Connection>();		// collection of connections
	private Location startLocation;												// starting location of route (is connected to first location in connections)
	private int size = 0;														// number of paths in route

	public Route() {
		this.connections = new ArrayList<Connection>();
	}
	
	public Route(ArrayList<Location> locations){
		// takes an arraylist of locations, creates a route if all locations are connected. 
		// TODO ensure that list is correct size
		Location previousLocation = null;
		for (Location location : locations){
			if (size == 0) {
				this.startLocation = location;
			} else {
				if (previousLocation.isConnected(location)){
					this.connections.add(new Connection(location, previousLocation.getCurvedRoute(location)));
				} else {
					System.out.println("NOT A VALID ROUTE"); // TODO be stricter here
				}
			}
			previousLocation = location;	
			size++;
		}
	}
	
	public int numLocations(){
		return size;
	}
	
	public Connection getConnection(int i){
		return connections.get(i);
	}
	
	public Location getStartLocation(){
		return this.startLocation;
	}

	public void addConnection(Connection connection){
		connections.add(connection);
		size+=1;
	}
	
}
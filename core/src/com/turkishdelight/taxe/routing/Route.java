package com.turkishdelight.taxe.routing;

import java.util.ArrayList;

import com.turkishdelight.taxe.worldobjects.RouteLocation;
import com.turkishdelight.taxe.worldobjects.Station;

public class Route {
	// Contains information on route from location to another, possibly through multiple locations via multiple paths
	// Has start location, then array of connections. 
	
	
	private ArrayList<Connection> connections =new ArrayList<Connection>();		// collection of connections
	private RouteLocation startLocation;										// starting location of route (must connected to first location in connections)
	private int size = 0;														// number of paths in route
	private String name = "";													// name of route of form "Location1Location2..."
	public Route() {
		this.connections = new ArrayList<Connection>();
	}
	
	public int getSize() {
		return size;
	}
	
	public Route(ArrayList<RouteLocation> locations){
		// takes an arraylist of locations, creates a route if all locations are connected. 
		// TODO ensure that list is correct size
		RouteLocation previousLocation = null;
		String name = "";
		for (RouteLocation location : locations){
			if (size == 0) {
				this.startLocation =  location;
			} else {
				if (previousLocation.isConnected(location)){
					this.connections.add(new Connection(location, previousLocation.getCurvedRoute(location)));
					name += location.getName();
				} else {
					// if the previous location isnt connected to the current location, invalid route.
					System.out.println("NOT A VALID ROUTE"); // be stricter here
				}
			}
			previousLocation = location;	
			size++;
		}
		this.name = name;
	}
	
	public int numLocations(){
		return size;
	}
	
	public Connection getConnection(int i){
		return connections.get(i);
	}
	
	public RouteLocation getStartLocation(){
		return this.startLocation;
	}

	public void addConnection(Connection connection){
		connections.add(connection);
		size+=1;
	}

	public String getName() {
		// get the name of the route- of form LocationLocationLocation
		return this.name;
	}
	
}
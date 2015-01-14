package com.turkishdelight.taxe.routing;

import java.util.ArrayList;

import com.turkishdelight.taxe.worldobjects.RouteLocation;
import com.turkishdelight.taxe.worldobjects.Station;

public class Route {
	// Contains information on route from location to another, possibly through multiple locations via multiple paths
	// Has start location, then array of connections. 
	
	// TODO take location strings and get 
	
	private ArrayList<Connection> connections =new ArrayList<Connection>();		// collection of connections
	private Station startStation;												// starting location of route (is connected to first location in connections)
	private int size = 0;														// number of paths in route

	public Route() {
		this.connections = new ArrayList<Connection>();
	}
	
	public Route(ArrayList<RouteLocation> locations){
		// takes an arraylist of locations, creates a route if all locations are connected. 
		// TODO ensure that list is correct size
		RouteLocation previousLocation = null;
		for (RouteLocation location : locations){
			if (size == 0) {
				this.startStation = (Station) location;
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
	
	public Station getStartLocation(){
		return this.startStation;
	}

	public void addConnection(Connection connection){
		connections.add(connection);
		size+=1;
	}
	
}
package com.fvs.taxe.goals;

/**********************************
 * Taken from http://www.algolist.com/code/java/Dijkstra%27s_algorithm
 **********************************/

import com.fvs.taxe.routing.Connection;
import com.fvs.taxe.worldobjects.RouteLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

class DijkstraVertex implements Comparable<DijkstraVertex>{
    public final String name;
    public ArrayList<DijkstraEdge> adjacencies;
    public int minDistance = Integer.MAX_VALUE; //Shortest distance to get to Vertex from start
    public DijkstraVertex previous;
    
    public DijkstraVertex(String argName) { 
    	name = argName;
    	adjacencies = new ArrayList<DijkstraEdge>();
    }
    public String toString() { 
    	return name; 
    }
    
    //Get difference in distance
    public int compareTo(DijkstraVertex other) {
        return Double.compare(minDistance, other.minDistance);
    }
}

class DijkstraEdge {
    public final DijkstraVertex target;
    public final int weight;
    public DijkstraEdge(DijkstraVertex argTarget, int argWeight) {
    	target = argTarget; weight = argWeight;
    }
}

public class Dijkstra {
	
	//Static function to calculate distance between stations/junctions
    public static void computePaths(DijkstraVertex source) {
        source.minDistance = 0;
        //Use Priority Queue to store nodes to be analysed
        PriorityQueue<DijkstraVertex> vertexQueue = new PriorityQueue<DijkstraVertex>();
      	vertexQueue.add(source);

      	while (!vertexQueue.isEmpty()) {
      		//Loop through each item in Queue
      		DijkstraVertex current = vertexQueue.poll();

            // Visit each edge exiting
            for (DijkstraEdge edge : current.adjacencies) {
                DijkstraVertex vertex = edge.target;
                int weight = edge.weight;
                int distanceThroughU = current.minDistance + weight; //Calculate total distance along path to vertex
                if (distanceThroughU < vertex.minDistance) {
                	//If vertex already has shorter path available
                	vertexQueue.remove(vertex);
                	vertex.minDistance = distanceThroughU ;
                	vertex.previous = current;
                	vertexQueue.add(vertex);
                }
            }
        }
    }
    
    //Return shortest path from source to destination (Not current used but useful if implementing a computer player)
    public static List<DijkstraVertex> getShortestPathTo(DijkstraVertex target) {
        List<DijkstraVertex> path = new ArrayList<DijkstraVertex>();
        for (DijkstraVertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }

    public static int calculate(ArrayList<RouteLocation> routeLocations, RouteLocation start, RouteLocation dest) {
    	//Initialise variables
    	ArrayList<DijkstraVertex> vertices = new ArrayList<DijkstraVertex>();
    	DijkstraVertex startVertex = new DijkstraVertex("");
    	DijkstraVertex destVertex = new DijkstraVertex("");
    	
    	//Add all stations/junctions to algorithm structure
    	for(RouteLocation routeLocation : routeLocations) {
    		DijkstraVertex newVertex = new DijkstraVertex(routeLocation.getName());
    		vertices.add(newVertex);
    		if(routeLocation == start){
    			startVertex = newVertex;
    		}else if(routeLocation == dest){
    			destVertex = newVertex;
    		}
    	}
    	
    	//Add all connections to route structure
    	for(RouteLocation routeLocation : routeLocations) {
    		DijkstraVertex vertex = new DijkstraVertex("");
    		//Find correct initial vertex
    		for(DijkstraVertex test : vertices){
    			if(test.name.equals(routeLocation.getName())) {
    				vertex = test;
    			}
    		}
    		for(Connection connection : routeLocation.getConnections()) {
    			DijkstraVertex connectionDest = new DijkstraVertex("");
    			//Find correct destination vertex
    			for(DijkstraVertex test: vertices){
    				if(test.name.equals(connection.getTargetLocation().getName())){
    					connectionDest = test;
    				}
    			}
    			vertex.adjacencies.add(new DijkstraEdge(connectionDest, Math.round(connection.getPath().getFinalDistance()))); //Use final distance so parts of route aren't counted twice
    		}
    	}
    	
    	computePaths(startVertex); //Compute Distances
    	
    	return destVertex.minDistance;
    }
}
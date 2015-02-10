package com.fvs.taxe.goals;

/**********************************
 * Taken from http://www.algolist.com/code/java/Dijkstra%27s_algorithm
 **********************************/

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.fvs.taxe.routing.Connection;
import com.fvs.taxe.worldobjects.RouteLocation;

class DijkstraVertex implements Comparable<DijkstraVertex>{
    public final String name;
    public ArrayList<DijkstraEdge> adjacencies;
    public int minDistance = Integer.MAX_VALUE;
    public DijkstraVertex previous;
    
    public DijkstraVertex(String argName) { 
    	name = argName;
    	adjacencies = new ArrayList<DijkstraEdge>();
    }
    public String toString() { 
    	return name; 
    }
    
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
	
    public static void computePaths(DijkstraVertex source) {
        source.minDistance = 0;
        PriorityQueue<DijkstraVertex> vertexQueue = new PriorityQueue<DijkstraVertex>();
      	vertexQueue.add(source);

      	while (!vertexQueue.isEmpty()) {
      		DijkstraVertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (DijkstraEdge e : u.adjacencies) {
                DijkstraVertex v = e.target;
                int weight = e.weight;
                int distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                	vertexQueue.remove(v);
                	v.minDistance = distanceThroughU ;
                	v.previous = u;
                	vertexQueue.add(v);
                }
            }
        }
    }

    public static List<DijkstraVertex> getShortestPathTo(DijkstraVertex target) {
        List<DijkstraVertex> path = new ArrayList<DijkstraVertex>();
        for (DijkstraVertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }

    public static int calculate(ArrayList<RouteLocation> routeLocations, RouteLocation start, RouteLocation dest) {
    	ArrayList<DijkstraVertex> vertices = new ArrayList<DijkstraVertex>();
    	DijkstraVertex startVertex = new DijkstraVertex("");
    	DijkstraVertex destVertex = new DijkstraVertex("");
    	
    	for(RouteLocation routeLocation : routeLocations) {
    		DijkstraVertex newVertex = new DijkstraVertex(routeLocation.getName());
    		vertices.add(newVertex);
    		if(routeLocation == start){
    			startVertex = newVertex;
    		}else if(routeLocation == dest){
    			destVertex = newVertex;
    		}
    	}
    	
    	for(RouteLocation routeLocation : routeLocations) {
    		DijkstraVertex vertex = new DijkstraVertex("");
    		for(DijkstraVertex test : vertices){
    			if(test.name.equals(routeLocation.getName())) {
    				vertex = test;
    			}
    		}
    		for(Connection connection : routeLocation.getConnections()) {
    			DijkstraVertex connectionDest = new DijkstraVertex("");
    			for(DijkstraVertex test: vertices){
    				if(test.name.equals(connection.getTargetLocation().getName())){
    					connectionDest = test;
    				}
    			}
    			vertex.adjacencies.add(new DijkstraEdge(connectionDest, Math.round(connection.getPath().getFinalDistance())));
    		}
    	}
    	
    	computePaths(startVertex);
    	
    	return destVertex.minDistance;
    	
    	/*DijkstraVertex v0 = new DijkstraVertex("Redvile");
    	DijkstraVertex v1 = new DijkstraVertex("Blueville");
    	DijkstraVertex v2 = new DijkstraVertex("Greenville");
		DijkstraVertex v3 = new DijkstraVertex("Orangeville");
		DijkstraVertex v4 = new DijkstraVertex("Purpleville");

		v0.adjacencies = new DijkstraEdge[]{ new DijkstraEdge(v1, 5),
	                             new DijkstraEdge(v2, 10),
                               new DijkstraEdge(v3, 8) };
		v1.adjacencies = new DijkstraEdge[]{ new DijkstraEdge(v0, 5),
	                             new DijkstraEdge(v2, 3),
	                             new DijkstraEdge(v4, 7) };
		v2.adjacencies = new DijkstraEdge[]{ new DijkstraEdge(v0, 10),
                               new DijkstraEdge(v1, 3) };
		v3.adjacencies = new DijkstraEdge[]{ new DijkstraEdge(v0, 8),
	                             new DijkstraEdge(v4, 2) };
		v4.adjacencies = new DijkstraEdge[]{ new DijkstraEdge(v1, 7),
                               new DijkstraEdge(v3, 2) };
		DijkstraVertex[] vertices = { v0, v1, v2, v3, v4 };
        computePaths(v0);
        for (DijkstraVertex v : vertices) {
        	System.out.println("Distance to " + v + ": " + v.minDistance);
        	List<DijkstraVertex> path = getShortestPathTo(v);
        	System.out.println("Path: " + path);
        }*/
    }
}
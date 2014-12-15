package com.turkishdelight.taxe.routing;

import com.turkishdelight.taxe.worldobjects.Location;

public class Connection {

	  private final Location endLocation;
	  private CurvedRoute curvedRoute;

	  public Connection(Location endLocation, CurvedRoute curvedRoute) {
	    this.endLocation = endLocation;
	    this.curvedRoute = curvedRoute;
	  }

	  public Location getLocation() { return endLocation; }
	  public CurvedRoute getPath() { return curvedRoute; }
	  
	  public void setRoute(CurvedRoute curvedRoute){
		  this.curvedRoute = curvedRoute;
	  }
	  
	  @Override
	  public boolean equals(Object o) {
	    if (o == null) {
	    	return false;
	    }
	    if (!(o instanceof Connection)) {
	    	return false;
	    }
	    Connection pair = (Connection) o;
	    return this.endLocation.equals(pair.getLocation()) && this.curvedRoute.equals(pair.getPath());
	  }

	}
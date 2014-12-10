package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.worldobjects.Location;

public class Connection {

	  private final Location endLocation;
	  private CatmullRomSpline<Vector2> path;

	  public Connection(Location endLocation, CatmullRomSpline<Vector2> crs) {
	    this.endLocation = endLocation;
	    this.path = crs;
	  }

	  public Location getLocation() { return endLocation; }
	  public CatmullRomSpline<Vector2> getPath() { return path; }
	  
	  public void setPath(CatmullRomSpline<Vector2> path){
		  this.path = path;
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
	    return this.endLocation.equals(pair.getLocation()) && this.path.equals(pair.getPath());
	  }

	}
package com.turkishdelight.taxe.routing;

import com.turkishdelight.taxe.worldobjects.Location;

public class Connection {

	private final Location endLocation;
	private CurvedPath curvedPath;

	public Connection(Location endLocation, CurvedPath curvedPath) {
		this.endLocation = endLocation;
		this.curvedPath = curvedPath;
	}

	public Location getLocation() { 
		return endLocation; 
	}

	public CurvedPath getPath() { 
		return curvedPath; 
	}

	public void setRoute(CurvedPath curvedPath){
		this.curvedPath = curvedPath;
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
		return this.endLocation.equals(pair.getLocation()) && this.curvedPath.equals(pair.getPath());
	}

}
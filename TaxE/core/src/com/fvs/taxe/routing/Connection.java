package com.fvs.taxe.routing;

import com.fvs.taxe.worldobjects.RouteLocation;

public class Connection {

	private final RouteLocation endLocation;
	private CurvedPath curvedPath;

	public Connection(RouteLocation endLocation, CurvedPath curvedPath) {
		this.endLocation = endLocation;
		this.curvedPath = curvedPath;
	}

	public RouteLocation getTargetLocation() { 
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
		return this.endLocation.equals(pair.getTargetLocation()) && this.curvedPath.equals(pair.getPath());
	}

}
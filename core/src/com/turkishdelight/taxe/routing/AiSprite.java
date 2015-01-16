package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.worldobjects.Station;

public abstract class AiSprite extends Clickable {
	// abstract class for anything that follows a path on every turn (extended by carriage, train)
	protected Player player; 
	protected static final int SPRITEWIDTH = 50;		// could change to widths/ heights of corresponding trains
	protected static final float SPRITEHEIGHT = 20;
	protected Route route;							// the complete route from start to end
	protected Connection connection;				// current connection the Train is on
	protected int waypoint = 0;						// index of route (which route in route) currently on
	protected CurvedPath path;						// current path aiSprite is on
	protected boolean completed;					// has train completed entire route?
	
	protected int midSpritex;
	protected int midSpritey;						// halfway of sprite, used to correct into middle of route

	protected float current = 0; 					// 'time' passed (between 0 and 1)
	protected Vector2 out = new Vector2(1,1);		// vector to output current location at (set at (1,1) to stop jumping when starting a new route)

	protected Polygon polygon;						// polygon used for collision detection
	protected int weight;							// weight of aiSprite
	protected boolean hasStopped;						// determines if aiSprite has hasStopped due to collision

	protected float routeDistance = 0;				// actual distance in pixels travelled along entire route
	protected float pathDistance = 0;				// actual distance in pixels travelled along the current path

	public AiSprite(Scene parentScene, Texture texture, Player player, Station station) {
		super(parentScene, texture, Game.objectsZ);
		setSize(SPRITEWIDTH, SPRITEHEIGHT);	
		setOriginCenter();																	// used for rotation
		
		this.player = player;
		
		midSpritex = (int) SPRITEWIDTH/2;  
		midSpritey = (int) SPRITEHEIGHT/2;
		Vector2 startLocation = station.getCoords();
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);

		// polygon for collision detection
		float[] vertices = {0,0,0,SPRITEHEIGHT,SPRITEWIDTH,SPRITEHEIGHT,SPRITEWIDTH,0};		
		this.polygon = new Polygon(vertices);
		this.polygon.setOrigin(getOriginX(), getOriginY());
		this.polygon.setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
	}

	@Override
	public abstract void updateTurn();
	protected abstract void updatePosition();
	public abstract void setRoute(Route route);
	public abstract void restoreRoute(Route route, int waypoint, float current);
	public abstract int getWeight();

	public Polygon getPolygon(){
		return polygon;
	}

	public float getCurrent(){
		return current;
	}
	
	public float getRouteDistance(){
		return routeDistance;
	}

	public void stopSprite(){
		// currently unused, was used in collisions
		this.hasStopped = true;
	}
	
	protected void move(){
		// calculations for moving sprite, called from updatePosition of aiSprite
		path.valueAt(out,current);
		float xposition = out.x - midSpritex;
		float yposition = out.y - midSpritey;
		setPosition(xposition, yposition );
		polygon.setPosition(xposition, yposition);

		path.derivativeAt(out, current);
		out.nor();
		float angle = out.angle();
		setRotation(angle);
		polygon.setRotation(angle);
	}


}
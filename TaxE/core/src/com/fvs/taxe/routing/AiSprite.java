package com.fvs.taxe.routing;

import java.security.InvalidParameterException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Clickable;
import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.Scene;
import com.fvs.taxe.worldobjects.Station;

public abstract class AiSprite extends Clickable {
	public enum AIType
	{
		TRAIN, OBSTACLE;
	}
	private AIType type = AIType.TRAIN;
	// abstract class for anything that follows a path on every turn (extended by train)
	protected Player player; 
	public static final int SPRITEWIDTH = 50;		// could change to widths/ heights of corresponding trains
	public static final float SPRITEHEIGHT = 20;
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
		if (player == null || station == null){
			throw new InvalidParameterException();
		}
		setSize(SPRITEWIDTH, SPRITEHEIGHT);	
		setOriginCenter();																	// used for rotation
		
		this.player = player;
		
		midSpritex = (int) SPRITEWIDTH/2;  
		midSpritey = (int) SPRITEHEIGHT/2;
		Vector2 startLocation = station.getPosition();
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);

		// polygon for collision detection
		float[] vertices = {0,0,0,SPRITEHEIGHT,SPRITEWIDTH,SPRITEHEIGHT,SPRITEWIDTH,0};		
		this.polygon = new Polygon(vertices);
		this.polygon.setOrigin(getOriginX(), getOriginY());
		this.polygon.setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
	}

	@Override
	public abstract void updateTurn();
	public abstract void updatePosition();
	public abstract int getWeight();
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Polygon getPolygon(){
		return polygon;
	}

	public Vector2 getPosition(){
		return new Vector2(getX(), getY());
	}
	
	public void setAiSpritePosition(int x, int y){
		// sets the image location and polygon location
		this.setPosition(x, y);
		this.polygon.setPosition(x, y);
	}
	
	public float getCurrent(){
		return current;
	}
	
	public void setCurrent(float current){
		// used purely for testing purposes
		this.current = current;
	}
	
	public void setPath(CurvedPath path){
		// used purely for testing purposes
		this.path = path;
	}
	
	public float getRouteDistance(){
		return routeDistance;
	}
	
	public float getPathDistance(){
		return pathDistance;
	}
	
	public Route getRoute(){
		return route;
	}
	
	public CurvedPath getPath(){
		return path;
	}
	
	public Connection getConnection(){
		return connection;
	}

	public Vector2 getSize(){
		return new Vector2(SPRITEWIDTH, SPRITEHEIGHT);
	}
	
	public Vector2 getSizeOffset(){
		// offset applied to centralise the aiSprite
		return new Vector2(midSpritex, midSpritey);
	}
	
	public boolean hasCompleted(){
		// has aisprite completed path?
		return this.completed;
	}
	
	public void stopSprite(){
		// currently unused, was used in collisions
		this.hasStopped = true;
	}
	
	public void move(){
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

	
	public AIType getAIType() {
		return type;
	}

	public void setAIType(AIType type) {
		this.type = type;
	}
}
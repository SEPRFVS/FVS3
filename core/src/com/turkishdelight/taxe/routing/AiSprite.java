package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.worldobjects.Location;

public abstract class AiSprite extends Clickable {
	// abstract class for anything that follows a path on every turn (extended by carriage, train)

	private static final int SPRITEWIDTH = 50;		// TODO change to widths/ heights of corresponding trains
	private static final float SPRTEHEIGHT = 20;
	protected Route route;							// the complete route from start to end
	protected Connection connection;				// current connection the Train is on
	protected int waypoint = 0;						// index of route (which route in route) currently on
	protected CurvedPath curvedPath;				// current curvedPath aiSprite is on

	protected int midSpritex;
	protected int midSpritey;						// halfway of sprite, used to correct into middle of route

	protected int speed = 1; 						// speed to travel - TODO probably only use in train
	protected float current = 0; 					// 'time' passed (between 0 and 1)
	protected Vector2 out = new Vector2(1,1);		// vector to output current location at (set at (1,1) to stop jumping when starting a new route)

	protected Polygon polygon;						// polygon used for collision detection
	protected int weight;							// weight of aiSprite
	protected boolean stopped;						// determines if train has stopped due to collision

	protected float distance = 0;					// actual distance in pixels travelled along entire route


	public AiSprite(Scene parentScene, Texture texture, Route route) {

		super(parentScene, texture, Game.objectsZ);
		setSize(SPRITEWIDTH, SPRTEHEIGHT);	
		setOriginCenter();			// used for rotation
		this.route = route;
		Vector2 startLocation = route.getStartLocation().getCoords(); // set Location to start of route
		connection = route.getConnection(waypoint);
		curvedPath = connection.getPath();

		midSpritex = (int) SPRITEWIDTH/2;  
		midSpritey = (int) SPRTEHEIGHT/2;
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);

		// polygon for collision detection
		float[] vertices = {0,0,0,SPRTEHEIGHT,SPRITEWIDTH,SPRTEHEIGHT,SPRITEWIDTH,0};		// TODO make more accurate, so it fits the curves of the trains
		this.polygon = new Polygon(vertices);
		this.polygon.setOrigin(getOriginX(), getOriginY());
		this.polygon.setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
	}

	public AiSprite(Scene parentScene, Texture texture, Location location) {

		super(parentScene, texture, Game.objectsZ);
		setSize(SPRITEWIDTH, SPRTEHEIGHT);	
		setOriginCenter();			// used for rotation
		Vector2 startLocation = location.getCoords();

		midSpritex = (int) SPRITEWIDTH/2;  
		midSpritey = (int) SPRTEHEIGHT/2;
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);

		// polygon for collision detection
		float[] vertices = {0,0,0,SPRTEHEIGHT,SPRITEWIDTH,SPRTEHEIGHT,SPRITEWIDTH,0};		// TODO make more accurate, so it fits the curves of the trains
		this.polygon = new Polygon(vertices);
		this.polygon.setOrigin(getOriginX(), getOriginY());
		this.polygon.setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
	}

	@Override
	public abstract void updateTurn();
	protected abstract void updatePosition();
	public abstract void setPath(Route route);
	public abstract int getWeight();

	public Polygon getPolygon(){
		return polygon;
	}

	public float getCurrent(){
		return current;
	}

	public float getDistance(){
		return distance;
	}

	public int getSpeed() {
		return speed;
	}

	public void stopSprite(){
		this.stopped = true;
	}

	@Override
	public void draw(Batch batch) {
		super.draw(batch);
		/*// for testing only
		 * if (stopped){
			stopped = false;
			return;
		}
		if (!completed) {
			updatePosition(); // Update the position
		}*/
	}

	protected void move(){
		// calculations for moving sprite
		curvedPath.valueAt(out,current);
		float xposition = out.x - midSpritex;
		float yposition = out.y - midSpritey;
		setPosition(xposition, yposition );
		polygon.setPosition(xposition, yposition);

		curvedPath.derivativeAt(out, current);
		out.nor();
		float angle = out.angle();
		setRotation(angle);
		polygon.setRotation(angle);
	}


}
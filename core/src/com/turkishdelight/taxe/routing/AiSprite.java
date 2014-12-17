package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public abstract class AiSprite extends SpriteComponent {

	private static final int SPRITEWIDTH = 50;		// TODO change to widths/ heights of corresponding trains
	private static final float SPRTEHEIGHT = 20;
	protected Path path;							// the complete path from start to end
	protected Connection connection;				// current connection the Train is on
	protected int waypoint = 0;						// index of path (which route in path) currently on
	protected CurvedRoute curvedRoute;				// current curvedRoute aiSprite is on
	
	protected int midSpritex;
	protected int midSpritey;						// halfway of sprite, used to correct into middle of path
	
	protected int speed = 1; 						// speed to travel - TODO probably only use in train
	protected float current = 0; 					// 'time' passed (between 0 and 1)
	protected Vector2 out = new Vector2(1,1);		// vector to output current location at (set at (1,1) to stop jumping when starting a new path)
	
	protected Polygon polygon;						// polygon used for collision detection
	protected int weight;							// weight of aiSprite
	protected boolean stopped;						// determines if train has stopped due to collision
	
	protected float distance = 0;					// actual distance in pixels travelled along entire path
	
	
	public AiSprite(Scene parentScene, Texture texture, Path path) {
		
		super(parentScene, texture, Game.objectsZ);
		setSize(SPRITEWIDTH, SPRTEHEIGHT);	
		setOriginCenter();			// used for rotation
		this.path = path;
	    Vector2 startLocation = path.getStartLocation().getCoords(); // set Location to start of path
	    connection = path.getConnection(waypoint);
		curvedRoute = connection.getPath();
		
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
	public abstract void setPath(Path path);
	
	
	public int getWeight(){
		return weight;
	}
	
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
		curvedRoute.valueAt(out,current);
		float xposition = out.x - midSpritex;
		float yposition = out.y - midSpritey;
		setPosition(xposition, yposition );
		polygon.setPosition(xposition, yposition);
		
		curvedRoute.derivativeAt(out, current);
		out.nor();
		float angle = out.angle();
		setRotation(angle);
		polygon.setRotation(angle);
	}
}
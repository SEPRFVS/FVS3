package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.scenes.GameScene;

public class Train extends SpriteComponent {

	private int midSpritex;
	private int midSpritey;					// halfway of sprite, used to correct into middle of path
	int speed = 1; 							// speed to travel
	float current = 0; 						// 'time' passed (between 0 and 1)
	private Vector2 out = new Vector2(1,1);	// vector to output current location at (set at (1,1) to stop jumping when starting a new path)
	private Path path;						// the complete path from start to end
	
	Connection connection;					// current connection the aiSprite is on
	private int waypoint = 0;				// index of pathv2 (which path) currently on
	private boolean completed;				// has aiSprite completed entire path?
	float overshoot;						// amount that the curvedaisprite passes the station by
	private Polygon polygon;				// polygon used for collision detection
	private int weight;						// weight of train (calculated from core stats of train, amount of carriages)
	private boolean stopped;				// determines if train has stopped due to collision, other things
	
	public Train(int width, int height, int weight, Scene parentScene, Texture text, int z, Path path) {
		super(parentScene, text, z);
		setSize(width, height);	
		setOriginCenter();			// used for rotation
		this.weight = weight;
		this.path = path;
		waypoint = 0;
	    Vector2 startLocation = path.getStartLocation().getCoords(); // set Location to start of path
		
		midSpritex = (int) width/2;  
		midSpritey = (int) height/2;
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
		
		// polygon for collision detection
		float[] vertices = {0,0,0,height,width,height,width,0};		// TODO make more accurate, so it fits the curves of the trains
		polygon = new Polygon(vertices);
		polygon.setOrigin(getOriginX(), getOriginY());
		polygon.setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
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
	
	public void stopTrain(){
		this.stopped = true;
	}
	
	public void setPath(Path path) {
		// ASSUMES GIVEN PATH FROM STATION ALREADY AT
		this.path = path;
		waypoint = 0;
		completed = false;
		current = 0;
		out = new Vector2(1,1);
	}
	
	public int getWeight(){
		return weight;
	}
	
	public Polygon getPolygon(){
		return polygon;
	}
	
	public int getSpeed(){
		return (int) speed;
	}
	
	
	@Override
	public void updateTurn()
	{	
		if (stopped){
			stopped = false;
			return;
		}
		if (!completed ) {
			updatePosition(); // Update the position
		}
	}
	
	private void updatePosition() {
		// add on, then clear any overshoot if the train stopped at the station previous turn 
		current += overshoot;
		overshoot = 0;
		
		// for constant velocity, ignoring effect of curves (TODO test this!)
		connection = path.getConnection(waypoint);
		CatmullRomSpline<Vector2> crs = connection.getPath();
		crs.derivativeAt(out, current);
		current += speed*10/out.len();    // 10 is just an arbitrary number, works well- increase to increase distance travelled each turn
		
		// for variable velocity (curves affect movement)
		//current += speed;
		
		// for testing with animation only
		//current += Gdx.graphics.getDeltaTime() * speed / out.len() * 100;
		
		if(current >= 0.95) {// if a waypoint is reached (0.95 means no tiny movements!)
			System.out.println("Waypoint reached");
			
			if(waypoint+2 >= path.size()) { // if reached final waypoint, fix it to final waypoint
				System.out.println("Final waypoint reached");
				completed = true;
				current = 1;
			} else {// otherwise fix it to station, give it overshoot next turn
				waypoint++; // move to next waypoint
				overshoot = current -1;
				current = 0;
			}
		}
		
		// calculations for current position on given connection
		connection = path.getConnection(waypoint);
		crs = connection.getPath();
		
		crs.valueAt(out,current);
		setPosition(out.x - midSpritex, out.y - midSpritey);

		crs.derivativeAt(out, current);
		out.nor();
		setRotation(out.angle());
		
		// used for collision detection
		//polygon.setOrigin(getOriginX(), getOriginY());
		polygon.setPosition(getX(), getY());
		polygon.setRotation(out.angle());	
	}
}
package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Train extends SpriteComponent {

	private Path path;							// the complete path from start to end
	private Connection connection;				// current connection the Train is on
	private int waypoint = 0;					// index of path (which path) currently on
	private CurvedRoute curvedRoute;
	
	private SpriteComponent carriage;		
	private int midSpritex;
	private int midSpritey;						// halfway of sprite, used to correct into middle of path
	
	private int speed = 1; 						// speed to travel
	private float current = 0; 					// 'time' passed (between 0 and 1)
	private Vector2 out = new Vector2(1,1);		// vector to output current location at (set at (1,1) to stop jumping when starting a new path)
	private boolean completed;					// has train completed entire path?
	private float overshoot;					// amount that the train passes the station by
	
	private Polygon polygon;					// polygon used for collision detection
	private int weight;							// weight of train (calculated from core stats of train, amount of carriages)
	private boolean stopped;					// determines if train has stopped due to collision, other things
	
	private float distance = 0;					// actual distance in pixels travelled along route (not entire path)
	private float previouscurrent = 0;			// the previous current value for the previous turn- used for distance calculation
	private float carriageCurrent = 0;			// the value of current for the carriage
	
	public Train(int width, int height, int weight, Scene parentScene, Texture text, int z, Path path, SpriteComponent carriage) {
		super(parentScene, text, z);
		setSize(width, height);	
		setOriginCenter();			// used for rotation
		this.weight = weight;
		this.path = path;
		waypoint = 0;
	    Vector2 startLocation = path.getStartLocation().getCoords(); // set Location to start of path
	    connection = path.getConnection(waypoint);
		curvedRoute = connection.getPath();
		
		midSpritex = (int) width/2;  
		midSpritey = (int) height/2;
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
		
		// polygon for collision detection
		float[] vertices = {0,0,0,height,width,height,width,0};		// TODO make more accurate, so it fits the curves of the trains
		polygon = new Polygon(vertices);
		polygon.setOrigin(getOriginX(), getOriginY());
		polygon.setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
		
		// set up carriage here
		this.carriage = carriage;
		carriage.setSize(width, height);
		carriage.setOriginCenter();
		carriage.setPosition(getX(), getY());
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
			updatePosition(); 
		}
	}
	
	private void updatePosition() {
		// add on, then clear any overshoot if the train stopped at the station previous turn 
		current += overshoot;
		overshoot = 0;
		
		// for constant velocity, ignoring effect of curves
		Vector2 prevout = new Vector2();
		curvedRoute.derivativeAt(prevout, current);
		
		//curvedRoute.derivativeAt(out, current);
		current += speed*30/prevout.len();    // 30 is just an arbitrary number, works well- increase to increase distance travelled each turn
		
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
				carriageCurrent=1;
			} else {// otherwise fix it to station, give it overshoot next turn
				overshoot = current-1;
				waypoint++; // move to next waypoint
				connection = path.getConnection(waypoint);	// get next connection in path
				curvedRoute = connection.getPath();					// get next route in path
				current = 0;
				carriageCurrent = 0;
				distance = 0;
				previouscurrent = 0;						// reset variables
			}
		}
		
		// calculations for collision detection
		
		curvedRoute.valueAt(out,current);
		setPosition(out.x - midSpritex, out.y - midSpritey);
		
		curvedRoute.derivativeAt(out, current);
		out.nor();
		setRotation(out.angle());
		
		// used for collision detection
		polygon.setPosition(getX(), getY());
		polygon.setRotation(out.angle());	

		
		
		// used for carriage calculation
		
		// calculates distance travelled based on distance between previous current, current distance
		// TODO find a more efficient way- perhaps a method in curvedroute?
		for (int i = (int) (previouscurrent*1000+1); i < (int) (current*1000); i++){
			distance += curvedRoute.getPoint(i/1000f).dst(curvedRoute.getPoint((i-1)/1000f)); 	
		}
		previouscurrent = current;
		
		System.out.println("Distance travelled = " + distance);
		
		// if distance greater than threshold or carriage is at start of station
		if (distance >= 50 || carriageCurrent == 0){
			
			if (current != 0 && current != 1){ // if not at start or end of route, calculate current position
				carriageCurrent = curvedRoute.getT(distance-50f);	// carriage is always 50 pixels behind train
			}
			
			curvedRoute.valueAt(out,carriageCurrent);
			carriage.setPosition(out.x - midSpritex, out.y - midSpritey);
			
			curvedRoute.derivativeAt(out, carriageCurrent);
			out.nor();
			carriage.setRotation(out.angle());
		}
		
	}
}
package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class CurvedAiSprite extends SpriteComponent {

	private int midSpritex;
	private int midSpritey;					// halfway of sprite, used to correct into middle of path
	float speed = 1f; 					// speed to travel
	float current = 0; 						// 'time' passed (between 0 and 1)
	private Vector2 out = new Vector2(1,1);	// vector to output current location at (set at (1,1) to stop jumping when starting a new path)
	private Path path;						// the complete path from start to end
	
	Connection connection;					// current connection the aiSprite is on
	private int waypoint = 0;				// index of pathv2 (which path) currently on
	private boolean completed;				// has aiSprite completed entire path
	float overshoot;

	public CurvedAiSprite(Scene parentScene, Texture text, int z, Path path) {
		super(parentScene, text, z);
		this.path = path;
		waypoint = 0;
		
		// set Location to start of path
	    Vector2 startLocation = path.getStartLocation().getCoords();
		
		midSpritex = 25;  //TODO possible constant-ification
		midSpritey = 25;
		setPosition(startLocation.x-midSpritex, startLocation.y-midSpritey);
	}
	
	public void setPath(Path path) {
		// ASSUMES GIVEN PATH FROM STATION ALREADY AT
		this.path = path;
		waypoint = 0;
		completed = false;
		current = 0;
		out = new Vector2(1,1);
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
		
		// for testing only
		/*if (!completed ) {
			updatePosition(); // Update the position
		}*/
	}
	
	@Override
	public void updateTurn()
	{
		if (!completed ) {
			updatePosition(); // Update the position
		}
	}
	
	private void updatePosition() {
		// add on, then clear any overshoot if the curvedaisprite stopped at the station previous turn 
		current += overshoot;
		overshoot = 0;
		
		// for constant velocity, ignoring effect of curves (TODO test this!)
		connection = path.getConnection(waypoint);
		CatmullRomSpline<Vector2> crs = connection.getPath();
		crs.derivativeAt(out, current);
		current += speed*10/out.len();    // 70 is just an arbitrary number, works well- increase to increase distance travelled each turn
		
		// for variable velocity (curves affect movement)
		//current += speed;
		
		// for testing with animation only
		//current += Gdx.graphics.getDeltaTime() * speed;
		
		
		if(current >= 1) {// if a waypoint is reached
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
		
		
	}
}
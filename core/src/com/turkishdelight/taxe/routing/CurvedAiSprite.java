package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class CurvedAiSprite extends SpriteComponent {

	private int midSpritex;
	private int midSpritey;					// halfway of sprite, used to correct into middle of path
	float speed = 0.15f; 					// speed to travel
	float current = 0; 						// 'time' passed (between 0 and 1)
	private Vector2 out = new Vector2();	// vector to output current location at(?)
	private Path path;						// the complete path from start to end
	
	Connection connection;					// current connection the aiSprite is on
	private int waypoint = 0;				// index of pathv2 (which path) currently on
	private boolean completed;				// has aiSprite completed entire path


	public CurvedAiSprite(Scene parentScene, Texture text, int z, Path path) {
		super(parentScene, text, z);
		this.path = path;
		waypoint = 0;
		
		// set Location to start of path
	    Vector2 startLocation = path.getStartLocation().getCoords();
		setPosition(startLocation.x, startLocation.y);
		midSpritex = 25;  //TODO possible constant-ification
		midSpritey = 25;
		
	}
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}
	
	@Override
	public void updateTurn()
	{
		if (!completed ) {
			updatePosition(); // Update the position
		}
	}
	
	private void updatePosition() {
		// TODO need code to save 'overshoot'
		
		current += speed;
		
		// testing if reached final waypoint
		if (waypoint + 1 >= path.size()) {
			System.out.println("Final waypoint reached");
			completed = true;
			return;
		}
		
		// calculation for current position on given connection
		connection = path.get(waypoint);
		CatmullRomSpline<Vector2> crs = connection.getPath();
		crs.valueAt(out,current);
		setPosition(out.x - midSpritex, out.y - midSpritey);

		crs.derivativeAt(out, current);
		out.nor();
		setRotation(out.angle());
		
		// if a waypoint is reached, notify as completed, move to next in path
		if(current >= 1) {
			System.out.println("Waypoint reached");
			current =1;// to waypoint location); // stops it passing past waypoint in high speeds

			if(waypoint + 1 >= path.size()) // if reached final waypoint, fix it to final waypoint
				completed = true;
			else {
				waypoint++;	
				current = 0;
			}
		}
	}
}
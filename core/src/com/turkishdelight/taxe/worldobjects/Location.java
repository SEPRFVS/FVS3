package com.turkishdelight.taxe.worldobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.LabelButton;
import com.turkishdelight.taxe.routing.Connection;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.scenes.GameScene;

public class Location extends SpriteComponent{

	// Class that is designed to hold information on locations- position and connections- connected locations and route to other locations 
	static Texture text = new Texture("location.png");
	private Vector2 coords;	      						  				// vector location of location
	private ArrayList<Connection> connections = new ArrayList<Connection>();  	// connected locations
	private int numConnections = 0;										// number of connected locations
	private LabelButton lbutton;
	private String locationName;
	private Boolean selectingRoute = false;
	
	public Location(final GameScene parentScene, String locationName, int x, int y) {
		super(parentScene, text, Game.locationZ);
		this.setSize(10, 10);
		this.locationName = locationName;
		coords = new Vector2(x, y);
		Texture clearButton = new Texture("Clear_Button.png");
		lbutton = new LabelButton(parentScene, clearButton, 40,40, Label.genericFont(Color.BLACK, 20)) {
			@Override
			public void onClickEnd()
			{
				if (!selectingRoute){
					setFont(Label.genericFont(Color.GREEN, 20));
				} else {
					parentScene.selectLocation(getLocationClass());
				}
			}
		};
		lbutton.setPosition(x, y-15);
		lbutton.setText(locationName);
		parentScene.Add(lbutton);
		
	}
	
	public void setFont(BitmapFont font){
		lbutton.setFont(font);
	}
	
	public String getName(){
		return locationName;
	}
	
	public Location getLocationClass() {
	    return Location.this;
	}
	
	public void addConnection(Location location, CurvedPath path) {
		this.connections.add(new Connection(location, path));
		numConnections++;
	}
	
	public int numConnections() {
		return numConnections;
	}

	public boolean isConnected(Location location) {
		connections = getConnections();
		for (Connection connection : connections) {
			if (connection.getLocation().equals(location)){ 
				return true;
			}
		}
		return false;
	}
	
	public CurvedPath getCurvedRoute(Location location) {
		// gets the route connecting this to location
		// currently assumes location is connected
		connections = getConnections();
		for (Connection connection : connections) {
			if (connection.getLocation().equals(location)){ 
				return connection.getPath();
			}
		}
		return null;
	}
	
	public Vector2 getCoords() {
		return this.coords;
	}

	public ArrayList<Connection> getConnections () {
		return this.connections;
	}

	public void setSelectingRoute(Boolean selectingRoute) {
		this.selectingRoute = selectingRoute;
	}
}
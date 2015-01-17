package com.turkishdelight.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.scenes.GameScene;

public class Junction extends RouteLocation {
	static Texture text = new Texture("location.png"); 	//TODO needs changing
	
	public Junction(final GameScene parentScene, String junctionName, int x, int y) {
		super(parentScene, text, junctionName,  x ,y);
		this.setSize(7, 7);
	}
	
}

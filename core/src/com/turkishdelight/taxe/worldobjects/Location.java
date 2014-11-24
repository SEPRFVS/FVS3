package com.turkishdelight.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Location extends SpriteComponent {

	static Texture text = new Texture("location.png");
	public Location(Scene parentScene) {
		super(parentScene, text, Game.locationZ);
		this.setSize(10, 10);
	}

}

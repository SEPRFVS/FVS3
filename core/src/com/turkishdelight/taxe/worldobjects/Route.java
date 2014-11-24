package com.turkishdelight.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Route extends SpriteComponent {

	static Texture text = new Texture("route.png");
	public Route(Scene parentScene, int z) {
		super(parentScene, text, Game.locationZ);
	}

}

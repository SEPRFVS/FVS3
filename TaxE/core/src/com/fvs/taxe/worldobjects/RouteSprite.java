package com.fvs.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Game;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;

public class RouteSprite extends SpriteComponent {

	static Texture text = new Texture("route.png");
	public RouteSprite(Scene parentScene) {
		super(parentScene, text, Game.locationZ);
	}

}

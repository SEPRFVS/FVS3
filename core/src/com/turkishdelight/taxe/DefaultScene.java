package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.worldobjects.Location;

public class DefaultScene extends Scene {
	Texture mapText;
	SpriteComponent map;
	@Override
	public void onCreate()
	{
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.mapZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Location London = new Location(this);
		London.setPosition(210, 390);
		Add(London);
		Add(map);
	}
	
	@Override
	public void Update()
	{
		
	}

}

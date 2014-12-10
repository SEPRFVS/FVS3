package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.guiobjects.Button;

public class DefaultScene extends Scene{
	Texture mapText;
	SpriteComponent map;
	@Override
	public void onCreate()
	{
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.mapZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(map);
		Button b = new Button(this);
		b.setPosition(600, 600);
		Add(b);
	}
	
	@Override
	public void Update()
	{
		
	}

}

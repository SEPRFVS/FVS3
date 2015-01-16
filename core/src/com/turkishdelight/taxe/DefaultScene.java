package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Pane;
import com.turkishdelight.taxe.guiobjects.Scroller;

public class DefaultScene extends Scene{
	Texture mapText;
	SpriteComponent map;
	@Override
	public void onCreate()
	{
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.backgroundZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(map);
		final Pane c = new Pane(this, mapText, Game.guiZ);
		Button b = new Button(this) {
			@Override
			public void onClickEnd()
			{
				c.setPosition(c.getX() / 2.0f, c.getY()/2.0f);
			}
		};
		b.setZ(Game.guiZ);
		b.setLocalPosition(100, 100);
		c.Add(b);
		SpriteComponent pc = new SpriteComponent(this, mapText, Game.backgroundZ);
		pc.setLocalPosition(0, 0);
		c.setPosition(300, 300);
		c.Add(pc);
		Add(c);
		Scroller s = new Scroller(this, Button.text, Game.guiZ);
		s.setRange(0, 600);
		s.setOrientation(false);
		s.setPosition(30, 200);
		Add(s);
	}
	
	@Override
	public void Update()
	{
		
	}

}

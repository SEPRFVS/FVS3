package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;


public class Button extends Clickable {

	static Texture text = new Texture("Button.png");
	public Button(Scene parentScene) {
		super(parentScene, text, Game.guiZ);
		this.setSize(100, 40);
	}
	
	public Button(Scene parentScene, Texture targText, int width, int height) {
		super(parentScene, targText, Game.guiZ);
		this.setSize(width, height);
	}
	
	public Button(Scene parentScene, Texture targText, int width, int height, int z) {
		super(parentScene, targText, z);
		this.setSize(width, height);
	}
	
	@Override
	public void onClickEnd()
	{
		System.out.println("No click method defined!");
	}
	
	@Override
	public void onClickStart()
	{
		
	}

}

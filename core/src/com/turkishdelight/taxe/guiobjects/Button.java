package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;


public class Button extends Clickable {
	//This class allows us to create objects that can be clicked in the GUI and game
	
	//We store a default button texture to use if no texture is passed
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
	
	//This method is called when the mouse is released on this button
	@Override
	public void onClickEnd()
	{
		System.out.println("No click method defined!");
	}
	
	//This method is called when the mouse is pressed on this button
	@Override
	public void onClickStart()
	{
		
	}

}

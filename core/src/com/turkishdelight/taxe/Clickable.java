package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;


public class Clickable extends SpriteComponent {
	//This class creates a generic class for click able objects in the game
	public Clickable(Scene parentScene, Texture text, int z) {
		super(parentScene, text, z);
		this.setClickAble(true);
	}
	
	//This method checks if a click's position is in the bounds of the sprite, and calls an event if that is true
	public boolean clickStart(int posX, int posY)
	{
		//Firstly we get the bounds of the item
		float minX = this.getX();
		float minY = this.getY();
		float maxX = this.getX() + this.getBoundingRectangle().getWidth();
		float maxY = this.getY() + this.getBoundingRectangle().getHeight();
		
		//Then we check for the touch location between the bounds
		if(posX > minX && posX < maxX)
		{
			if(posY > minY && posY < maxY)
			{
				//If we are with in the bounds we register a click event 
				onClickStart();
			return true;
			}
		}
		return false;
	}
	
	//This method checks if a click's position is in the bounds of the sprite, and calls an event if that is true
	public boolean clickEnd(int posX, int posY)
	{
		//Firstly we get the bounds of the item
		float minX = this.getX();
		float minY = this.getY();
		float maxX = this.getX() + this.getBoundingRectangle().getWidth();
		float maxY = this.getY() + this.getBoundingRectangle().getHeight();
		//Then we check for the touch location between the bounds
		if(posX > minX && posX < maxX)
		{
			if(posY > minY && posY < maxY)
			{
				//If we are with in the bounds we register a click event 
				onClickEnd();
			return true;
			}
		}
		return false;
	}
	
	
	//This method is overwritten to set up events when the sprite is clicked
	public void onClickStart()
	{
		
	}
	
	//This method is overwritten to set up events when the sprite is clicked
	public void onClickEnd()
	{
		
	}

	//This method is overwritten to set up events when the a specific character ch is typed
	public void onCharStroke(char ch)
	{
		
	}

	//This method is overwritten to set up events when a specific keystroke with keycode is pressed
	public void onKeyPressed(int keycode)
	{
		
	}
}

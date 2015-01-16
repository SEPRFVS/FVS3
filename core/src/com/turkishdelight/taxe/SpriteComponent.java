package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent extends Sprite{
	//All sprites are by default places at z order of 0
	private int z = 0;
	
	//We store the parentScene so we can interact with it
	private Scene parentScene;
	
	//These booleans store whether an object is clickable (and triggers onClick events), and whether
	//The activity of the object ignores a pause command
	private boolean clickAble = false;
	private boolean ignoresPause = false;
	
	
	public SpriteComponent(Scene parentScene, Texture text, int z)
	{
		super(text);
		this.setOriginCenter();
		this.z = z;
		this.parentScene = parentScene;
	}
	
	//Return z
	public int getZ()
	{
		return z;
	}
	
	//Set z
	public void setZ(int newZ)
	{
		z = newZ;
		//We indicate that the spriteComponents need to be reordered in the Component batch
		parentScene.postSpriteReorder();
	}
	
	//Get whether the sprite is clickable
	public boolean isClickAble()
	{
		return clickAble;
	}
	
	//Set whether the sprite is clickable. Register this with the scene
	public void setClickAble(boolean newClickAble)
	{
		this.clickAble = newClickAble;
		if(newClickAble == true)
		{
			parentScene.registerClickAble(this);
		}
		else
		{
			parentScene.removeClickAble(this);
		}
	}
	
	//Get whether the sprite updates when pause is clicked
	public boolean ignoresPause()
	{
		return ignoresPause;
	}
	
	//Set whether the sprite updates when pause is clicked. Register this with the scene
	public void setIgnoresPause(boolean newPause)
	{
		ignoresPause = newPause;
	}
	
	//This method is called when a sprite component's turn is called (e.g. for a specific player for trains, or
	//Each turn for money updates
	public void updateTurn()
	{
		
	}
	
	//This method is called based on the game ticks
	public void update()
	{
		
	}
	
	//Local positions are used for when a sprite component is within another pane, not at the top level of a scene
	
	private int localX = 0;
	private int localY = 0;
	
	public void setLocalPosition(int x, int y)
	{
		localX = x;
		localY = y;
	}
	
	public int getLocalX()
	{
		return localX;
	}
	
	public int getLocalY()
	{
		return localY;
	}
	
	public Scene getParentScene()
	{
		return parentScene;
	}
	
	

}

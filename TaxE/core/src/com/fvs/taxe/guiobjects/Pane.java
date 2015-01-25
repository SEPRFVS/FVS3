package com.fvs.taxe.guiobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fvs.taxe.Clickable;
import com.fvs.taxe.ComponentBatch;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;

public class Pane extends Clickable {
	//This class implements a componentBatch within a scene, allowing a set of sprites to be repositioned and reordered within a scene.
	
	//We store the previous location of the pane in these variables for comparison
	float previousX = 0;
	float previousY = 0;
	//This stores the active sprite components of the scene
	ComponentBatch components;
	ArrayList<Clickable> clickAbleObjects = new ArrayList<Clickable>();
	
	public Pane(Scene parentScene, int z) {
		super(parentScene, Button.text, z);
		components = new ComponentBatch(){
			@Override
			public void Update()
			{
				if(getX() != previousX || getY() != previousY)
				{
					previousX = getX();
					previousY = getY();
					for(SpriteComponent c : this.spriteComponents)
					{
						c.setPosition(c.getLocalX() + getX(), c.getLocalY() + getY());
					}
				}
			}
		};
	}
	
	//This method is used to get the component batch
	public ComponentBatch getComponents()
	{
		return components;
	}
	
	//This method is used to set the component batch
	public void setComponents(ComponentBatch newComponents)
	{
		this.components = newComponents;
	}
	
	//This method draws the scene
	@Override
	public void draw(Batch batch)
	{
		components.Draw((SpriteBatch)batch);
	}
	
	//This method is used to add a sprite component to the game
	public void Add(SpriteComponent spriteComp)
	{
		spriteComp.setPosition(spriteComp.getLocalX() + getX(), spriteComp.getLocalY() + getY());
		spriteComp.setIsPaneChild(true);
		components.Add(spriteComp);
		if(spriteComp.isClickAble())
		{
			registerClickAble(spriteComp);
		}
	}
	
	//This method is used to remove a sprite component from the game
	public void Remove(SpriteComponent spriteComp)
	{
		components.Remove(spriteComp);
		//If the sprite is clickable, we acknowledge and remove it
		if(spriteComp.isClickAble())
		{
			removeClickAble(spriteComp);
		}
	}
	
	//This method is used to store a new clickable sprite
	public void registerClickAble(SpriteComponent spriteComp)
	{
		if(!clickAbleObjects.contains(spriteComp))
		{
			clickAbleObjects.add((Clickable)spriteComp);
		}
	}
	
	//This method unregisters a stored clickable sprite
	public void removeClickAble(SpriteComponent spriteComp)
	{
		if(clickAbleObjects.contains(spriteComp))
		{
			clickAbleObjects.remove((Clickable)spriteComp);
		}
	}
	
	@Override
	//This method is called every frame. It can be overridden with game logic
	public void update()
	{
		components.Update();
	}
	
	//This method is called if a sprite wishes to force a reorder (e.g. it's z order has changed) in the ComponentBatch
	public void postSpriteReorder()
	{
		components.Reorder();
	}
	
	@Override
	//This method is called when the mouse is clicked up. The location of the mouse click is sent to the method as parameters
	public boolean clickEnd(int posX, int posY)
	{
		boolean click = false;
		for(Clickable item : clickAbleObjects)
		{
			if(item.clickEnd(posX, posY))
			{
				click = true;
			}
		}
		return click;
	}
	
	@Override
	//This method is called when the mouse is clicked down. The location of the mouse click is sent to the method as parameters
	public boolean clickStart(int posX, int posY)
	{
		boolean click = false;
		for(Clickable item : clickAbleObjects)
		{
			if(item.clickStart(posX, posY))
			{
				click = true;
			}
		}
		return click;
	}
	
	//This method clears the pane while avoiding concurrent arrays.
	public void clear()
	{
		components.clear();
		//Clearing like this avoids concurrent arrays
		ArrayList<Clickable> componentsToDrop = new ArrayList<Clickable>();
		for(Clickable s : clickAbleObjects)
		{
			componentsToDrop.add(s);
		}
		for(Clickable s : componentsToDrop)
		{
			clickAbleObjects.remove(s);
		}
		componentsToDrop = null;
	}
	
}

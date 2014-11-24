package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Scene {
	//This stores the active sprite components of the scene
	ComponentBatch components = new ComponentBatch();
	
	public Scene()
	{
		//When the scene is instantiated we call the on create method
		onCreate();
	}
	
	//This method can be overridden 
	public void onCreate()
	{
		
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
	public void Draw(SpriteBatch batch)
	{
		components.Draw(batch);
	}
	
	//This method is used to add a sprite component to the game
	public void Add(SpriteComponent spriteComp)
	{
		components.Add(spriteComp);
	}
	
	//This method is used to remove a sprite component from the game
	public void Remove(SpriteComponent spriteComp)
	{
		components.Remove(spriteComp);
	}
	
	//This method is called every frame. It is used to update the graphics
	public void UpdateGraphics()
	{
		components.Update();
	}
	
	//This method is called every frame. It can be overridden with game logic
	public void Update()
	{
		
	}
	
	//This method is called if a sprite wishes to force a reorder (e.g. it's z order has changed) in the ComponentBatch
	public void postSpriteReorder()
	{
		components.Reorder();
	}
}

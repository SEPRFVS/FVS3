package com.fvs.taxe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ComponentBatch {
	//This array list stores all the active sprite components of the game
	protected List<SpriteComponent> spriteComponents = new ArrayList<SpriteComponent>();
	
	//This boolean 
	private boolean reorderZ = false;
	public void Reorder()
	{
		//To reorder, we set reorderZ so that the sprites will be reordered next instance
		setReorderZ(true);
	}
	
	//This update method is called at the start of a Game's render() method so that the sprites are correctly reordered before they are first shown
	public void Update()
	{
		//If indicated, sort the components so that zorders work
		if(getReorderZ())
		{
			sort();
			setReorderZ(false);
		}
		//Update each sprite component
		for(SpriteComponent c : spriteComponents)
		{
			c.update();
		}
	}
	
	//This method is used to add a new active sprite component to the game
	public void Add(SpriteComponent newComponent)
	{
		spriteComponents.add(newComponent);
		setReorderZ(true);
	}
	
	//This method is used to remove an active sprite component from the game
	public void Remove(SpriteComponent oldComponent)
	{
		spriteComponents.remove(oldComponent);
	}
	
	//This method is used to draw all of our active sprite components
	public void Draw(SpriteBatch batch)
	{
		//Draw in ascending z order
		int i = 0;
		while (i < spriteComponents.size())
		{
			spriteComponents.get(i).draw(batch);
			i++;
		}
	}
	
	public void sort()
	{
		//Sorting method that reorders our spriteComponents list by z order
		//We let java do our sorting, so we simply create a comparator that allows java to tell which value is greater of 2 sprites z values
		Collections.sort(spriteComponents, new Comparator<SpriteComponent>() {
		        @Override
		        public int compare(SpriteComponent sprite1, SpriteComponent sprite2)
		        {
		        	//The z values of sprite1 and sprite2 are compared. If sprite1's is bigger, then >0 is returned
		        	//If sprite1's and sprite2's are equal, then 0 is returned
		        	//If sprite1's is smaller then <0 is returned
		            return  Integer.valueOf(sprite1.getZ()).compareTo(Integer.valueOf(sprite2.getZ()));
		        }
		    });
	}

	//Getter and setter for reorder Z
	public boolean getReorderZ() {
		return reorderZ;
	}

	public void setReorderZ(boolean reorderZ) {
		this.reorderZ = reorderZ;
	}
	
	//Generic clean up method, simply empties spriteComponents while avoiding concurrent arrays	
	public void cleanup()
	{
		clear();
		spriteComponents = null;
	}
	
	//Clear method used to reset a ComponentBatch. Empties the spriteComponents Array while avoiding concurrent arrays
	public void clear()
	{
		//Clearing like this avoids concurrent arrays
		ArrayList<SpriteComponent> componentsToDrop = new ArrayList<SpriteComponent>();
		for(SpriteComponent s : spriteComponents)
		{
			componentsToDrop.add(s);
		}
		for(SpriteComponent s : componentsToDrop)
		{
			spriteComponents.remove(s);
		}
		componentsToDrop = null;
	}
}

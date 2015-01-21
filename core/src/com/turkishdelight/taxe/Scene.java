package com.turkishdelight.taxe;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Scene implements InputProcessor{
	//This class is a generic scene class that is overridden by various children. It creates a scene framework
	//That allows easy jumping between menus and game
	
	//This object stores the active sprite components of the scene
	ComponentBatch components = new ComponentBatch();
	//We also store any object that is explictly clickable seperately for reference without searching
	ArrayList<Clickable> clickAbleObjects = new ArrayList<Clickable>();
	
	private int mouseX = 0;
	private int mouseY = 0;
	
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
	
	public void removeClickAble(SpriteComponent spriteComp)
	{
		if(clickAbleObjects.contains(spriteComp))
		{
			clickAbleObjects.remove((Clickable)spriteComp);
		}
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
	
	//--------------------------//
	//The following methods are implemented by implementing input processor. Their names and times of calls are pretty self
	//Explanatory, but some methods have have logic coded into them

	@Override
	public boolean keyDown(int keycode) {
		//No Action
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		//No Action
		if(keycode == Keys.ESCAPE)
		{
			//Detect an escape press
			onEscPressed();
		}
		//We update each clickable item with a keystroke
		for(Clickable item : clickAbleObjects)
		{
			item.onKeyPressed(keycode);
		}
		return false;
	}
	
	public void onEscPressed()
	{
		//Generically, if escape is pressed we pop the scene
		Game.popScene();
	}

	@Override
	public boolean keyTyped(char character) {
		//We update each clickable item with the character pressed
		for(Clickable item : clickAbleObjects)
		{
			item.onCharStroke(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//We register this as an onclick start event
		onClickStart(screenX, Game.targetWindowsHeight - screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//We update the mouse position
		mouseX = screenX;
		mouseY = screenY;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		//We update the mouse position
		mouseX = screenX;
		mouseY = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		//No Action
		return false;
	}
	
	@Override
	   public boolean touchUp (int x, int y, int pointer, int button) {
		  //We register this as an onclick end event
		  onClickEnd(x, Game.targetWindowsHeight - y);
	      return false;
	   }
	
	//This method is called when the mouse is clicked up. The location of the mouse click is sent to the method as parameters
	public void onClickEnd(int posX, int posY)
	{
		try
		{
			for(Clickable item : clickAbleObjects)
			{
				//Clicks for pane children are handled by the pane itself
				if(!item.isPaneChild())
				{
					item.clickEnd(posX, posY);
				}
			}
		}
		catch (ConcurrentModificationException e)
		{
			//We catch concurrent exceptions for when we clear the scroll pane
		}
	}
	
	//This method is called when the mouse is clicked down. The location of the mouse click is sent to the method as parameters
	public void onClickStart(int posX, int posY)
	{
		try
		{
			for(Clickable item : clickAbleObjects)
			{
				//Clicks for pane children are handled by the pane itself
				if(!item.isPaneChild())
				{
					item.clickStart(posX, posY);
				}
			}
		}
		catch (ConcurrentModificationException e)
		{
			//We catch concurrent exceptions for when we clear the scroll pane
		}
	}
	
	//Getter methods for the mouse position
	public int getMouseX()
	{
		return mouseX;
	}
	
	public int getMouseY()
	{
		return mouseY;
	}
	
	//This method is called when the scene becomes the currentScene. Useful for scenes that are created and then returned to
	public void onFocusGained()
	{
		//TODO in children
	}

	//This method is called when the scene is no longer the currentScene. Useful for scenes that are created and then returned to
	public void onFocusLost()
	{
		//TODO in children
	}
	
	//This method is used to cull a scene for memory sake
	public void cleanup()
	{
		//Clean the sprite components, then notify the garbage collector
		components.cleanup();
		components = null;
		clickAbleObjects = null;
		try {
			System.gc();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
}

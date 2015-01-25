package com.fvs.taxe;

import java.util.EmptyStackException;
import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fvs.taxe.scenes.MainMenuScene;

public class Game extends ApplicationAdapter {
	//This class is the main class of the game. It is the class called by the LibGDX desktop client, and instantiates the game.
	
	//The spriteBatch batch is a libgdx variable used for graphics
	private SpriteBatch batch;
	
	//These values are used to conform the width and height of the window across both this application and the taxe-desktop application
	public static final int targetWindowsWidth = 1024;
	public static final int targetWindowsHeight = 768;
		
	//The values store the z values of various layers in the game. SpriteComponents are then sorted by the zorder before being drawn
	//To create layering
	//The background is always the lowest layer	
	public static final int backgroundZ = 0;
	//Locations and routes are displayed in the layer above the map
	public static final int locationZ = 1;
	//Trains and obstacles are displayed in the layer above the locations and routes
	public static final int objectsZ = 2;
	//The gui is displayed in the layer above the objects
	public static final int guiZ = 3;
	//The shop is displayed as the top layer
	public static final int shopZ = 3;
	//The goals window is displayed in the same layer
	public static final int goalsZ = 3;
	//The current resources window is displayed in the same layer
	public static final int currentResourcesZ = 3;
	//mainZ is used to define a top layer above every other layer
	public static final int mainZ =5;
	//We store the topScene (the scene that has had other scenes pushed on top of it) in this variable
	public static Scene topScene;
	//We store the currently pushed scene in this variable
	public static Scene currentScene;
	//We store all of our currently pushed scenes in this stack, that we can push to and pop
	public static Stack<Scene> scenes = new Stack<Scene>();
	//This SpriteComponent is used for layering scenes
	private SpriteComponent gray;
	
	//This method is called by default through libgdx. We use it as our base of instantiation
	public void create () {
		System.out.println("Game created");
		batch = new SpriteBatch();
		System.out.println("Setting initial scene");
		//By default we set our scene to the main menu
		setScene(new MainMenuScene());
		//setScene(new GameScene(new Player(), new Player()));
		//Set up a sprite for fading background scenes
		
		//We set up a SpriteComponent gray that we can use to gray out a background scene when multiple scenes are displayed
		gray = new SpriteComponent(null, new Texture("blank.png"), 0);
		gray.setSize(targetWindowsWidth, targetWindowsHeight);
		gray.setColor(Color.GRAY);
		gray.setAlpha(0.5f);

	}

	//This method is used to set a scene that has been pushed as the current top scene
	public static void setPushedScene(Scene newScene)
	{
		//Update the current scene to the new scene and notify it
		currentScene = newScene;
		newScene.onFocusGained();
		Gdx.input.setInputProcessor(newScene);
		System.out.println("Current Scene:" + newScene.getClass().getSimpleName().toString());
	}
	
	//This method is used to set a scene overwritting the scenes stack
	public static void setScene(Scene newScene)
	{
		//Set the top scene and clear the stack
		topScene = newScene;
		setPushedScene(newScene);
		scenes = new Stack<Scene>();
	}
	
	//This method is used to pop a layer down of scenes, dropping the current scene
	public static void popScene()
	{
		try
		{
			//We attempt to drop the current scene and set the currentScene to the next scene in the stack
			currentScene.onFocusLost();
			setPushedScene(scenes.pop());
		}
		catch(EmptyStackException e)
		{
			//If an empty stack exception occurs, we are at the bottom of the stack, so fall back to using the topScene
			setScene(topScene);
		}
	}
	
	//This method variation of popScene allows cleanup, cleaning up a scene as it is popped, emptying it's ComponentBatch
	public static void popScene(boolean cleanup)
	{
		currentScene.cleanup();
		popScene();
	}
	
	//This method is used to push a scene
	public static void pushScene(Scene s)
	{
		//We avoid pushing a scene that is already the top scene of the game
		if(currentScene != topScene)
		{
			scenes.push(currentScene);
		}
		//We then set the scene to the newly pushed scene
		setPushedScene(s);
	}
	
	 
	//LibGdxs default update method is named render, it calls an update method before rendering the game
	@Override
	public void render () {
		//Ensure our components object is correctly set up by updating the scene
		Update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//If the top scene is not the current scene, we try to draw the scene underneath first, followed by a translucent gray panel,
		//Allowing a multiscene layering affect where 1 scene is active, but through that scene's transparent sections it is possible
		//To see the scene below's graphics.
		if(topScene != currentScene)
			{
			//Attempt Draw the scene underneath if we are not using 1 scene
			try
			{
				scenes.peek().Draw(batch);
			}
			catch(EmptyStackException e)
			{
				//We are at the bottom of the stack
				topScene.Draw(batch);
			}
			//Draw a gray rectangle to tint the background
			gray.draw(batch);
		}
		//Draw the current scene
		currentScene.Draw(batch);
		
		batch.end();
	}
	
	//This method updates the scene
	public void Update()
	{
		//The scene's update methods are called
		currentScene.UpdateGraphics();
		currentScene.Update();
	}
}
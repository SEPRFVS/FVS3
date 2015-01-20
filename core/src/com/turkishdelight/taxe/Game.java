package com.turkishdelight.taxe;

import java.util.EmptyStackException;
import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turkishdelight.taxe.scenes.MainMenuScene;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	//These values are used to conform the width and height of the window across both this application and the taxe-desktop application
	public static final int targetWindowsWidth = 1024;
	public static final int targetWindowsHeight = 768;
	
	//The values store the z values of various layers in the game. SpriteComponents are then sorted by the zorder before being drawn
	//To create layering
	//The map is always the lowest layer
	
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
	public static final int mainZ =5;
	public static Scene topScene;
	public static Scene currentScene;
	public static Stack<Scene> scenes = new Stack<Scene>();
	
	public void create () {
		System.out.println("Game created");
		batch = new SpriteBatch();
		System.out.println("Setting initial scene");
		//By default we set out scene to the main menu
		setScene(new MainMenuScene());
		//setScene(new GameScene(new Player(), new Player()));
		//Set up a sprite for fading background scenes
		gray = new SpriteComponent(null, new Texture("blank.png"), 0);
		gray.setSize(targetWindowsWidth, targetWindowsHeight);
		gray.setColor(Color.GRAY);
		gray.setAlpha(0.5f);

	}

	public static void setPushedScene(Scene newScene)
	{
		currentScene = newScene;
		newScene.onFocusGained();
		Gdx.input.setInputProcessor(newScene);
		System.out.println("Current Scene:" + newScene.getClass().getSimpleName().toString());
	}
	
	public static void setScene(Scene newScene)
	{
		topScene = newScene;
		setPushedScene(newScene);
	}
	
	public static void popScene()
	{
		try
		{
			currentScene.onFocusLost();
			setPushedScene(scenes.pop());
		}
		catch(EmptyStackException e)
		{
			//We are at the bottom of the stack
			setScene(topScene);
		}
	}
	
	public static void popScene(boolean cleanup)
	{
		currentScene.cleanup();
		popScene();
	}
	
	public static void pushScene(Scene s)
	{
		if(currentScene != topScene)
		{
			scenes.push(currentScene);
		}
		setPushedScene(s);
	}
	
	 
	//LibGdxs default update method is named render, it calls an update method before rendering the game
	SpriteComponent gray;
	@Override
	public void render () {
		//Ensure our components object is correctly set up
		Update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if(topScene != currentScene)
			{
			//Draw the scene underneath
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
		currentScene.UpdateGraphics();
		currentScene.Update();
	}
}
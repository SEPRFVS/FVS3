package com.turkishdelight.taxe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turkishdelight.taxe.scenes.*;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	private Scene currentScene;
	//These values are used to conform the width and height of the window across both this application and the taxe-desktop application
	public static final int targetWindowsWidth = 1024;
	public static final int targetWindowsHeight = 768;
	
	//The values store the z values of various layers in the game. SpriteComponents are then sorted by the zorder before being drawn
	//To create layering
	//The map is always the lowest layer
	public static Game activeGame;
	
	public static final int mapZ = 0;
	//Locations and routes are displayed in the layer above the map
	public static final int locationZ = 1;
	//Trains and obstacles are displayed in the layer above the locations and routes
	public static final int objectsZ = 2;
	//The gui is displayed in the layer above the objects
	public static final int guiZ = 3;
	//The shop is displayed in the layer above the gui
	public static final int shopZ = 4;
	//The goals window is displayed in the layer above the shop window
	public static final int goalsZ = 5;
	//The current resources window is displayed as the top layer
	public static final int currentResourcesZ = 6;
	
	public void create () {
		activeGame = this;
		batch = new SpriteBatch();
		//By default we set out scene to the main menu
		setLocalScene(new MainMenuScene());
	}

	public void setLocalScene(Scene newScene)
	{
		this.currentScene = newScene;
		Gdx.input.setInputProcessor(newScene);
	}
	
	public static void setScene(Scene newScene)
	{
		activeGame.setLocalScene(newScene);
		System.out.println("Current Scene:" + newScene.getClass().getSimpleName().toString());
	}
	
	
	//LibGdxs default update method is named render, it calls an update method before rendering the game
	@Override
	public void render () {
		//Ensure our components object is correctly set up
		Update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//draw our components object
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

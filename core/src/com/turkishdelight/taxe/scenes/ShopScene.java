package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Scene;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;

public class ShopScene extends Scene {
	
	Texture shopText;
	SpriteComponent shopTrains;
	
	Texture obstaclesText;
	SpriteComponent shopObstacles;
	
	Texture resourcesText;
	SpriteComponent shopResources;
	
	public void onCreate()
	{
		// Create background image for trains shop
		shopText = new Texture("Shop_Trains.png");
		shopTrains = new SpriteComponent(this, shopText, Game.shopZ);
		shopTrains.setPosition(0, 0);
		shopTrains.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(shopTrains);
		// ---------------------
		
		// Create background image for obstacles shop
		obstaclesText = new Texture("Shop_Obstacles.png");
		shopObstacles = new SpriteComponent(this, obstaclesText, Game.shopZ);
		shopObstacles.setPosition(0, 0);
		shopObstacles.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		// ---------------------
		
		// Create background image for resources shop
		resourcesText = new Texture("Shop_Resources.png");
		shopResources = new SpriteComponent(this, resourcesText, Game.shopZ);
		shopResources.setPosition(0, 0);
		shopResources.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		// ---------------------
		
		// Create Train button
		Button trainButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				trainPressed();
			}
		};
		trainButton.setPosition(418, 518);
		trainButton.setSize(180, 70);
		Texture buttonText = new Texture("Clear_Button.png");
		trainButton.setTexture(buttonText);
		Add(trainButton);
		// ---------------------
		
		// Create Obstacle button
		Button obstacleButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				obstaclePressed();
			}
		};
		obstacleButton.setPosition(608, 518);
		obstacleButton.setSize(180, 70);
		obstacleButton.setTexture(buttonText);
		Add(obstacleButton);
		// ---------------------
		
		// Create Resource button
		Button resourceButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				resourcePressed();
			}
		};
		resourceButton.setPosition(798, 518);
		resourceButton.setSize(180, 70);
		resourceButton.setTexture(buttonText);
		Add(resourceButton);
		// ---------------------
		
		// Create Next Go button
		Button nextGoButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				nextGoPressed();
			}
		};
		nextGoButton.setPosition(590, 667);
		nextGoButton.setSize(83, 44);
		nextGoButton.setTexture(buttonText);
		Add(nextGoButton);
		// ---------------------
		
		// Create Shop toolbar button
		Button shopToolbarButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				shopToolbarPressed();
			}
		};
		shopToolbarButton.setPosition(504, 665);
		shopToolbarButton.setSize(55, 55);
		shopToolbarButton.setTexture(buttonText);
		Add(shopToolbarButton);
		// ---------------------
		
		// Create Goals toolbar button
		Button goalsToolbarButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				goalToolbarPressed();
			}
		};
		goalsToolbarButton.setPosition(428, 665);
		goalsToolbarButton.setSize(55, 55);
		goalsToolbarButton.setTexture(buttonText);
		Add(goalsToolbarButton);
		// ---------------------
		
		// Create Resources toolbar button
		Button resourcesToolbarButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				resourceToolbarPressed();
			}
		};
		resourcesToolbarButton.setPosition(352, 665);
		resourcesToolbarButton.setSize(55, 55);
		resourcesToolbarButton.setTexture(buttonText);
		Add(resourcesToolbarButton);
		// ---------------------
	}
	
	public void trainPressed()
	{
		System.out.println("trainPressed");
		
		// Remove obstacles texture from scene
		Remove(shopObstacles);
		
		// Remove resources texture from scene
		Remove(shopResources);
		
		// Add shopObstacles texture to scene
		Add(shopTrains);
		
	}
	
	public void obstaclePressed()
	{
		System.out.println("obstaclePressed");
		
		// Remove trains texture from scene
		Remove(shopTrains);
		
		// Remove resources texture from scene
		Remove(shopResources);
		
		// Add shopObstacles texture to scene
		Add(shopObstacles);
		
	}
	
	public void resourcePressed()
	{
		System.out.println("resourcePressed");
		
		// Remove trains texture from scene
		Remove(shopTrains);
		
		// Remove obstacles texture from scene
		Remove(shopObstacles);
		
		// Add resources texture to scene
		Add(shopResources);
		
	}
	
	public void nextGoPressed()
	{
		System.out.println("nextGoPressed");
	}
	
	public void shopToolbarPressed() 
	{
		System.out.println("shopToolbarPressed");
	}
	
	public void goalToolbarPressed() 
	{
		System.out.println("goalToolbarPressed");
	}
	
	public void resourceToolbarPressed() 
	{
		System.out.println("resourceToolbarPressed");
	}

}

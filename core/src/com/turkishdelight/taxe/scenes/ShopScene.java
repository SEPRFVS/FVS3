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
	}
	
	public void trainPressed()
	{
		System.out.println("trainPressed");
		
		// Remove shopTrains texture from scene
		Remove(shopObstacles);
		
		// Add shopObstacles texture to scene
		Add(shopTrains);
		
	}
	
	public void obstaclePressed()
	{
		System.out.println("obstaclePressed");
		
		// Remove shopTrains texture from scene
		Remove(shopTrains);
		
		// Add shopObstacles texture to scene
		Add(shopObstacles);
		
	}

}

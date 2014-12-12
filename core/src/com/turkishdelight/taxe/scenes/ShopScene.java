package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Scene;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;

public class ShopScene extends Scene {

	SpriteComponent shop;
	SpriteComponent player1Money;
	SpriteComponent player2Money;
	
	public void onCreate()
	{
		// Create background image for shop
		Texture trainsText = new Texture("Shop_Trains.png");
		shop = new SpriteComponent(this, trainsText, Game.shopZ);
		shop.setPosition(0, 0);
		shop.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(shop);
		// ---------------------
		
		// Create player 1 money icon
		Texture money1Text = new Texture("Money.png");
		player1Money = new SpriteComponent(this, money1Text, Game.shopZ);
		player1Money.setPosition(34, 628);
		player1Money.setSize(37, 27);
		Add(player1Money);
		// ---------------------
		
		// Create player 2 money icon
		Texture money2Text = new Texture("Money.png");
		player2Money = new SpriteComponent(this, money2Text, Game.shopZ);
		player2Money.setPosition(779, 628);
		player2Money.setSize(37, 27);
		Add(player2Money);
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
		
		// Create trains texture and set shop window background to be trains.
		Texture trainsText = new Texture("Shop_Trains.png");
		shop.setTexture(trainsText);
		// ---------------------
		
	}
	
	public void obstaclePressed()
	{
		System.out.println("obstaclePressed");
		
		// Create obstacles texture and set shop window background to be obstacles.
		Texture obstaclesText = new Texture("Shop_Obstacles.png");
		shop.setTexture(obstaclesText);
		// ---------------------
		
	}
	
	public void resourcePressed()
	{
		System.out.println("resourcePressed");
		
		// Create resources texture and set shop window background to be resources.
		Texture resourcesText = new Texture("Shop_Resources.png");
		shop.setTexture(resourcesText);
		// ---------------------
		
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

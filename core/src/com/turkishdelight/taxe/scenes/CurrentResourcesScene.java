package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Label;

public class CurrentResourcesScene extends Scene {
	
	SpriteComponent currentResources;
	
	SpriteComponent player1Money;
	SpriteComponent player1Fuel;
	SpriteComponent activePlayer1Indicator;
	Label player1NameLabel;
	Label player1ScoreLabel;
	Label player1MoneyLabel;
	Label player1FuelLabel;
	
	SpriteComponent player2Money;
	SpriteComponent player2Fuel;
	SpriteComponent activePlayer2Indicator;
	Label player2NameLabel;
	Label player2ScoreLabel;
	Label player2MoneyLabel;
	Label player2FuelLabel;
	
	public void onCreate()
	{
		// Create background image for goals
		Texture trainsText = new Texture("Current_Resources_Trains.png");
		currentResources = new SpriteComponent(this, trainsText, Game.currentResourcesZ);
		currentResources.setPosition(0, 0);
		currentResources.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(currentResources);
		// ---------------------
		
		// Draw player 1 info on create
		drawPlayer1Info();
				
		// Draw player 2 info on create
		drawPlayer2Info();
				
		// Draw window buttons (Current, obstacles and trains) on create
		drawWindowButtons();
				
		// Draw toolbar buttons on create
		drawToolbarButtons();
		
	}
	
	
	public void drawPlayer1Info()
	{
		// Create player 1 name label
		Texture player1LabelText = new Texture("Clear_Button.png");
		player1NameLabel = new Label(this, player1LabelText, Label.genericFont(Color.BLUE, 40), Game.currentResourcesZ);
		player1NameLabel.setText("PLAYER 1");
		player1NameLabel.setPosition(36, 730);
		player1NameLabel.setAlignment(0);
		Add(player1NameLabel);
		// ---------------------
		
		// Create player 1 active indicator icon
		Texture player1ActiveText = new Texture("Active_Player1_Indicator.png");
		activePlayer1Indicator = new SpriteComponent(this, player1ActiveText, Game.currentResourcesZ);
		activePlayer1Indicator.setPosition(17, 707);
		activePlayer1Indicator.setSize(10, 20);
		activePlayer1Indicator.setAlpha(1);
		Add(activePlayer1Indicator);
		// ---------------------
		
		// Create player 1 score label
		player1ScoreLabel = new Label(this, player1LabelText, Label.genericFont(Color.BLUE, 20), Game.currentResourcesZ);
		player1ScoreLabel.setText("Score: 12345");
		player1ScoreLabel.setPosition(36, 683);
		player1ScoreLabel.setAlignment(0);
		Add(player1ScoreLabel);
		// ---------------------
		
		// Create player 1 money icon
		Texture money1Text = new Texture("Money.png");
		player1Money = new SpriteComponent(this, money1Text, Game.currentResourcesZ);
		player1Money.setPosition(34, 628);
		player1Money.setSize(37, 27);
		player1Money.setAlpha(1);
		Add(player1Money);
		// ---------------------
		
		// Create player 1 money label
		player1MoneyLabel = new Label(this, player1LabelText, Label.genericFont(Color.MAROON, 20), Game.currentResourcesZ);
		player1MoneyLabel.setText("35353");
		player1MoneyLabel.setPosition(player1Money.getX() + player1Money.getWidth() + 8, 648);
		player1MoneyLabel.setAlignment(0);
		Add(player1MoneyLabel);
		// ---------------------
		
		// Create player 1 fuel icon
		Texture fuel1Text = new Texture("Fuel.png");
		player1Fuel = new SpriteComponent(this, fuel1Text, Game.currentResourcesZ);
		TextBounds player1MoneyLabelBounds = player1MoneyLabel.getFont().getBounds(player1MoneyLabel.getText());
		player1Fuel.setPosition(player1MoneyLabel.getX() + player1MoneyLabelBounds.width + 30, 630);
		player1Fuel.setSize(20, 25);
		player1Fuel.setAlpha(1);
		Add(player1Fuel);
		// ---------------------
		
		// Create player 1 fuel label
		player1FuelLabel = new Label(this, player1LabelText, Label.genericFont(Color.TEAL, 20), Game.currentResourcesZ);
		player1FuelLabel.setText("67543");
		player1FuelLabel.setPosition(player1Fuel.getX() + player1Fuel.getWidth() + 8, 648);
		player1FuelLabel.setAlignment(0);
		Add(player1FuelLabel);
		// ---------------------
	}
	
	public void drawPlayer2Info()
	{
		// Create player 2 name label
		Texture player2LabelText = new Texture("Clear_Button.png");
		player2NameLabel = new Label(this, player2LabelText, Label.genericFont(Color.LIGHT_GRAY, 40), Game.currentResourcesZ);
		player2NameLabel.setText("PLAYER 2");
		player2NameLabel.setPosition(985, 730);
		player2NameLabel.setAlignment(2);
		player2NameLabel.setAlpha((float) 0.4);
		Add(player2NameLabel);
		// ---------------------
		
		// Create player 2 active indicator icon
		Texture player2ActiveText = new Texture("Active_Player2_Indicator.png");
		activePlayer2Indicator = new SpriteComponent(this, player2ActiveText, Game.currentResourcesZ);
		activePlayer2Indicator.setPosition(995, 707);
		activePlayer2Indicator.setSize(10, 20);
		activePlayer2Indicator.setAlpha((float) 0.4);
		Add(activePlayer2Indicator);
		// ---------------------
		
		// Create player 2 score label
		player2ScoreLabel = new Label(this, player2LabelText, Label.genericFont(Color.LIGHT_GRAY, 20), Game.currentResourcesZ);
		player2ScoreLabel.setText("Score: 54321");
		player2ScoreLabel.setPosition(985, 683);
		player2ScoreLabel.setAlignment(2);
		Add(player2ScoreLabel);
		// ---------------------
		
		// Create player 2 fuel label
		player2FuelLabel = new Label(this, player2LabelText, Label.genericFont(Color.LIGHT_GRAY, 20), Game.currentResourcesZ);
		player2FuelLabel.setText("67543");
		player2FuelLabel.setPosition(985, 648);
		player2FuelLabel.setAlignment(2);
		Add(player2FuelLabel);
		// ---------------------
		
		// Create player 2 fuel icon
		Texture fuel2Text = new Texture("Fuel.png");
		player2Fuel = new SpriteComponent(this, fuel2Text, Game.currentResourcesZ);
		player2Fuel.setSize(20, 25);
		TextBounds player2FuelLabelBounds = player2FuelLabel.getFont().getBounds(player2FuelLabel.getText());
		player2Fuel.setPosition(player2FuelLabel.getX() - player2FuelLabelBounds.width - player2Fuel.getWidth() - 8, 630);
		player2Fuel.setAlpha((float) 0.4);
		Add(player2Fuel);
		// ---------------------
		
		// Create player 2 money label
		player2MoneyLabel = new Label(this, player2LabelText, Label.genericFont(Color.LIGHT_GRAY, 20), Game.currentResourcesZ);
		player2MoneyLabel.setText("98452");
		int x = (int) player2Fuel.getX() - 30;
		player2MoneyLabel.setPosition(x, 648);
		player2MoneyLabel.setAlignment(2);
		Add(player2MoneyLabel);
		// ---------------------
		
		// Create player 2 money icon
		Texture money2Text = new Texture("Money.png");
		player2Money = new SpriteComponent(this, money2Text, Game.currentResourcesZ);
		player2Money.setSize(37, 27);
		TextBounds player2MoneyLabelBounds = player2MoneyLabel.getFont().getBounds(player2MoneyLabel.getText());
		player2Money.setPosition(player2MoneyLabel.getX() - player2MoneyLabelBounds.width - player2Money.getWidth() - 8, 628);
		player2Money.setAlpha((float) 0.4);
		Add(player2Money);
		// ---------------------
	}
	
	public void drawWindowButtons()
	{

		Texture buttonText = new Texture("Clear_Button.png");
		
		// Create trains button
		Button trainsButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				trainsPressed();
			}
		};
		trainsButton.setPosition(608, 518);
		trainsButton.setSize(180, 70);
		trainsButton.setTexture(buttonText);
		Add(trainsButton);
		// ---------------------
		
		// Create obstacles button
		Button obstaclesButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				obstaclesPressed();
			}
		};
		obstaclesButton.setPosition(798, 518);
		obstaclesButton.setSize(180, 70);
		obstaclesButton.setTexture(buttonText);
		Add(obstaclesButton);
		// ---------------------
	}
	
	public void drawToolbarButtons()
	{
		Texture buttonText = new Texture("Clear_Button.png");
		
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
		
		// Create goals toolbar button
		Button goalsToolbarButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				goalsToolbarPressed();
			}
		};
		goalsToolbarButton.setPosition(428, 665);
		goalsToolbarButton.setSize(55, 55);
		goalsToolbarButton.setTexture(buttonText);
		Add(goalsToolbarButton);
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
		
		// Create obstacless toolbar button
		Button resourcesToolbarButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				resourcesToolbarPressed();
			}
		};
		resourcesToolbarButton.setPosition(352, 665);
		resourcesToolbarButton.setSize(55, 55);
		resourcesToolbarButton.setTexture(buttonText);
		Add(resourcesToolbarButton);
		// ---------------------
	}

	public void trainsPressed()
	{
		System.out.println("trainsPressed");
		
		// Create trainss texture and set goals window background to be trainss.
		Texture trainsText = new Texture("Current_Resources_Trains.png");
		currentResources.setTexture(trainsText);
		// ---------------------
		
	}
	
	public void obstaclesPressed()
	{
		System.out.println("obstaclesPressed");
		
		// Create obstacles texture and set current resources window background to be for obstacles tab.
		Texture obstaclesText = new Texture("Current_Resources_Obstacles.png");
		currentResources.setTexture(obstaclesText);
		// ---------------------
		
	}
	
	boolean player1Go = true;
	
	public void nextGoPressed()
	{
		System.out.println("nextGoPressed");
		
		if (player1Go) {	
			player2Active(); 
		}
		else {
			player1Active(); 
		}
		
		player1Go = !player1Go;
	}
	
	public void goalsToolbarPressed() 
	{
		System.out.println("goalsToolbarPressed");
		
		Game.setScene(new GoalsScene());
	}
	
	public void shopToolbarPressed() 
	{
		System.out.println("shopToolbarPressed");
		
		Game.setScene(new ShopScene());
	}
	
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		
	}
	
	public void player1Active()
	{
		player1NameLabel.getFont().setColor(Color.BLUE);
		player1ScoreLabel.getFont().setColor(Color.BLUE);
		player1MoneyLabel.getFont().setColor(Color.MAROON);
		player1FuelLabel.getFont().setColor(Color.TEAL);
		player1Money.setAlpha(1);
		player1Fuel.setAlpha(1);
		activePlayer1Indicator.setAlpha(1);
		
		player2NameLabel.getFont().setColor(Color.LIGHT_GRAY);
		player2ScoreLabel.getFont().setColor(Color.LIGHT_GRAY);
		player2MoneyLabel.getFont().setColor(Color.LIGHT_GRAY);
		player2FuelLabel.getFont().setColor(Color.LIGHT_GRAY);
		player2Money.setAlpha((float) 0.4);
		player2Fuel.setAlpha((float) 0.4);
		activePlayer2Indicator.setAlpha((float) 0.4);
	}
	
	public void player2Active()
	{
		player1NameLabel.getFont().setColor(Color.LIGHT_GRAY);
		player1ScoreLabel.getFont().setColor(Color.LIGHT_GRAY);
		player1MoneyLabel.getFont().setColor(Color.LIGHT_GRAY);
		player1FuelLabel.getFont().setColor(Color.LIGHT_GRAY);
		player1Money.setAlpha((float) 0.4);
		player1Fuel.setAlpha((float) 0.4);
		activePlayer1Indicator.setAlpha((float) 0.4);
		
		player2NameLabel.getFont().setColor(Color.BLUE);
		player2ScoreLabel.getFont().setColor(Color.BLUE);
		player2MoneyLabel.getFont().setColor(Color.MAROON);
		player2FuelLabel.getFont().setColor(Color.TEAL);
		player2Money.setAlpha(1);
		player2Fuel.setAlpha(1);
		activePlayer2Indicator.setAlpha(1);
	}

}

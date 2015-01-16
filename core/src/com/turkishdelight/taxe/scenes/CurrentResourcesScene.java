package com.turkishdelight.taxe.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;

public class CurrentResourcesScene extends GameWindowedGUIScene {
	
	
	public CurrentResourcesScene(GameScene parent, Player p1, Player p2) {
		super(parent, p1, p2);
	}

	SpriteComponent currentResources;
	
	@Override
	public void drawGUIBackground()
	{
		// Create background image for goals
		Texture trainsText = new Texture("Current_Resources_Trains.png");
		currentResources = new SpriteComponent(this, trainsText, Game.currentResourcesZ);
		currentResources.setPosition(0, 0);
		currentResources.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(currentResources);
		
	}
	
	@Override
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
		
		//Create Exit Button
				Button exitButton = new Button(this) {
					@Override
					public void onClickEnd()
					{
						Game.popScene();
						Game.pushScene(parentGame);
					}
				};
				exitButton.setPosition(975, 582);
				exitButton.setSize(23, 17);
				Texture exitButtonText = new Texture("shopExitButton.png");
				exitButton.setTexture(exitButtonText);
				Add(exitButton);
			
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
	
	@Override
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		//Do nothing
	}
}

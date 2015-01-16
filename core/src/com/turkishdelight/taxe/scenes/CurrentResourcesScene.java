package com.turkishdelight.taxe.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Pane;
import com.turkishdelight.taxe.guiobjects.Scroller;

public class CurrentResourcesScene extends GameWindowedGUIScene {
	
	
	public CurrentResourcesScene(GameScene parent, Player p1, Player p2) {
		super(parent, p1, p2);
	}

	SpriteComponent currentResources;
	SpriteComponent scrollPaneBackground;
	
	SpriteComponent steamInUse;
	SpriteComponent dieselInUse;
	SpriteComponent electricInUse;
	SpriteComponent nuclearInUse;
	SpriteComponent magLevInUse;
	SpriteComponent kingInUse;
	
	@Override
	public void drawGUIBackground()
	{
		// Create background image for goals
		Texture trainsText = new Texture("Current_Resources_Trains.png");
		currentResources = new SpriteComponent(this, trainsText, Game.currentResourcesZ);
		currentResources.setPosition(0, 0);
		currentResources.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(currentResources);
		
		drawCurrentResourcesScrollpane();
	}
	
	public void drawCurrentResourcesScrollpane()
	{
		final Pane pane = new Pane(this, -1);
		pane.setSize(922, 800);
		pane.setPosition(50, 485 - pane.getHeight());
		Add(pane);
		
		Texture scrollPaneBackgroundText = new Texture("Trains_Scrollpane_Background.png");
		scrollPaneBackground = new SpriteComponent(this, scrollPaneBackgroundText, Game.guiZ);
		scrollPaneBackground.setSize(922,800);
		scrollPaneBackground.setLocalPosition(0, 0);
		pane.Add(scrollPaneBackground);
		
		//Min Y is the position the pane must be in to show it's lowest content
		//Max Y is the position the pane must be in to show it's highest content
		final float minY = 73;
		final float maxY = 485 - pane.getHeight();
		
		Texture scrollerText = new Texture("Scroller.png");
		final Scroller scrollPane = new Scroller(this, scrollerText, Game.guiZ) {
					@Override
					public void onMove(float percentage)
					{
						pane.setY(((maxY - minY) * percentage) + minY);
					}
		};
		scrollPane.setOrientation(false);
		scrollPane.setSize(15, 30);
		scrollPane.setRange(74, 454);
		scrollPane.setPosition(35, 454);
		Add(scrollPane);
		
		Texture inUseText = new Texture("in-use.png");

		// Create Steam In Use sprite
		steamInUse = new SpriteComponent(this, inUseText, Game.guiZ);
		steamInUse.setLocalPosition(65, 555);
		steamInUse.setSize(115,34);
		steamInUse.setAlpha(1);
		pane.Add(steamInUse);
		// ---------------------
		
		// Create Diesel In Use sprite
		dieselInUse = new SpriteComponent(this, inUseText, Game.guiZ);
		dieselInUse.setLocalPosition(530, 555);
		dieselInUse.setSize(115,34);
		dieselInUse.setAlpha(1);
		pane.Add(dieselInUse);
		// ---------------------
		
		// Create Electric In Use sprite
		electricInUse = new SpriteComponent(this, inUseText, Game.guiZ);
		electricInUse.setLocalPosition(65, 305);
		electricInUse.setSize(115,34);
		electricInUse.setAlpha(1);
		pane.Add(electricInUse);
		// ---------------------
		
		// Create Nuclear In Use sprite
		nuclearInUse = new SpriteComponent(this, inUseText, Game.guiZ);
		nuclearInUse.setLocalPosition(530, 305);
		nuclearInUse.setSize(115,34);
		nuclearInUse.setAlpha(1);
		pane.Add(nuclearInUse);
		// ---------------------
		
		// Create MagLev In Use sprite
		magLevInUse = new SpriteComponent(this, inUseText, Game.guiZ);
		magLevInUse.setLocalPosition(65, 45);
		magLevInUse.setSize(115,34);
		magLevInUse.setAlpha(1);
		pane.Add(magLevInUse);
		// ---------------------
		
		// Create King In Use sprite
		kingInUse = new SpriteComponent(this, inUseText, Game.guiZ);
		kingInUse.setLocalPosition(530, 45);
		kingInUse.setSize(115,34);
		kingInUse.setAlpha(1);
		pane.Add(kingInUse);
		// ---------------------

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
	}
	
	public void trainsPressed()
	{
		System.out.println("trainsPressed");
		
		// Create trainss texture and set goals window background to be trainss.
		Texture trainsText = new Texture("Current_Resources_Trains.png");
		currentResources.setTexture(trainsText);
		// ---------------------
		
		// Create trains scrollpane background texture and assign to the scrollpane
		Texture scrollPaneBackgroundText = new Texture("Trains_Scrollpane_Background.png");
		scrollPaneBackground.setTexture(scrollPaneBackgroundText);
		// ---------------------
		
		// Set in use sprites to be visible
		steamInUse.setAlpha(1);
		dieselInUse.setAlpha(1);
		electricInUse.setAlpha(1);
		nuclearInUse.setAlpha(1);
		magLevInUse.setAlpha(1);
		kingInUse.setAlpha(1);
		// ---------------------
		
	}
	
	public void obstaclesPressed()
	{
		System.out.println("obstaclesPressed");
		
		// Create obstacles texture and set current resources window background to be for obstacles tab.
		Texture obstaclesText = new Texture("Current_Resources_Obstacles.png");
		currentResources.setTexture(obstaclesText);
		// ---------------------
		
		// Create obstacles scrollpane background texture and assign to the scrollpane
		Texture scrollPaneBackgroundText = new Texture("Obstacles_Scrollpane_Background.png");
		scrollPaneBackground.setTexture(scrollPaneBackgroundText);
		// ---------------------
		
		// Set in use sprites to be transparent
		steamInUse.setAlpha(0);
		dieselInUse.setAlpha(0);
		electricInUse.setAlpha(0);
		nuclearInUse.setAlpha(0);
		magLevInUse.setAlpha(0);
		kingInUse.setAlpha(0);
		// ---------------------
		
	}
	
	@Override
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		//Do nothing
	}
}

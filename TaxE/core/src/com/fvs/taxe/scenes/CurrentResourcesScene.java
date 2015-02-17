package com.fvs.taxe.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.SpriteComponent;
import com.fvs.taxe.guiobjects.Button;
import com.fvs.taxe.guiobjects.Pane;
import com.fvs.taxe.guiobjects.Scroller;

public class CurrentResourcesScene extends GameWindowedGUIScene {
	
	Pane pane;
	
	public CurrentResourcesScene(GameScene parent, Player p1, Player p2) {
		super(parent, p1, p2);
	}

	SpriteComponent currentResources;
	SpriteComponent scrollPaneBackground;
	
	SpriteComponent steamUsage;
	SpriteComponent dieselUsage;
	SpriteComponent electricUsage;
	SpriteComponent nuclearUsage;
	SpriteComponent magLevUsage;
	SpriteComponent kingUsage;
	
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
		pane = new Pane(this, -1);
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
		
		drawTrainButtons();

	}
	
	private void drawTrainButtons() {
		Texture inUseText = new Texture("in-use.png");
		Texture availableText = new Texture("available.png");
		//Texture readyText = new Texture("ready.png");

		// Create Steam Usage sprite
		if(parentGame.activePlayer().hasTrain("Steam")) {
			steamUsage = new SpriteComponent(this, inUseText, Game.guiZ);
		} else {
			steamUsage = new SpriteComponent(this, availableText, Game.guiZ);
		}
		steamUsage.setLocalPosition(65, 555);
		steamUsage.setSize(115,34);
		steamUsage.setAlpha(1);
		pane.Add(steamUsage);
		// ---------------------
		
		// Create Diesel In Use sprite
		if(parentGame.activePlayer().hasTrain("Diesel")) {
			dieselUsage = new SpriteComponent(this, inUseText, Game.guiZ);
		} else {
			dieselUsage = new SpriteComponent(this, availableText, Game.guiZ);
		}
		dieselUsage.setLocalPosition(530, 555);
		dieselUsage.setSize(115,34);
		dieselUsage.setAlpha(1);
		pane.Add(dieselUsage);
		// ---------------------
		
		// Create Electric In Use sprite
		if(parentGame.activePlayer().hasTrain("Electric")) {
			electricUsage = new SpriteComponent(this, inUseText, Game.guiZ);
		} else {
			electricUsage = new SpriteComponent(this, availableText, Game.guiZ);
		}
		electricUsage.setLocalPosition(65, 305);
		electricUsage.setSize(115,34);
		electricUsage.setAlpha(1);
		pane.Add(electricUsage);
		// ---------------------
		
		// Create Nuclear In Use sprite
		if(parentGame.activePlayer().hasTrain("Nuclear")) {
			nuclearUsage = new SpriteComponent(this, inUseText, Game.guiZ);
		} else {
			nuclearUsage = new SpriteComponent(this, availableText, Game.guiZ);
		}
		nuclearUsage.setLocalPosition(530, 305);
		nuclearUsage.setSize(115,34);
		nuclearUsage.setAlpha(1);
		pane.Add(nuclearUsage);
		// ---------------------
		
		// Create MagLev In Use sprite
		if(parentGame.activePlayer().hasTrain("Mag")) {
			magLevUsage = new SpriteComponent(this, inUseText, Game.guiZ);
		} else {
			magLevUsage = new SpriteComponent(this, availableText, Game.guiZ);
		}
		magLevUsage.setLocalPosition(65, 45);
		magLevUsage.setSize(115,34);
		magLevUsage.setAlpha(1);
		pane.Add(magLevUsage);
		// ---------------------
		
		// Create King In Use sprite
		if(parentGame.activePlayer().hasTrain("TheKing")) {
			kingUsage = new SpriteComponent(this, inUseText, Game.guiZ);
		} else {
			kingUsage = new SpriteComponent(this, availableText, Game.guiZ);
		}
		kingUsage.setLocalPosition(530, 45);
		kingUsage.setSize(115,34);
		kingUsage.setAlpha(1);
		pane.Add(kingUsage);
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
		
		// Create trains scrollpane background texture and assign to the scrollpane
		Texture scrollPaneBackgroundText = new Texture("Trains_Scrollpane_Background.png");
		scrollPaneBackground.setTexture(scrollPaneBackgroundText);
		// ---------------------
		
		// Set in use sprites to be visible
		steamUsage.setAlpha(1);
		dieselUsage.setAlpha(1);
		electricUsage.setAlpha(1);
		nuclearUsage.setAlpha(1);
		magLevUsage.setAlpha(1);
		kingUsage.setAlpha(1);
		// ---------------------
		
	}
	
	@Override
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		//Do nothing
	}
	
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		drawTrainButtons();
	}
}

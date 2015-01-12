package com.turkishdelight.taxe.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;

public class GoalsScene extends GameWindowedGUIScene {
	
	SpriteComponent goals;
	
	public GoalsScene(GameScene parent, Player p1, Player p2)
	{
		super(parent, p1, p2);
	}
	
	@Override
	public void drawGUIBackground()
	{
		// Create background image for goals
		Texture currentText = new Texture("Goals_Current.png");
		goals = new SpriteComponent(this, currentText, Game.backgroundZ);
		goals.setPosition(0, 0);
		goals.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(goals);
	}
	
	@Override
	public void drawWindowButtons()

	{
		// Create current button
		Button currentButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				currentPressed();
			}
		};
		currentButton.setPosition(418, 518);
		currentButton.setSize(180, 70);
		Texture buttonText = new Texture("Clear_Button.png");
		currentButton.setTexture(buttonText);
		Add(currentButton);
		// ---------------------
		
		// Create complete button
		Button completeButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				completePressed();
			}
		};
		completeButton.setPosition(608, 518);
		completeButton.setSize(180, 70);
		completeButton.setTexture(buttonText);
		Add(completeButton);
		// ---------------------
		
		// Create incomplete button
		Button incompleteButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				incompletePressed();
			}
		};
		incompleteButton.setPosition(798, 518);
		incompleteButton.setSize(180, 70);
		incompleteButton.setTexture(buttonText);
		Add(incompleteButton);
		// ---------------------
	}
	
	
	public void currentPressed()
	{
		System.out.println("currentPressed");
		
		// Create current texture and set goals window background to be current.
		Texture currentText = new Texture("Goals_Current.png");
		goals.setTexture(currentText);
		// ---------------------
		
	}
	
	public void completePressed()
	{
		System.out.println("completePressed");
		
		// Create completes texture and set goals window background to be completes.
		Texture completeText = new Texture("Goals_Incomplete.png");
		goals.setTexture(completeText);
		// ---------------------
		
	}
	
	public void incompletePressed()
	{
		System.out.println("incompletePressed");
		
		// Create incompletes texture and set goals window background to be incompletes.
		Texture incompletesText = new Texture("Goals_Complete.png");
		goals.setTexture(incompletesText);
		// ---------------------	
	}
	
	@Override
	public void goalsToolbarPressed() 
	{
		System.out.println("goalsToolbarPressed");
		//DO Nothing
	}
}

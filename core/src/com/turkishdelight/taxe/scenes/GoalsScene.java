package com.turkishdelight.taxe.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.Pane;
import com.turkishdelight.taxe.guiobjects.Scroller;

public class GoalsScene extends GameWindowedGUIScene {
	
	SpriteComponent goals;
	SpriteComponent scrollPaneBackground;
	Pane pane;
	
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
		
		drawScrollpane();
	}
	
	public void drawScrollpane()
	{
		pane = new Pane(this, -1);
		pane.setSize(922, 800);
		pane.setPosition(50, 485 - pane.getHeight());
		Add(pane);
		
		Texture scrollPaneBackgroundText = new Texture("Obstacles_Scrollpane_Background.png");
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
		
		drawCurrentGoals();
	}
	
	public void drawCurrentGoals()
	{
		// Loop through number of current goals in array and display in same number of labels displayed at correct y Coord
		int yCoord = 770;
		for (int i = 0; i < 8; i++)
		{
			// Main Objective label
			Texture labelText = new Texture("Clear_Button.png");
			Label mainObjectiveLabel = new Label(this, labelText, Label.genericFont(Color.BLACK, 30), Game.goalsZ);
			mainObjectiveLabel.setText("Send a train from London to Rome through Paris");
			mainObjectiveLabel.setLocalPosition(20, yCoord);
			mainObjectiveLabel.setAlignment(0);
			pane.Add(mainObjectiveLabel);
			// ---------------------
			
			// Main Objective Points Label
			Label mainObjectivePointsLabel = new Label(this, labelText, Label.genericFont(Color.BLUE, 30), Game.goalsZ);
			mainObjectivePointsLabel.setText("100pts");
			mainObjectivePointsLabel.setLocalPosition(700, yCoord);
			mainObjectivePointsLabel.setAlignment(0);
			pane.Add(mainObjectivePointsLabel);
			// ---------------------
			
			// Main Objective Money Label
			Label mainObjectiveMoneyLabel = new Label(this, labelText, Label.genericFont(Color.MAROON, 30), Game.goalsZ);
			mainObjectiveMoneyLabel.setText("100cr");
			mainObjectiveMoneyLabel.setLocalPosition(850, yCoord);
			mainObjectiveMoneyLabel.setAlignment(0);
			pane.Add(mainObjectiveMoneyLabel);
			// ---------------------
			
			// Second Objective Label
			Label secondObjectiveLabel = new Label(this, labelText, Label.genericFont(Color.GRAY, 20), Game.goalsZ);
			secondObjectiveLabel.setText("Train must be Diesel");
			secondObjectiveLabel.setLocalPosition(20, yCoord - 35);
			secondObjectiveLabel.setAlignment(0);
			pane.Add(secondObjectiveLabel);
			// ---------------------
			
			// Second Objective Points Label
			Label secondObjectivePointsLabel = new Label(this, labelText, Label.genericFont(Color.BLUE, 20), Game.goalsZ);
			secondObjectivePointsLabel.setText("+30pts");
			secondObjectivePointsLabel.setLocalPosition(700, yCoord - 35);
			secondObjectivePointsLabel.setAlignment(0);
			pane.Add(secondObjectivePointsLabel);
			// ---------------------
			
			// Second Objective Money Label
			Label secondObjectiveMoneyLabel = new Label(this, labelText, Label.genericFont(Color.MAROON, 20), Game.goalsZ);
			secondObjectiveMoneyLabel.setText("+20cr");
			secondObjectiveMoneyLabel.setLocalPosition(850, yCoord - 35);
			secondObjectiveMoneyLabel.setAlignment(0);
			pane.Add(secondObjectiveMoneyLabel);
			// ---------------------
			
			// Third Objective Label
			Label thirdObjectiveLabel = new Label(this, labelText, Label.genericFont(Color.GRAY, 20), Game.goalsZ);
			thirdObjectiveLabel.setText("Train must have 4 carriages");
			thirdObjectiveLabel.setLocalPosition(20, yCoord - 60);
			thirdObjectiveLabel.setAlignment(0);
			pane.Add(thirdObjectiveLabel);
			// ---------------------
			
			// Third Objective Points Label
			Label thirdObjectivePointsLabel = new Label(this, labelText, Label.genericFont(Color.BLUE, 20), Game.goalsZ);
			thirdObjectivePointsLabel.setText("+40pts");
			thirdObjectivePointsLabel.setLocalPosition(700, yCoord - 60);
			thirdObjectivePointsLabel.setAlignment(0);
			pane.Add(thirdObjectivePointsLabel);
			// ---------------------
			
			// Third Objective Money Label
			Label thirdObjectiveMoneyLabel = new Label(this, labelText, Label.genericFont(Color.MAROON, 20), Game.goalsZ);
			thirdObjectiveMoneyLabel.setText("+40cr");
			thirdObjectiveMoneyLabel.setLocalPosition(850, yCoord - 60);
			thirdObjectiveMoneyLabel.setAlignment(0);
			pane.Add(thirdObjectiveMoneyLabel);
			// ---------------------
			
			// Update Y Coordinate
			yCoord = yCoord - 100;
		}
		// ---------------------
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

package com.turkishdelight.taxe.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.Pane;
import com.turkishdelight.taxe.guiobjects.Scroller;

public class LeaderboardScene extends Scene {
	
	Pane pane;
	
	@Override
	public void onCreate()
	{
		// Create background image for shop
		Texture backgroundText = new Texture("Leaderboard.png");
		SpriteComponent leaderboard = new SpriteComponent(this, backgroundText, Game.backgroundZ);
		leaderboard.setPosition(0, 0);
		leaderboard.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(leaderboard);
		// ---------------------
		
		// Create button to return to the main menu.
		Button mainMenuButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.setScene(new MainMenuScene());
			}
		};
		mainMenuButton.setPosition(870, 660);		
		Add(mainMenuButton);
		// ---------------------
		
		drawScrollpane();
	}
	
	public void drawScrollpane()
	{
		pane = new Pane(this, -1);
		pane.setSize(922, 800);
		pane.setPosition(50, 485 - pane.getHeight());
		Add(pane);
		
		Texture scrollPaneBackgroundText = new Texture("Obstacles_Scrollpane_Background.png");
		SpriteComponent scrollPaneBackground = new SpriteComponent(this, scrollPaneBackgroundText, Game.guiZ);
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
		
		drawLeaderboardData();
		
	}
	
	public void drawLeaderboardData()
	{
		// Loop through number of current games in leaderboard array and display in same number of labels displayed at correct y Coord
		int yCoord = 790;
		for (int i = 0; i < 8; i++)
		{
			// Rank label
			Texture labelText = new Texture("Clear_Button.png");
			Label rankLabel = new Label(this, labelText, Label.genericFont(Color.BLACK, 30), Game.guiZ);
			String rank = Integer.toString(i+1);
			rankLabel.setText(rank);
			rankLabel.setLocalPosition(55, yCoord);
			rankLabel.setAlignment(0);
			pane.Add(rankLabel);
			// ---------------------
			
			// Player Name Label
			Label nameLabel = new Label(this, labelText, Label.genericFont(Color.BLACK, 30), Game.guiZ);
			nameLabel.setText("Player Name");
			nameLabel.setLocalPosition(250, yCoord);
			nameLabel.setAlignment(1);
			pane.Add(nameLabel);
			// ---------------------
			
			// Points Label
			Label pointsLabel = new Label(this, labelText, Label.genericFont(Color.BLACK, 30), Game.guiZ);
			pointsLabel.setText("53433");
			pointsLabel.setLocalPosition(460, yCoord);
			pointsLabel.setAlignment(1);
			pane.Add(pointsLabel);
			// ---------------------
			
			// Difficulty Label
			Label difficultyLabel = new Label(this, labelText, Label.genericFont(Color.BLACK, 30), Game.guiZ);
			difficultyLabel.setText("Easy");
			difficultyLabel.setLocalPosition(635, yCoord);
			difficultyLabel.setAlignment(1);
			pane.Add(difficultyLabel);
			// ---------------------
			
			// Date Label
			Label dateLabel = new Label(this, labelText, Label.genericFont(Color.BLACK, 30), Game.guiZ);
			dateLabel.setText("16/01/2015");
			dateLabel.setLocalPosition(820, yCoord);
			dateLabel.setAlignment(1);
			pane.Add(dateLabel);
			// ---------------------
			
			// Update Y Coordinate
			yCoord = yCoord - 50;
		}
		// ---------------------
	}

}

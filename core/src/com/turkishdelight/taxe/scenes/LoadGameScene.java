package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.guiobjects.Button;

public class LoadGameScene extends Scene {
	
	@Override
	public void onCreate()
	{
		//Set up a button that changes scene (to the main game scene)
		Button beginGameButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.setScene(new GameScene(new Player(), new Player()));
			}
		};
		//Position the button
		beginGameButton.setPosition(Game.targetWindowsWidth / 2, Game.targetWindowsHeight / 2);		
		//Add the button
		Add(beginGameButton);
		//Set up a button that takes the player back to the 
		Button mainMenuButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.setScene(new MainMenuScene());
			}
		};
		//Position the button
		mainMenuButton.setPosition(Game.targetWindowsWidth / 2, Game.targetWindowsHeight / 4);		
		//Add the button
		Add(mainMenuButton);
	}
}

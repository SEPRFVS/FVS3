package com.fvs.taxe.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Game;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;
import com.fvs.taxe.guiobjects.Button;

public class MainMenuScene extends Scene {
	SpriteComponent bkg;
	@Override
	public void onCreate()
	{	
		//Create background image game menu
		Texture bkgText = new Texture("main.jpg");
		bkg = new SpriteComponent(this, bkgText, Game.mainZ);
		bkg.setPosition(0, 0);
		bkg.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(bkg);
		
		//Create new game button
		Button newGameButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.setScene(new NewGameScene());
				cleanup();
			}
		};
		//Position new game button
		newGameButton.setPosition(632, 443);
		newGameButton.setSize(302, 78);		
		Add(newGameButton);
		
		//Create load game button
		Button loadGameButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.pushScene(new DialogueScene("Coming Soon"));
				cleanup();
			}
		};
		//Position load game button
		loadGameButton.setPosition(631, 354);
		loadGameButton.setSize(317, 75);		
		Add(loadGameButton);
		
		//Create leaderboard button
		Button viewLeaderboardButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.pushScene(new DialogueScene("Coming Soon!"));
				cleanup();
			}
		};
		//Position view leaderboard button
		viewLeaderboardButton.setPosition(619, 229);
		viewLeaderboardButton.setSize(340, 112);		
		Add(viewLeaderboardButton);
		
		//Create new game scene
		Button exitGameButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				System.exit(0);
			}
		};
		//Position view leaderboard button
		exitGameButton.setPosition(641, 137);
		exitGameButton.setSize(295, 73);		
		Add(exitGameButton);
		
	}
}

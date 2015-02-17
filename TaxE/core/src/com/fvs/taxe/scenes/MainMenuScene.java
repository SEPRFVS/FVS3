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
		Texture bkgText = new Texture("main.png");
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
		newGameButton.setPosition(560, 280);
		newGameButton.setSize(404, 90);		
		Add(newGameButton);
		
		//Create exit game scene
		Button exitGameButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				System.exit(0);
			}
		};
		//Position view exit button
		exitGameButton.setPosition(680, 150);
		exitGameButton.setSize(160, 90);		
		Add(exitGameButton);
		
	}
}

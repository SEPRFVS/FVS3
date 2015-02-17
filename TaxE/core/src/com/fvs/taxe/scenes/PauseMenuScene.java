package com.fvs.taxe.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Game;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;
import com.fvs.taxe.guiobjects.Button;

public class PauseMenuScene extends Scene {
	SpriteComponent pauseMenu;	
	GameScene parentGame;
	public PauseMenuScene(GameScene gameScene) {
		super();
		parentGame = gameScene;
	}

	@Override
	public void onCreate()
	{	// Create background image for pausemenu
		Texture pauseMenuText = new Texture("MapPause.png");
		pauseMenu = new SpriteComponent(this, pauseMenuText, Game.backgroundZ);
		pauseMenu.setPosition(0, 0);
		pauseMenu.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(pauseMenu);
		// ---------------------
	drawPauseMenu();
	}
	
	public void drawPauseMenu(){
	Button resumeButton = new Button(this) {
		@Override
		public void onClickEnd()
		{
			resumePressed();
		}
	};
	resumeButton.setPosition(370, 378);
	resumeButton.setSize(280, 70);
	Texture resumeText = new Texture("Clear_Button.png");
	resumeButton.setTexture(resumeText);
	Add(resumeButton);
	
	Button saveButton = new Button(this) {
		@Override
		public void onClickEnd()
		{
			savePressed();
		}
	};
	saveButton.setPosition(370, 308);
	saveButton.setSize(280, 70);
	Texture saveText = new Texture("Clear_Button.png");
	saveButton.setTexture(saveText);
	Add(saveButton);
	
	Button quitButton = new Button(this) {
		@Override
		public void onClickEnd()
		{
			quitPressed();
		}
	};
	quitButton.setPosition(370, 238);
	quitButton.setSize(280, 70);
	Texture quitText = new Texture("Clear_Button.png");
	quitButton.setTexture(quitText);
	Add(quitButton);
	}
	public void resumePressed()
	{
		Game.popScene();
		
	}
	public void savePressed()
	{
		Game.pushScene(new DialogueScene("Coming Soon!"));
	}
	public void quitPressed()
	{
		Game.popScene();
		parentGame.cleanup();
		Game.setScene(new MainMenuScene());
	}
	

}

	

package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;

public class GameWindowedGUIScene extends GameGUIScene{
	
	public GameScene parentGame;
	
	public GameWindowedGUIScene(GameScene parent, Player p1, Player p2) {
		super(p1, p2, true, parent);
		
	}
	
	@Override
	public void onCreate(Player p1, Player p2, boolean hasWindow, Scene parent)
	{
		parentGame = (GameScene)parent;
		player1 = p1;
		player2 = p2;
		initialiseGUI(hasWindow);
	}
	
	@Override
	public void onFocusGained()
	{
		super.onFocusGained();
		this.player1Go = parentGame.player1Go;
		if(this.player1Go)
		{
			player1Active();
		}
		else
		{
			player2Active();
		}
	}
	
	@Override
	public void nextGoPressed()
	{
		System.out.println("nextGoPressed");
	}

	@Override
	public void goalsToolbarPressed() 
	{
		System.out.println("goalsToolbarPressed");
		Game.pushScene(parentGame.makeDialogueScene("Goals coming soon!"));
	}
	
	@Override
	public void shopToolbarPressed() 
	{
		System.out.println("shopToolbarPressed");
		//Switch to shop scene
		Game.popScene();
		Game.pushScene(parentGame.shopScene);
	}
	
	@Override
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		Game.popScene();
		Game.pushScene(parentGame.resourceScene);
	}
}

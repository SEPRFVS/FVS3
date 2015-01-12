package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;

public class GameWindowedGUIScene extends GameGUIScene{
	
	GameScene parentGame;
	
	public GameWindowedGUIScene(GameScene parent, Player p1, Player p2) {
		super(p1, p2, true);
		parentGame = parent;
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
		
		if (player1Go) {	
			player2Active(); 
			parentGame.player2Active();
		}
		else {
			player1Active(); 
			parentGame.player1Active();
		}
		parentGame.nextTurn();
		
		player1Go = !player1Go;
	}

	@Override
	public void goalsToolbarPressed() 
	{
		System.out.println("goalsToolbarPressed");
		Game.popScene();
		Game.pushScene(parentGame.goalsScene);
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

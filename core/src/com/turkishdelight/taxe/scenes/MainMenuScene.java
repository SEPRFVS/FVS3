package com.turkishdelight.taxe.scenes;

import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.guiobjects.Button;

public class MainMenuScene extends Scene {
	
	@Override
	public void onCreate()
	{
		//Set up a button that changes scene (to the new game scene)
		Button b = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.setScene(new ShopScene());
			}
		};
		//Position the button
		b.setPosition(Game.targetWindowsWidth / 2, Game.targetWindowsHeight / 2);		
		//Add the button
		Add(b);
		
	}
}

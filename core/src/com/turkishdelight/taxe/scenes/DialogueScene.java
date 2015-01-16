package com.turkishdelight.taxe.scenes;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Label;


public class DialogueScene extends Scene {
	SpriteComponent dialogue;
	Label dialLabel;
	String text;
	
	// seems to currently require seperate setText method to set the text correctly, as OnCreate called before text assignment
	public DialogueScene(String text)
	{
		super();
		this.text = text;
		System.out.println(this.text);
	}
	
	@Override
	public void onCreate()
	{	// Create background image for dialogue
		Texture dialText = new Texture("dialogueBox.png");
		dialogue = new SpriteComponent(this, dialText, Game.backgroundZ);
		dialogue.setPosition(380, 400);
		dialogue.setSize(Game.targetWindowsWidth/4, Game.targetWindowsHeight/5);
		Add(dialogue);
		drawDialogueButtons();
	}
	
	public void drawDialogueButtons()
	{
		//Create Dialogue Button
		Button exitButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				//System.out.println(Game.scenes.pop().getClass().getSimpleName());
				Game.popScene();
				
			}
		};
		exitButton.setPosition(605, 525);
		exitButton.setSize(30, 26);
		Texture exitButtonText = new Texture("shopExitButton.png");
		exitButton.setTexture(exitButtonText);
		Add(exitButton);
		
		Button okayButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.popScene();
				
			}
		};
		okayButton.setPosition(463, 423);
		okayButton.setSize(90, 33);
		Texture okayButtonText = new Texture("Clear_Button.png");
		okayButton.setTexture(okayButtonText);
		Add(okayButton);
		
		Texture dialLabelText  = new Texture("Clear_Button.png");
		dialLabel = new Label(this, dialLabelText, Label.genericFont(Color.LIGHT_GRAY, 30), Game.goalsZ);
		dialLabel.setText(text);
		dialLabel.setPosition(508, 519);
		dialLabel.setAlignment(1);
		dialLabel.setAlpha((float) 0.4);
		Add(dialLabel);
	}
	
	public void setText(String labelText){
		dialLabel.setText(this.text = labelText);
	}
	
	}
	
		
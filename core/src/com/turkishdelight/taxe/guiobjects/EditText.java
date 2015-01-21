package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.turkishdelight.taxe.Scene;

public class EditText extends LabelButton {
	//This class extends the label button such that it can be clicked to enable setting of the text
	
	//This boolean stores whether the EditText is active
	private boolean focus = false;
	
	public EditText(Scene parentScene, Texture targText, BitmapFont font, int z) {
		super(parentScene);
		this.setTexture(targText);
		this.setFont(font);
		this.setZ(z);
	}
	
	//We override onClickEnd such that the editText gains focus
	@Override
	public void onClickEnd()
	{
		focus = true;
		this.setFont(Label.genericFont(Color.GREEN, 40));
		//Clear the editText
		this.setText("");
	}
	
	//We override clickEnd so that any click outside of the edit text drops focus
	@Override
	public boolean clickEnd(int x, int y)
	{
		//Any click drops focus
		focus = false;
		this.setFont(Label.genericFont(Color.GRAY, 40));
		return super.clickEnd(x, y);
	}
	
	//We monitor for char strokes while the edit text is active, and add these to the text
	@Override
	public void onCharStroke(char ch)
	{
		if(focus)
		{
			this.setText(this.getText().trim());
			if(this.getText().length() < 10)
			{
				this.setText(this.getText() + ch);
			}
		}
	}
	
	//We also monitor for the back key being pressed, and use this to delete text if the EditText is active
	@Override
	public void onKeyPressed(int keycode)
	{
		if(keycode == Keys.BACKSPACE && focus && this.getText().length() > 1)
		{
			this.setText(this.getText().substring(0, this.getText().length() - 2));
		}
	}
	

}


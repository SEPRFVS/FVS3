package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.turkishdelight.taxe.Scene;

public class EditText extends LabelButton {
	private boolean focus = false;
	public EditText(Scene parentScene, Texture targText, BitmapFont font, int z) {
		super(parentScene);
		this.setTexture(targText);
		this.setFont(font);
		this.setZ(z);
	}
	
	@Override
	public void onClickEnd()
	{
		focus = true;
	}
	
	@Override
	public boolean clickEnd(int x, int y)
	{
		focus = false;
		return super.clickEnd(x, y);
	}
	
	@Override
	public void onCharStroke(char ch)
	{
		this.setText(this.getText().trim());
		if(focus)
		{
			if(this.getText().length() < 10)
			{
				this.setText(this.getText() + ch);
			}
		}
	}
	
	@Override
	public void onKeyPressed(int keycode)
	{
		if(keycode == Keys.BACKSPACE && focus && this.getText().length() > 1)
		{
			this.setText(this.getText().substring(0, this.getText().length() - 2));
		}
	}
	

}


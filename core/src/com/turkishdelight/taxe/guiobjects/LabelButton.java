package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;

public class LabelButton extends Button {
	//This method extends the button object so that is can have text on top of the texture
	
	//We store the font, text and text alignment as private variables
	private BitmapFont font;
	private String text;
	private int alignment = 1;
	
	public LabelButton(Scene parentScene) {
		super(parentScene);
		this.font = Label.genericFont(Color.BLACK, 30);
	}
	
	public LabelButton(Scene parentScene, Texture targText, int width, int height, BitmapFont font) {
		super(parentScene, targText, width, height, Game.guiZ);
		this.font = font;
	}
	
	public LabelButton(Scene parentScene, Texture targText, int width, int height, int z, BitmapFont font) {
		super(parentScene, targText, width, height, z);
		this.font = font;
	}
	
	//This method returns the label's font
	public BitmapFont getFont()
	{
		return font;
	}
	
	//This method sets the label's font
	public void setFont(BitmapFont newFont)
	{
		font = newFont;
	}
	
	//This method returns the label's text
	public String getText()
	{
		return text;
	}
	
	//This method sets the label's text
	public void setText(String newText)
	{
		text = newText;
	}
	
	//This method returns the alignment of the label's text
	public int getAlignment()
	{
		return alignment;
	}
	
	//This method sets the alignment of the label's text
	public void setAlignment(int newAlignment)
	{
		// 0 = left aligned, 1 = center aligned, 2 = right aligned
		alignment = newAlignment; 
	}
	
	//We override the default draw method so that we draw the text *after* we draw the object's texture 
	//So that the text appears on top
	@Override
	public void draw(Batch batch)
	{
		//We draw the texture first, then the text
		super.draw(batch);
		
		float originX = 0;
		float originY = this.getY();
		TextBounds bounds = font.getBounds(text);
		
		// Left Alignment
		if (alignment == 0) {
			originX = this.getX();
		}
		// Centre aligned
		else if (alignment == 1) {
			originX = this.getX() - (bounds.width / 2);
		}
		// Right aligned
		else if (alignment == 2) {
			originX = this.getX() - bounds.width;
		}

		font.draw(batch, text, originX, originY);
	}

}

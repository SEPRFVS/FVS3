package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;

public class Label extends SpriteComponent {
	//This class is used to create sprites with text on top of them, allowing us to add
	//Labels to the GUI and game
	
	//We store the font, text, and text alignment of the label as private variables
	private BitmapFont font;
	private String text;
	private int alignment = 1;
	
	public Label(Scene parentScene, Texture targText, BitmapFont font) {
		super(parentScene, targText, Game.shopZ);
		this.font = font;
	}
	public Label(Scene parentScene, Texture targText, BitmapFont font, int z) {
		super(parentScene, targText, z);
		this.font = font;
	}
	//This method creates a new font object of a specific color and size for our default font file GOST.ttf
	public static BitmapFont genericFont(Color fontColor, int fontSize)
	{
		//We use GOST.ttf
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GOST.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		
		//This size of the font is set and the font is generated
		parameter.size = fontSize;
		BitmapFont font = generator.generateFont(parameter);
		
		//Set the color of the font and return is
		font.setColor(fontColor);
		generator.dispose();
		return font;
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
		// Draw the texture first, then the text
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


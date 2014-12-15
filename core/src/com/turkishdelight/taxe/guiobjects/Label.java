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
	BitmapFont font;
	private String text;
	int alignment;
	
	public Label(Scene parentScene, Texture targText, BitmapFont font) {
		super(parentScene, targText, Game.shopZ);
		this.font = font;
	}
	
	public static BitmapFont genericFont(Color fontColor, int fontSize)
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GOST.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = fontSize;
		BitmapFont font30 = generator.generateFont(parameter);
		font30.setColor(fontColor);
		generator.dispose();
		return font30;
	}
	
	public BitmapFont getFont()
	{
		return font;
	}
	
	public void setFont(BitmapFont newFont)
	{
		font = newFont;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String newText)
	{
		text = newText;
	}
	
	public int getAlignment()
	{
		return alignment;
	}
	
	public void setAlignment(int newAlignment)
	{
		// 0 = left aligned, 1 = center aligned, 2 = right aligned
		alignment = newAlignment; 
	}
	
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


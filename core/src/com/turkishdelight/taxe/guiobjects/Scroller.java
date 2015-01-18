package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Scene;

public class Scroller extends Clickable {
	private float minVal = 0;
	private float maxVal = 0;
	private int prevMX = 0;
	private int prevMY = 0;
	private boolean xOrientation = false;
	private boolean clickDown = false;
	
	public Scroller(Scene parentScene, Texture text, int z) {
		super(parentScene, text, z);
	}
	
	public void setRange(int min, int max)
	{
		minVal = min;
		maxVal = max;
	}
	
	public float getMinVal()
	{
		return minVal;
	}
	
	public float getMaxVal()
	{
		return maxVal;
	}
	
	//This method sets the orientation of the scroller. True moves it along the x axis, false moves it along the y
	public void setOrientation(boolean xO)
	{
		xOrientation = xO;
	}
	
	public boolean getOrientation()
	{
		return xOrientation;
	}
	
	@Override
	public void onClickStart()
	{
		clickDown = true;
		prevMX = getParentScene().getMouseX();
		prevMY = getParentScene().getMouseY();
		System.out.println("clickStart!");
	}
	
	@Override
	public boolean clickEnd(int x, int y)
	{
		clickDown = false;
		return super.clickEnd(x, y);
	}
	
	//This method is called every tick. If the scroller is clicked down, we calculate the displacement in the necessary direction of
	//The mouse, and then update the scroller's position and call onMove()
	@Override
	public void update()
	{
		if(clickDown)
		{
			if(xOrientation)
			{
				int disX = getParentScene().getMouseX() - prevMX;
				this.setX(this.getX() - disX);
				if(this.getX() < minVal)
				{
					this.setX(minVal);
				}
				if(this.getX() > maxVal)
				{
					this.setX(maxVal);
				}
				onMove((this.getX() - minVal) / (maxVal - minVal));
			}
			if(!xOrientation)
			{
				int disY = getParentScene().getMouseY() - prevMY;
				System.out.println(getParentScene().getMouseY() + " : " + prevMY);
				this.setY(this.getY() - disY);
				if(this.getY() < minVal)
				{
					this.setY(minVal);
				}
				if(this.getY() > maxVal)
				{
					this.setY(maxVal);
				}
				onMove((this.getY() - minVal) / (maxVal - minVal));
			}
			prevMX = getParentScene().getMouseX();
			prevMY = getParentScene().getMouseY();
		}
		
	}
	
	//This method is called when the scroller is moved. It can be overwritten, and sends the percentage of the way the scroller is
	//Between it's minimum and maximum values
	public void onMove(float percent)
	{
		System.out.println("Percent: " + percent);
	}

}

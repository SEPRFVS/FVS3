package com.turkishdelight.taxe.guiobjects;

import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Clickable;
import com.turkishdelight.taxe.Scene;

public class Scroller extends Clickable {
	//This class is a type of clickable that moves between 2 locations by being dragged, allowing the implementation of scrolling objects
	//and panes
	
	//These variables store the minimum and maximum location of the scroller between which it can travel
	private float minVal = 0;
	private float maxVal = 0;
	//These variables store the previous tick's mouse position for comparison purpose.
	private int prevMX = 0;
	private int prevMY = 0;
	//This boolean is used to determine the direction in which the scroller moves. If xOrientation is true, it moves along the X,
	//Otherwise it moves along the Y
	private boolean xOrientation = false;
	private boolean clickDown = false;
	
	public Scroller(Scene parentScene, Texture text, int z) {
		super(parentScene, text, z);
	}
	
	//Getter and setter for range
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
	
	//Getter and setter for xOrientation
	public void setOrientation(boolean xO)
	{
		xOrientation = xO;
	}
	
	public boolean getOrientation()
	{
		return xOrientation;
	}
	
	//We register when the object has been clicked as the start of it's activity.
	@Override
	public void onClickStart()
	{
		clickDown = true;
		//Store the mouse coordinates
		prevMX = getParentScene().getMouseX();
		prevMY = getParentScene().getMouseY();
	}
	
	//Whenever the click ends, irregardless of whether it is on the scroller, we end the activity.
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
		//Check if the scroller is active
		if(clickDown)
		{
			if(xOrientation)
			{
				//If we are moving on the X axis
				//Calculate the mouse displacement
				int disX = getParentScene().getMouseX() - prevMX;
				this.setX(this.getX() - disX);
				//Ensure that the scroller is constrained within it's range
				if(this.getX() < minVal)
				{
					this.setX(minVal);
				}
				if(this.getX() > maxVal)
				{
					this.setX(maxVal);
				}
				//Alert the scroller that there has been movement
				onMove((this.getX() - minVal) / (maxVal - minVal));
			}
			if(!xOrientation)
			{
				//If we are moving on the Y axis
				//Calculate the mouse displacement
				int disY = getParentScene().getMouseY() - prevMY;
				System.out.println(getParentScene().getMouseY() + " : " + prevMY);
				this.setY(this.getY() - disY);
				//Ensure that the scroller is constrained within it's range
				if(this.getY() < minVal)
				{
					this.setY(minVal);
				}
				if(this.getY() > maxVal)
				{
					this.setY(maxVal);
				}
				//Alert the scroller that there has been movement
				onMove((this.getY() - minVal) / (maxVal - minVal));
			}
			//Store the mouse position for the next instance of movement.
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

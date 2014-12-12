package com.turkishdelight.taxe;

import java.util.ArrayList;

import com.turkishdelight.taxe.routing.Train;

public class Player {
	private int money = 0;
	private int score = 0;
	private int fuel = 0;
	private ArrayList<SpriteComponent> possessions = new ArrayList<SpriteComponent>();
	private ArrayList<Train> trains = new ArrayList<Train>(); // used in game scene for collisions
	
	public int getFuel()
	{
		return fuel;
	}
	
	public int getMoney()
	{
		return money;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setFuel(int newFuel)
	{
		fuel = newFuel;
	}
	
	public void setMoney(int newMoney)
	{
		money = newMoney;
	}
	
	public void setScore(int newScore)
	{
		score = newScore;
	}
	
	public void updateFuel(int delta)
	{
		fuel += delta;
	}
	
	public void updateMoney(int delta)
	{
		money += delta;
	}
	
	public void updateScore(int delta)
	{
		score += delta;
	}
	
	public void addTrain(SpriteComponent Train)
	{
		possessions.add(Train);
		trains.add((Train) Train);
	}
	
	public ArrayList<Train> getTrains(){
		return trains;
	}
	
	public void updateTurn(boolean activePlayer)
	{
		updateGUI(activePlayer);
		if(activePlayer)
		{
			for(SpriteComponent spr : possessions)
			{
				spr.updateTurn();
			}
		}
	}
	
	public void updateGUI(boolean activePlayer)
	{
		if(activePlayer)
		{
			//Set Gui to show options and colours for this player
		}
		else
		{
			//Set Gui to hide options and set grayscale for this player
		}
	}
}

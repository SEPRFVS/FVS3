package com.turkishdelight.taxe;

import java.util.ArrayList;

import com.turkishdelight.taxe.routing.AiSprite;
import com.turkishdelight.taxe.routing.AiSprite.AIType;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.routing.Train.Type;
import com.turkishdelight.taxe.scenes.GameScene;

public class Player {
	private String name = "Player";
	private String startLocation = "";
	private int money = 0;
	private int score = 0;
	private int fuel = 0;
	private ArrayList<SpriteComponent> possessions = new ArrayList<SpriteComponent>();
	private ArrayList<AiSprite> aiSprites = new ArrayList<AiSprite>(); // used in game scene for collisions

	public String getName()
	{
		return name;
	}
	
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
	
	public String getStartLocation()
	{
		return startLocation;
	}

	public void setName(String newName)
	{
		name = newName;
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
	
	public void setStartLocation(String location)
	{
		startLocation = location;
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

	public void addAiSprite(SpriteComponent aiSprite)
	{
		possessions.add(aiSprite);
		aiSprites.add((AiSprite) aiSprite);
	}

	public ArrayList<AiSprite> getAiSprites(){
		return aiSprites;
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
	
	public boolean buyTrain(Type type, int price, String station, GameScene s)
	{
		if(hasTrain(type.getName()) || getTrainCount() > 2 || this.getMoney() < price)
		{
			return false;
		}
		else
		{
			setMoney(getMoney() - price);
			s.generateTrainAndCarraige(this, s.getStationByName(station), type);
			return true;
		}
	}
	
	public boolean sellTrain(String name, int price, Scene s)
	{
		if(!hasTrain(name))
		{
			return false;
		}
		else
		{
			Train t = getTrain(name);
			possessions.remove(t);
			aiSprites.remove(t);
			s.Remove(t);
			setMoney(getMoney() + price);
			return true;
		}
	}
	
	public boolean hasTrain(String name)
	{
		for(AiSprite item : aiSprites)
		{
			if(item.getAIType() == AIType.TRAIN)
			{
				if(((Train)item).getName().equals(name))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Train getTrain(String name)
	{
		for(AiSprite item : aiSprites)
		{
			if(item.getAIType() == AIType.TRAIN)
			{
				if(((Train)item).getName().equals(name))
				{
					return (Train)item;
				}
			}
		}
		return null;
	}
	
	public int getTrainCount()
	{
		int count = 0;
		for(AiSprite item : aiSprites)
		{
			if(item.getAIType() == AIType.TRAIN)
			{
				count++;
			}
		}
		return count;
		
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

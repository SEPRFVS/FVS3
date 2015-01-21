package com.turkishdelight.taxe;

import java.util.ArrayList;

import com.turkishdelight.taxe.goals.Goal;
import com.turkishdelight.taxe.routing.AiSprite;
import com.turkishdelight.taxe.routing.AiSprite.AIType;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.routing.Train.Type;
import com.turkishdelight.taxe.scenes.GameScene;

public class Player {
	//This class stores information about a specific player. It tracks their belongings, goals, and values
	
	//This variable stores the player's name. It is set when the player is created or loaded
	private String name = "Player";
	//This variable stores the player's start location. It is set when the player is created or loaded
	private String startLocation = "";
	//This variable stores the player's money. It is set when the player is created or loaded
	private int money = 0;
	//This variable stores the player's score. It is set when the player is created or loaded
	private int score = 0;
	//This variable stores the player's fuel. It is set when the player is created or loaded
	private int fuel = 0;
	//When a player is loaded into a game, we update this GameScene variable to track which GameScene we are working with
	private GameScene activeGame;
	//We use an arraylist of AiSprites to track the belongings of the player
	public ArrayList<AiSprite> aiSprites = new ArrayList<AiSprite>();
	//These 2 arrays track the completed and failed goals of the player for the purpose of displaying in the GUI
	public ArrayList<Goal> completeGoals = new ArrayList<Goal>();
	public ArrayList<Goal> failedGoals = new ArrayList<Goal>();
	
	//Setter and getter methods for variables
	public void setActiveGame(GameScene game)
	{
		activeGame = game;
	}

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

	//This method updates the fuel by a specific amount of change, delta
	public void updateFuel(int delta)
	{
		fuel += delta;
	}

	//This method updates the money by a specific amount of change, delta
	public void updateMoney(int delta)
	{
		money += delta;
	}

	//This method updates the score by a specific amount of change, delta
	public void updateScore(int delta)
	{
		score += delta;
	}

	//This method is used to add an aiSprite to the player's possessions externally
	public void addAiSprite(SpriteComponent aiSprite)
	{
		aiSprites.add((AiSprite) aiSprite);
	}

	//This method is used to access the player's possessions externally
	public ArrayList<AiSprite> getAiSprites(){
		return aiSprites;
	}

	//When the turn is updated, we execute the next turn logic of this player's possessions. 
	public void updateTurn(boolean activePlayer)
	{
		//We check if this player is the active player. This may seem redundant but it leaves scope for future logic for when it is not the
		//Player's turn (particularly for obstacles and quantifiable objectives)
		if(activePlayer)
		{
			for(SpriteComponent spr : aiSprites)
			{
				spr.updateTurn();
			}
		}
	}
	
	//This method is used to purchase a train for the player, updating their money and adding the train to the game
	public boolean buyTrain(Type type, int price, String station, GameScene s)
	{
		//We do our checks of possession, count (max 3 trains per player!) and price
		if(hasTrain(type.getName()) || getTrainCount() > 2 || this.getMoney() < price)
		{
			return false;
		}
		else
		{
			//If we have passed the checks, we update our money and place the train in the game
			setMoney(getMoney() - price);
			s.generateTrainAndCarriage(this, s.getStationByName(station), type);
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
			aiSprites.remove(t);
			s.Remove(t);
			s.Remove(t.getCarriage());
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

	public void clear()
	{
		//Clearing like this avoids concurrent arrays
		ArrayList<AiSprite> aiToDrop = new ArrayList<AiSprite>();
		for(AiSprite s : aiSprites)
		{
			aiToDrop.add(s);
			activeGame.Remove(s);
		}
		for(AiSprite s : aiToDrop)
		{
			aiSprites.remove(s);
		}
		aiToDrop = null;
	}
}

package com.fvs.taxe.goals;

import java.util.ArrayList;

import com.fvs.taxe.Player;

public class Objective {
//This class acts as a base class from which we can create Absolute and Quantifiable objectives in the game	
	
//This array list stores the players who have completed this objective
private ArrayList<Player> completePlayers = new ArrayList<Player>();
//When a player completes an objective they are rewarded it's value
private int moneyReward = 0;
private int scoreReward = 0;
//Score reward TODO with score implementation
protected String goalText = "";
//Track the objective's progress through the event log with this variable
private int eventIndex = 0;

public Objective(int money, int score, String goalText)
{
	this.moneyReward = money;
	this.scoreReward = score;
	this.setGoalText(goalText);
}

//Generate method, overridden by different children
public static Objective generate()
{
	return new Objective(0, 0, "Default objective instance");
}

//Override toString method
public String toString()
{
	return goalText;
}

//getter and setter for moneyReward, eventIndex and for goalText

public int getMoneyReward()
{
	return moneyReward;
}

public void setMoneyReward(int value)
{
	moneyReward = value;
}

public void setScoreReward(int value) {
	scoreReward = value;
}

public int getScoreReward() {
	return scoreReward;
}

public String getGoalText() {
	return goalText;
}

public void setGoalText(String goalText) {
	this.goalText = goalText;
}

public int getEventIndex() {
	return eventIndex;
}

public void setEventIndex(int eventIndex) {
	this.eventIndex = eventIndex;
}

//This method checks whether a certain player has completed this objective
public boolean isComplete(Player player)
{
	for(Player p : completePlayers)
	{
		if(p.equals(player))
		{
			return true;
		}
	}
	return false;
}

//This method registers a new player as having completed the objective
void registerCompletePlayer(Player pl)
{
	completePlayers.add(pl);
}

public boolean checkComplete(Player player, EventHandler eventLog)
{
	//Safety check for if the player has already completed this objective
	if(isComplete(player))
	{
		return true;
	}
	//TODO for specific children of this class
	if(fillsCompleteCriteria(player, eventLog))
	{
		registerCompletePlayer(player);
		return true;
	}
	else
	{
		return false;
	}
}

//This method is the generic method that gets overridden in children to determine whether a player has completed this objective
public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog)
{
	if(pl.getMoney() != -1)
	{
		return true;
	}
	return false;
}
}

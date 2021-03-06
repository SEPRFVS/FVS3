package com.fvs.taxe.goals.objectives;

import com.fvs.taxe.Player;
import com.fvs.taxe.goals.EventHandler;

//This class is a subchild of objective. It simply acts as a blank empty objective
public class EmptyObjective extends Objective {
	public EmptyObjective() {
		setMoneyReward(0);
		setScoreReward(0);
		setGoalText("No Objective");
	}

	//Override toString method
	public String toString()
	{
		return this.getGoalText();
	}
	
	//Always fails the success check
	@Override
	public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog)
	{
		return false;
	}
}

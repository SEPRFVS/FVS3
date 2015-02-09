package com.fvs.taxe.goals;

import com.fvs.taxe.Player;

public class EmptyObjective extends Objective {
	//This class is a subchild of objective. It simply acts as a blank empty objective

	public EmptyObjective() {
		setMoneyReward(0);
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

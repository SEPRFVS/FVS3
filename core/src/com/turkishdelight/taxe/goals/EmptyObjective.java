package com.turkishdelight.taxe.goals;

import com.turkishdelight.taxe.Player;

public class EmptyObjective extends Objective {

	public static Objective generate()
	{
		return new EmptyObjective();
	}
	
	public EmptyObjective() {
		super(0, "No Objective");
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

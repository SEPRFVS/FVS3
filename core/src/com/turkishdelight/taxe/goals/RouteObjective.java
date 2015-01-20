package com.turkishdelight.taxe.goals;

import java.util.ArrayList;

import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.routing.AiSprite;
import com.turkishdelight.taxe.routing.Train;

public class RouteObjective extends ArrivalObjective{
	//This variable stores the first station the player must reach for this objective
	private String startStation = "";
	//This array list stores the players who have completed this objective
	private ArrayList<Train> activeTrains = new ArrayList<Train>();
	
	public RouteObjective(int money, String goalText, String startStation, String destination) {
		super(money, goalText, destination);
		this.setStartStation(startStation);
	}
	
	//Override toString method
	public String toString()
	{
		return getGoalText() + startStation + " and " + getDestination();
	}
	
	public static Objective generate()
	{
		String station1 = getRandomStation();
		String station2 = getRandomStation();
		while(station2.equals(station1))
		{
			station2 = getRandomStation();
		}
		return new RouteObjective(400, "Transport a train between ", station1, station2);
	}
	
	//Getters and setters for start station
	public String getStartStation() {
		return startStation;
	}
	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}
	
	public boolean isActive(Train tr)
	{
		for(Train t : activeTrains)
		{
			if(t.equals(tr))
			{
				return true;
			}
		}
		return false;
	}
	
	void registerActiveTrain(Train tr)
	{
		activeTrains.add(tr);
	}
	
	@Override
	public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog)
	{
		ArrayList<AiSprite> playerPossessions = pl.aiSprites;
		for(int i = this.getEventIndex(); i < eventLog.getIndex(); i++)
		{
			Event e = eventLog.getEvent(i);
			//Firstly check to see if it is at the start station
			System.out.println("Checking event " + e.Station + " vs. " + startStation);
			if(e.Station.equals(startStation))
			{
				//We have found a match
				System.out.println("First location reached by " + e.train.getName());
				registerActiveTrain(e.train);
			}
			else
			{
			System.out.println("Checking event " + e.Station + " vs. " + this.getDestination());
			//Check for the train in the playerPossessions to see if the player owns that train
			boolean playerTrain = false;
			for(AiSprite item : playerPossessions)
				{
					if(e.train.equals(item))
					{
						//We have found the train in the player's possessions
						playerTrain = true;
					}
				}
				if(playerTrain)
				{
					//We can now compare the target destination with the even't location. If it is a match, we can say this objective has been completed 
					if(e.Station.equals(getDestination()))
					{
						//We have found a match
						return true;
					}
				}
			}
			
		}
		return false;
	}

}

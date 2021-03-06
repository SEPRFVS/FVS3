package com.fvs.taxe.goals.objectives;

import com.fvs.taxe.Player;
import com.fvs.taxe.goals.Dijkstra;
import com.fvs.taxe.goals.Event;
import com.fvs.taxe.goals.EventHandler;
import com.fvs.taxe.routing.AiSprite;
import com.fvs.taxe.routing.Train;
import com.fvs.taxe.scenes.GameScene;

import java.util.ArrayList;

public class RouteObjective extends ArrivalObjective {
	//This class extends ArrivalObjective, adding a start criteria as well as an end criteria so that
	//The player must pass a train along a route as supposed to sending it to a single point
	
	//This variable stores the first station the player must reach for this objective
	private String startStation = "";
	//This array list stores the players who have completed this objective
	private ArrayList<Train> activeTrains = new ArrayList<Train>();

	public RouteObjective(GameScene parentScene) {
        super(parentScene);

        String station1 = getRandomStation();
		String station2 = getRandomStation();

		while(station2.equals(station1)) {
			station2 = getRandomStation();
		}

		setStartStation(station1);
		setDestination(station2);

		int distance = Dijkstra.calculate(parentScene.getLocations(),parentScene.getStationByName(station1), parentScene.getStationByName(station2)); //Calculate shortest distance
		setMoneyReward(distance);
		setScoreReward(distance + Math.round(((float) distance)*((float) 0.25))); //Apply a multiplier to score as more difficult than just getting to a destination
		setGoalText("Transport a train between ");
	}
	
	//Override toString method
	public String toString()
	{
		return getGoalText() + startStation + " and " + getDestination();
	}
	
	//Getters and setters for start station
	public String getStartStation() {
		return startStation;
	}
	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}
	
	//This method checks if a specific train is marked as active
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
	
	//Register a new train as active
	void registerActiveTrain(Train tr)
	{
		activeTrains.add(tr);
	}
	
	//This method builds upon that of ArrivalObjective, adding in the necessity for a train to have passed through the first location in
	//Order to be eligible to fill the criteria and complete the objective.
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
			else if (isActive(e.train))
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

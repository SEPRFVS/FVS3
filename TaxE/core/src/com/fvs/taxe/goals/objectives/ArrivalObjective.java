package com.fvs.taxe.goals.objectives;

import com.fvs.taxe.Player;
import com.fvs.taxe.goals.Dijkstra;
import com.fvs.taxe.goals.Event;
import com.fvs.taxe.goals.EventHandler;
import com.fvs.taxe.routing.AiSprite;
import com.fvs.taxe.scenes.GameScene;

import java.util.ArrayList;
import java.util.Random;

public class ArrivalObjective extends Objective {
	//This class is a child of objective. It specifically implements the Absolute objective of reaching a location
	
	//This variable stores this Objective's target destination
	private String destination = "";
	
	//Constructor sets destination

	public ArrivalObjective(GameScene parentScene) {
		setGoalText("Transport a train to ");
		String randomStation = getRandomStation();
		int distance;
		if(parentScene.activePlayer().getStartLocation().equals(randomStation)) {
			distance = 200; //Give a standard score if already at station
		} else {
			distance = Dijkstra.calculate(parentScene.getLocations(), parentScene.getStationByName(parentScene.activePlayer().getStartLocation()), parentScene.getStationByName(randomStation)); //Calculate based on stating (base) station
		}
		setMoneyReward(distance);
		setScoreReward(distance);
		setDestination(randomStation);
	}
	
	//Override toString method
	public String toString()
	{
		return getGoalText() + destination;
	}
	
	//This method simply generates a random station name from the list of station
	public static String getRandomStation()
	{
		String[] stations = {"London", "Paris", "Madrid", "Lisbon", "Rome", "Berlin", "Krakow", "Budapest", "Moscow"};
		int idx = new Random().nextInt(stations.length);
		String random = (stations[idx]);
		return random;
	}
	
	//Getter and setter methods for eventIndex and destination
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	//This method is the generic method that gets overridden in children to determine whether a player has completed this objective
	@Override
	public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog)
	{
		ArrayList<AiSprite> playerPossessions = pl.aiSprites;
		for(int i = this.getEventIndex(); i < eventLog.getIndex(); i++)
		{
			Event e = eventLog.getEvent(i);
			System.out.println("Checking event " + e.Station + " vs. " + destination);
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
				if(e.Station.equals(destination))
				{
					//We have found a match
					return true;
				}
			}
			
		}
		return false;
	}
	

}

package com.fvs.taxe.goals.objectives;

import java.util.ArrayList;

import com.fvs.taxe.Player;
import com.fvs.taxe.goals.Dijkstra;
import com.fvs.taxe.goals.Event;
import com.fvs.taxe.goals.EventHandler;
import com.fvs.taxe.routing.AiSprite;
import com.fvs.taxe.routing.Train;
import com.fvs.taxe.scenes.GameScene;

public class ViaObjective extends Objective {
	
	private String viaStation;
	private String startStation; //Store to allow identification of correct train
	//This array list stores the players who have completed this objective
	private ArrayList<Train> activeTrains = new ArrayList<Train>();
	private ArrayList<Train> completeTrains = new ArrayList<Train>();
	
	public ViaObjective(GameScene parentScene, String startStation, String endStation) {
		this.startStation = startStation;
		
		if(startStation == null){
			startStation = parentScene.activePlayer().getStartLocation();
		}
		
		String viaStation = ArrivalObjective.getRandomStation();
		while (viaStation == startStation || viaStation == endStation) {
			viaStation = ArrivalObjective.getRandomStation();
		}
		
		this.viaStation = viaStation;
		
		int distanceWithout = Dijkstra.calculate(parentScene.getLocations(),parentScene.getStationByName(startStation), parentScene.getStationByName(endStation));
		int distanceWith = Dijkstra.calculate(parentScene.getLocations(),parentScene.getStationByName(startStation), parentScene.getStationByName(viaStation)) + Dijkstra.calculate(parentScene.getLocations(),parentScene.getStationByName(viaStation), parentScene.getStationByName(endStation));
		if(distanceWith > distanceWithout) {
			setScoreReward((distanceWith - distanceWithout)*2);
		} else {
			setScoreReward(50);
		}
		
		setMoneyReward(0);
		setGoalText("Travel via ");
	}

	@Override
	public String toString() {
		return getGoalText() + viaStation;
	}
	
	//This method checks if a specific train is marked as active
	public boolean isActive(Train tr) {
		for(Train t : activeTrains) {
			if(t.equals(tr)) {
				return true;
			}
		}
		return false;
	}
		
	//Register a new train as active
	void registerActiveTrain(Train tr) {
		activeTrains.add(tr);
	}

	@Override
	public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog) {
		System.out.println("Checking Via");
		ArrayList<AiSprite> playerPossessions = pl.aiSprites;
		for(int i = this.getEventIndex(); i < eventLog.getIndex(); i++)
		{
			Event e = eventLog.getEvent(i);
			//Firstly check to see if it is at the start station
			System.out.println("Checking event " + e.Station + " vs. " + startStation);
			if(e.Station.equals(startStation) || (startStation == null && !isActive(e.train))) {
				//We have found a match
				System.out.println("First location reached by " + e.train.getName());
				registerActiveTrain(e.train);
			} else if (isActive(e.train)) {
				System.out.println("Checking event " + e.Station + " vs. " + viaStation);
				//Check for the train in the playerPossessions to see if the player owns that train
				boolean playerTrain = false;
				for(AiSprite item : playerPossessions) {
					if(e.train.equals(item)) {
						//We have found the train in the player's possessions
						playerTrain = true;
					}
				}
				if(playerTrain)	{
					//We can now compare the target destination with the even't location. If it is a match, we can say this objective has been completed 
					if(e.Station.equals(viaStation)) {
						//We have found a match
						completeTrains.add(e.train);
						System.out.println("Side goal complete");
					}
				}
			}
			//Test if correct train
			for(Train train : completeTrains) {
				if(train == e.train){
					return true;
				}
			}
		}
		return false;
	}

}

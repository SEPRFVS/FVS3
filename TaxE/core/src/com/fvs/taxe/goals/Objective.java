package com.fvs.taxe.goals;

import java.util.ArrayList;

import com.fvs.taxe.Player;

abstract public class Objective {
//This class acts as a base class from which we can create Absolute and Quantifiable objectives in the game	

    //This array list stores the players who have completed this objective
    private ArrayList<Player> completePlayers = new ArrayList<Player>();
    //When a player completes an objective they are rewarded it's value
    private int moneyReward = 0;
    //Score reward TODO with score implementation
    protected String goalText = "";
    //Track the objective's progress through the event log with this variable
    private int eventIndex = 0;

    //Override toString method
    public String toString() {
        return goalText;
    }

    //getter and setter for moneyReward, eventIndex and for goalText
    public int getMoneyReward() {
        return moneyReward;
    }

    public void setMoneyReward(int value) {
        moneyReward = value;
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
    public boolean isComplete(Player player) {
        for (Player p : completePlayers) {
            if (p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    //This method registers a new player as having completed the objective
    void registerCompletePlayer(Player pl) {
        completePlayers.add(pl);
    }

    /**
     * WARNING, this has side effect, first time this method is called and the player has
     * completed the Objective it will call registerCompletePlayer() which will store the
     * player in the complete list
     *
     * @param player
     * @param eventLog
     * @return
     */
    public boolean checkComplete(Player player, EventHandler eventLog) {
        //Safety check for if the player has already completed this objective
        if (isComplete(player)) {
            return true;
        }

        //TODO for specific children of this class
        if (fillsCompleteCriteria(player, eventLog)) {
            registerCompletePlayer(player);
            return true;
        } else {
            return false;
        }
    }

    //This method is the generic method that gets overridden in children to determine whether a player has completed this objective
    public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog) {
        return pl.getMoney() != -1;
    }
}

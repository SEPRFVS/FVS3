package com.fvs.taxe.goals;

import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.goals.objectives.Objective;
import com.fvs.taxe.scenes.GameScene;

public class Goal {
    //The goal class links objectives to the game. Up to 3 objectives make up a goal, and the goal is checked for completion each player turn
    private GameScene parentGame;
    //We store the 3 objectives and the event Index
    public Objective mainObjective;
    public Objective sideObjective1;
    public Objective sideObjective2;
    private int eventIndex = 0;

    public Goal(GameScene parentGame, Objective main, Objective side1, Objective side2) {
        this.parentGame = parentGame;
        mainObjective = main;
        sideObjective1 = side1;
        sideObjective2 = side2;
        eventIndex = parentGame.getEventHandler().getIndex();
    }

    /**
     * This is where most of the work is done for the class.
     * On a player's active turn, that player is sent to each current Goal.
     * We drop a goal from the parentGame when it has by completed by at least 1 player
     * And it is player 2's turn. This gives both players equal chance to complete the goal.
     *
     * @param player the player who's turn it currently is
     * @return has the player completed this obj
     */
    public boolean nextTurn(Player player) {
        //Update objectives' start index
        sideObjective1.setEventIndex(eventIndex);
        sideObjective2.setEventIndex(eventIndex);
        mainObjective.setEventIndex(eventIndex);

        //Update the eventIndex for next turn
        eventIndex = parentGame.getEventHandler().getIndex();

        int reward = 0;

        boolean sideObjOneComplete = sideObjective1.checkComplete(player, parentGame.getEventHandler());

        if (sideObjOneComplete) {
            reward += sideObjective1.getMoneyReward();
        }

        boolean sideObjTwoComplete = sideObjective2.checkComplete(player, parentGame.getEventHandler());

        if (sideObjTwoComplete) {
            reward += sideObjective2.getMoneyReward();
        }

        //Check the main objective. If the player is player 2 and the main
        //objective is complete, we need to work out where to put the Goal in the players' goals as we
        //Drop the goal from the active goal
        boolean complete = mainObjective.checkComplete(player, parentGame.getEventHandler());
        if (!complete) {
            //If the player is player2, we need to check to drop the objective
            if (parentGame.getPlayer2().equals(player)) {
                //If player 2 has failed, we look at player 1. If player 1 has succeeded, drop the Goal from the game's goals and add it to player 2's fails
                if (mainObjective.isComplete(parentGame.getPlayer1())) {
                    System.out.println("Removing goals");
                    parentGame.goalsToDrop.add(this);
                    player.failedGoals.add(this);
                }
            }
            return false;
        } else {
            //We need to register the goal being completed and notify the player
            player.completeGoals.add(this);

            reward += mainObjective.getMoneyReward();
            player.setMoney(player.getMoney() + reward);

            Game.pushScene(parentGame.makeDialogueScene("Goal complete! +" + reward + "cr!"));

            //If player 2 has succeeded then we need to drop the goal
            if (parentGame.getPlayer2().equals(player)) {
                parentGame.goalsToDrop.add(this);
                //We also need to check if player1 has completed it. If not, we need to add the goal to player1's failed goals
                if (!mainObjective.isComplete(parentGame.getPlayer1())) {
                    System.out.println("player1 Fail!");
                    parentGame.getPlayer1().failedGoals.add(this);
                }
            }

            return true;
        }

    }
}

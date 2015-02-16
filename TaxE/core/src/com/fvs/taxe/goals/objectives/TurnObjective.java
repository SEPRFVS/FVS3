package com.fvs.taxe.goals.objectives;

import com.fvs.taxe.Player;
import com.fvs.taxe.goals.EventHandler;
import org.apache.commons.lang3.mutable.MutableInt;

public class TurnObjective extends Objective {
    // score received per turn in objective remaining
    private final int SCORE_MULTIPLIER = 10;

    // turn number when objective was created
    private int startingTurn;
    private MutableInt currentTurn;

    private boolean failed = false;

    // number of turns that this objective must be completed within
    private int turns;

    public TurnObjective(MutableInt currentTurn, int distance) {
        this.currentTurn = currentTurn;
        this.startingTurn = currentTurn.getValue();
        this.turns = distance/20;
        setMoneyReward(50);
    }

    @Override
    public String toString() {
        if (failed) {
            return "Turn side objective failed";
        }

        return "Complete within " + turnsElapsed() +"/"+ turns +" turns";
    }

    @Override
    public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog) {
        return !failed;
    }

    @Override
    public int getScoreReward() {
        if (failed) {
            return 0;
        }

        return turnsRemaining() * SCORE_MULTIPLIER;
    }

    private int turnsElapsed() {
        return currentTurn.getValue() - startingTurn;
    }

    private int turnsRemaining() {
        int turnsSinceCreation = turnsElapsed();
        int turnsRemaining = this.turns - turnsSinceCreation;

        if (turnsRemaining <= 0) {
            failed = true;
            return 0;
        }

        return turnsRemaining;
    }
}

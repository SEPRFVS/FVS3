package com.fvs.taxe.goals.objectives;

import com.fvs.taxe.Player;
import com.fvs.taxe.goals.EventHandler;

public class TurnObjective extends Objective {
    public TurnObjective() {
        setGoalText("Complete within X turns");
    }

    @Override
    public String toString() {
        return getGoalText();
    }

    @Override
    public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog) {
        return false;
    }
}

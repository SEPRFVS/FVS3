package com.fvs.taxe.goals.tests;

import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.goals.EventHandler;
import com.fvs.taxe.goals.Goal;
import com.fvs.taxe.goals.objectives.EmptyObjective;
import com.fvs.taxe.scenes.DialogueScene;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.testrunners.GdxTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class GoalTest {
    private Player player1;
    private Player player2;

    @Before
    public void setUp() throws Exception {
        player1 = new Player();
        player2 = new Player();
    }

    @Test
    public void testScore() {
        GameScene scene = new GameScene(player1, player2);

        int GOAL_MONEY = 50;
        int GOAL_SCORE = 50;

        int moneyBefore = player1.getMoney();
        int scoreBefore = player1.getScore();

        // make sure this objective returns that is is complete when asked
        EmptyObjective o = new EmptyObjective() {
            @Override
            public boolean fillsCompleteCriteria(Player pl, EventHandler eventLog)
            {
                return true;
            }
        };
        o.setMoneyReward(GOAL_MONEY);
        o.setScoreReward(GOAL_SCORE);

        Goal g = new Goal(scene, o, new EmptyObjective(), new EmptyObjective());
        g.nextTurn(player1);

        assertEquals(moneyBefore + GOAL_MONEY, player1.getMoney());
        assertEquals(scoreBefore + GOAL_SCORE, player1.getScore());
    }
    
    @Test
    public void winTest() {
    	//Test that a player can win
    	boolean gameWon = false;
    	
    	GameScene scene = new GameScene(player1, player2);
    	
    	player1.setScore(8000);
    	scene.nextTurn();
    	
    	gameWon = player1.getScore() >= 6000;
    	
    	assertEquals(gameWon, true);
    }
    
    @Test
    public void testTurnResources() {
    	GameScene scene = new GameScene(player1, player2);
    	scene.nextGoPressed(); //Start game
    	
    	int initialFuel = player1.getFuel();
    	int initialMoney = player1.getMoney();
    	scene.nextGoPressed();
    	//Get to player 1 so increment happens
    	while(scene.activePlayer() != player1) {
    		scene.nextGoPressed();
    	}
    	
    	assertEquals(player1.getMoney(), initialMoney + 5);
    	assertEquals(player1.getFuel(), initialFuel + 5);
    }
}
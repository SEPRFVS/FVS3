package com.fvs.taxe.worldobjects.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.worldobjects.Station;

public class StationObstacle extends Obstacle {
    static Texture text = new Texture("station_obstacle.png");
    static int size = 20;
    static public int turns = 10;

    public StationObstacle(GameScene gameScene, Station location) {
        super(gameScene, location, text);
        setSize(size, size);
        location.setObstacleTurns(turns);
    }
}

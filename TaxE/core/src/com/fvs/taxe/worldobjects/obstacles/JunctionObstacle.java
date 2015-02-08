package com.fvs.taxe.worldobjects.obstacles;


import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Scene;
import com.fvs.taxe.worldobjects.Junction;

public class JunctionObstacle extends Obstacle {
    static Texture text = new Texture("junction_obstacle.png");
    static int size = 10;

    public JunctionObstacle(Scene parentScene, Junction junction) {
        super(parentScene, junction, text);
        setSize(size, size);

    }
}

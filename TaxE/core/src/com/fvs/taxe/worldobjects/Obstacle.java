package com.fvs.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Game;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;

public class Obstacle extends SpriteComponent {
    public enum Type {
        JUNCTION,
        CONNECTION
    }

    static int size = 10;
    private Vector2 coords;

    public Obstacle(Scene parentScene, Junction junction){
        super(parentScene, text, Game.objectsZ);
        this.coords = junction.coords;
        setSize(size, size);
        setPosition(coords.x, coords.y);
        junction.setObstacle(this);
    }

    static Texture text = new Texture("junction_obstacle.png");
}

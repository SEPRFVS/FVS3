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

    public Obstacle(Scene parentScene, Vector2 coords){
        super(parentScene, text, Game.objectsZ);
        this.coords = coords;
        setSize(size, size);
        setPosition(coords.x, coords.y);

    }

    static Texture text = new Texture("junction_obstacle.png");
}

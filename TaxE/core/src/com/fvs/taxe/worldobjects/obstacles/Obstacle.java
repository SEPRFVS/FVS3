package com.fvs.taxe.worldobjects.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Game;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;
import com.fvs.taxe.worldobjects.RouteLocation;

public abstract class Obstacle extends SpriteComponent {
    public enum Type {
        JUNCTION,
        STATION
    }

    public Obstacle(Scene parentScene, RouteLocation routeLocation, Texture text){
        super(parentScene, text, Game.objectsZ);
        Vector2 coords = routeLocation.getPosition();
        setPosition(coords.x, coords.y);
        routeLocation.setObstacle(this);
    }
}

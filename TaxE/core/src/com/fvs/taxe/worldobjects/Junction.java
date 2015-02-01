package com.fvs.taxe.worldobjects;

import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.scenes.GameScene;

public class Junction extends RouteLocation {
	static Texture text = new Texture("location.png"); 	//TODO needs changing
    private Obstacle obstacle;
	
	public Junction(final GameScene parentScene, String junctionName, int x, int y) {
		super(parentScene, text, junctionName,  x ,y);
		this.setSize(7, 7);
	}

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public boolean hasObstacle() {
        return obstacle != null;
    }
	
	@Override
	public boolean equals(Object object){
		if (object.getClass() == Junction.class) {
			if (((Junction) object).getName().equals(this.getName()) && ((Junction) object).getPosition().equals(this.getPosition())){
				return true;
			}
		}
		return false;
	}
	
}

package com.fvs.taxe.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.fvs.taxe.guiobjects.Label;
import com.fvs.taxe.guiobjects.LabelButton;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.worldobjects.obstacles.Obstacle;

public class Station extends RouteLocation{

	// Class that is designed to hold information on locations- position and connections- connected locations and route to other locations 
	static Texture text = new Texture("location.png");
	private LabelButton lbutton;
    // -1 when no obstacle
    private int obstacleTurns;
	
	
	public Station(final GameScene parentScene, String locationName, int x, int y) {
		super(parentScene, text, locationName,  x, y);
		this.setSize(10, 10);
		Texture clearButton = new Texture("Clear_Button.png");
		lbutton = new LabelButton(parentScene, clearButton, 40,40, Label.genericFont(Color.BLACK, 20)) {
			@Override
			public void onClickEnd()
			{
				if (selectingRoute && parentScene.getSelectedTrain() != null){
					parentScene.selectLocation(getStationClass());
				}
			}
		};
		lbutton.setPosition(x, y-30);
		lbutton.setText(locationName);
		lbutton.setSize(30, 30);
		parentScene.Add(lbutton);
        obstacleTurns = -1;
		
	}
	
	public void setFont(BitmapFont font){
		lbutton.setFont(font);
	}
	
	public void setText(String newText){
		lbutton.setText(newText);
	}
	
	public Station getStationClass() {
	    return Station.this;
	}
	
	@Override
	public boolean equals(Object object){
		if (object.getClass() == Station.class) {
			if (((Station) object).getName().equals(this.getName()) && ((Station) object).getPosition().equals(this.getPosition())){
				return true;
			}
		}
		return false;
	}

    public void setObstacleTurns(int turns) {
        obstacleTurns = turns;
    }

    public void setObstacle(Obstacle obstacle) {
        // clear previous obstacle before placing a new one
        if (this.obstacle != null) removeObstacle();
        this.obstacle = obstacle;
    }

    public void removeObstacle() {
        parentScene.obstacles.remove(this.getObstacle());
        parentScene.Remove(this.getObstacle());
        this.obstacle = null;
        obstacleTurns = - 1;
    }

    public void decrementObstacleTurns() {
        if (obstacleTurns == -1) return;
        if (obstacleTurns == 0) {
            removeObstacle();
            obstacleTurns = -1;
            return;
        }
        obstacleTurns--;
    }
}
package com.turkishdelight.taxe.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.LabelButton;
import com.turkishdelight.taxe.scenes.GameScene;

public class Station extends RouteLocation{

	// Class that is designed to hold information on locations- position and connections- connected locations and route to other locations 
	static Texture text = new Texture("location.png");
	private LabelButton lbutton;
	
	
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
	
}
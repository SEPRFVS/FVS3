package com.turkishdelight.taxe.scenes;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.LabelButton;

public abstract class SelectionScene extends Scene {
	private static final int FONTSIZE = 35;
	private SpriteComponent selectionBackground;
	private Button confirmButton;
	protected int selectedElementIndex = -1;											// the index ot use in elements/labelbuttons to get the corresponding object
	protected ArrayList<?> elements;													// the elements that will be displayed
	private ArrayList<LabelButton> labelButtons = new ArrayList<LabelButton>();			// arraylist of labelbuttons- index corresponds to same labelbuttons element in elements
	// Generic Scene that displays an arraylist, with each element being selectable

	private Texture backText;
	public SelectionScene(Texture backgroundTexture){
		onCreate(backgroundTexture);
	}
	
	public SelectionScene(Texture backgroundTexture, ArrayList<?> elements) {
		this.elements = elements;
		onCreate(backgroundTexture);
	}
	
	public void onCreate(Texture backText) {
		//Load Background Image
		selectionBackground = new SpriteComponent(this, backText, Game.backgroundZ);
		selectionBackground.setPosition(Game.targetWindowsWidth/2-200, 150);
		Add(selectionBackground);
				
		// confirm button
		Texture clrButton = new Texture("Clear_Button.png");
		confirmButton = new Button(this, clrButton, 140, 40) {
			public void onClickEnd() {
				if (selectedElementIndex>=0){
					onSelectionEnd();
				}
			}
		};
		confirmButton.setPosition(432, 206-40);
		Add(confirmButton);
		
		createLabelButtons();
	}

	public abstract void onSelectionEnd();
	// needs implementing in scene to affect the scene fields

	public void setElements(ArrayList<?> elements){
		// set he elements to display
		this.elements = elements;
		createLabelButtons();
	}
	
	private void createLabelButtons() {
		//Drop existing elements
		if(labelButtons != null)
		{
			ArrayList<LabelButton> buttonsToDrop = new ArrayList<LabelButton>();
			for(LabelButton b : labelButtons)
			{
				buttonsToDrop.add(b);
				Remove(b);
			}
			for(LabelButton b : buttonsToDrop)
			{
				labelButtons.remove(b);
			}
		}
		/// create a labelbutton for each element in elements
		if (elements == null){
			return;
		}
		Texture dialLabelText = new Texture("Clear_Button.png");
		int i = 0;
		for (final Object element: elements){
			LabelButton labelButton = new LabelButton(this, dialLabelText, 200, 30, Game.goalsZ, Label.genericFont(Color.LIGHT_GRAY, FONTSIZE)) {
				public void onClickEnd() {
					elementSelected(element);
				}
			};
			labelButton.setText(element.toString());
			labelButton.setAlignment(1);
			labelButton.setPosition((Game.targetWindowsWidth/2) - 105, 590 - (50*i));
			Add(labelButton);
			labelButtons.add(labelButton);
			i++;
		}
	}

	protected void elementSelected(Object element) {
		// called when element in arraylist selected, show graphical cues
		System.out.println(element.toString());
		if (selectedElementIndex >= 0){
			labelButtons.get(selectedElementIndex).setFont(Label.genericFont(Color.LIGHT_GRAY, FONTSIZE));
		}
		selectedElementIndex = elements.indexOf(element);
		System.out.println(element);
		labelButtons.get(selectedElementIndex).setFont(Label.genericFont(Color.BLUE, FONTSIZE));
		
	}
}

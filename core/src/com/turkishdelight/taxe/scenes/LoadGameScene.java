package com.turkishdelight.taxe.scenes;

import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.LabelButton;


public class LoadGameScene extends Scene {
	
	SpriteComponent loadScene; //Background
	Button mainMenuButton, loadOther, loadGame; // Self explanatory buttons...
	
	
	String locRoot; //Local root set by LibGDX
	String loadFilePath; //Used by the File Chooser to pass path to libGDX FileHandle
	
	// These static variables contain the label currently selected & the save file currently selected
	static LabelButton labelSelected;
	static FileHandle gameToLoad;
	
	// Defining all the game save slots
	LabelButton g1, g2, g3, g4, g5, g6, g7, g8, g9, g10, g11;

	
	@Override
	public void onCreate()
	{
		//Load Background Image
		Texture loadBackground = new Texture("load_game.png");
		loadScene = new SpriteComponent(this, loadBackground, 0);
		loadScene.setPosition(0, 0);
		loadScene.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(loadScene);
		
		//Button to set to main menu
		Texture clrButton = new Texture("Clear_Button.png");
		mainMenuButton = new Button(this, clrButton, 152, 45) {
			public void onClickEnd() {
				Game.popScene();
			}
		};
		mainMenuButton.setPosition(586, 662);
		Add(mainMenuButton);
		
		loadButtons();
		loadFiles();

	}
	
	
	// Creates the load game buttons at the bottom of the screen 
	public void loadButtons()
	{
		//Clear texture for buttons integrated in bg
		Texture clrButton = new Texture("Clear_Button.png");
		
		// Load Other Button Draw
		loadOther = new Button(this, clrButton, 152, 45) {
			//Call module to Load file that doesn't appear in list
			public void onClickEnd() {
				loadFromChooser();
			}
		};
		loadOther.setPosition(281,39);
		Add(loadOther);
		
		// Load Game button draw
		loadGame = new Button(this, clrButton, 152,45) {
			//Pass load file to game and restore it
			public void onClickEnd() {
				loadGame();
			}
		};
		loadGame.setPosition(587,39);
		Add(loadGame);
	}
	
	// Creates labels for the 11 (yes, 11) latest files
	public void loadFiles()
	{
		
		// Fetches a list of files in the 'local' path with the extension .taxe
		// Local path is the one the .jar file is located in
		// Propagates an array
		final FileHandle[] files = Gdx.files.local("/").list(".taxe");
		
		
		// Sort Save Files by Date
		Arrays.sort(files, new Comparator<Object>()
		{
			// Comparator takes the time since file was last modified (libgdx passes as long)
			// Compares it and sorts accordingly (see returns)
		    public int compare(Object o1, Object o2) {
		        if (((FileHandle)o1).lastModified() > ((FileHandle)o2).lastModified()) {
		            return -1;
		        } else if (((FileHandle)o1).lastModified() < ((FileHandle)o2).lastModified()) {
		            return +1;
		        } else {
		            return 0;
		        }
		    }
		}); 
		
		// Set default game to load as most recently modified (helps user and you, the coder)
		gameToLoad = files[0];
		labelSelected = g1;
		
		// Clear button overlay
		Texture label = new Texture("Clear_Button.png");
		
		//g1-11 are the buttons, g1 is top, g11 bottom
		//
		//declare new LabelButton, clickable region is the width of the box
		g1 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.GREEN, 30)) {
			public void onClickEnd(){
				//selectLoad changes the colour of the selected save game to green
				selectLoad(this);
				//gameToLoad stores the currently chosen save file
				gameToLoad = files[0];
			}
		};
		//Display the name of the save file without extension
		g1.setText(files[0].nameWithoutExtension());
		g1.setPosition(281, 500);
		g1.setAlignment(0);
		
		//Draw label
		Add(g1);
		
		//The rest follow a v. similar pattern!!
		
		g2 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[1];
			}
		};
		g2.setText(files[1].nameWithoutExtension());
		g2.setPosition(281, 465);
		g2.setAlignment(0);
		Add(g2);
		
		g3 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[2];
			}
		};
		g3.setText(files[2].nameWithoutExtension());
		g3.setPosition(281, 430);
		g3.setAlignment(0);
		Add(g3);
		
		g4 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[3];
			}
		};
		g4.setText(files[3].nameWithoutExtension());
		g4.setPosition(281, 395);
		g4.setAlignment(0);
		Add(g4);
		
		g5 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[4];
			}
		};
		g5.setText(files[4].nameWithoutExtension());
		g5.setPosition(281, 360);
		g5.setAlignment(0);
		Add(g5);
		
		g6 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[5];
			}
		};
		g6.setText(files[5].nameWithoutExtension());
		g6.setPosition(281, 325);
		g6.setAlignment(0);
		Add(g6);
		
		g7 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[6];
			}
		};
		g7.setText(files[6].nameWithoutExtension());
		g7.setPosition(281, 290);
		g7.setAlignment(0);
		Add(g7);
		
		g8 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[7];
			}
		};
		g8.setText(files[7].nameWithoutExtension());
		g8.setPosition(281, 255);
		g8.setAlignment(0);
		Add(g8);
		
		g9 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[8];
			}
		};
		g9.setText(files[8].nameWithoutExtension());
		g9.setPosition(281, 220);
		g9.setAlignment(0);
		Add(g9);
		
		g10 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[9];
			}
		};
		g10.setText(files[9].nameWithoutExtension());
		g10.setPosition(281, 185);
		g10.setAlignment(0);
		Add(g10);
		
		g11 = new LabelButton(this, label, 479, 26, Label.genericFont(Color.BLACK, 30)) {
			public void onClickEnd(){
				selectLoad(this);
				gameToLoad = files[10];
			}
		};
		g11.setText(files[10].nameWithoutExtension());
		g11.setPosition(281, 150);
		g11.setAlignment(0);
		Add(g11);
		
	}
	
	//Changes the colour of the selected save game
	public void selectLoad(LabelButton label){
		//labelSelected.setColor(Color.BLACK); << code needs enabling when setColor is back working
		label.setColor(Color.GREEN);
		labelSelected = label;
		
	}
	
	//Creates a JFileChooser so the user may choose a file that isn't listed
	public void loadFromChooser() {
		
		//Create filter so only .taxe files may be loaded
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TaxE Saves", "taxe");	
		
		//Instantiate new JFileChooser, default directory the local root
		JFileChooser chooser = new JFileChooser(locRoot);
		
		//Apply filter created above
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter); 
		
		// Open Dialog for File Choosing and assign absolute path to loadFilePath
		int returnVal = chooser.showOpenDialog(chooser);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       loadFilePath = chooser.getSelectedFile().getAbsolutePath();
	    }
	    else {
	    	return;
	    }
	    System.out.println(loadFilePath);
	    
	    //Load file into libgdx's FileHandle System using absolute path.
	    //Have to create a new FileHandle loadedFromChooser.. Doesn't work without this
	    FileHandle loadedFromChooser = Gdx.files.absolute(loadFilePath);
	    //Make our chosen save file
	    gameToLoad = loadedFromChooser;
	    
	    
		
	}
	
	// Loads when the 'Load Game' button is pressed, currently just prints to console
	public void loadGame(){
		System.out.println("Loading game: " + gameToLoad.name());
	}
	
	
}

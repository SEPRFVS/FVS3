package com.fvs.taxe.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.Scene;
import com.fvs.taxe.SpriteComponent;
import com.fvs.taxe.guiobjects.Button;
import com.fvs.taxe.guiobjects.EditText;
import com.fvs.taxe.guiobjects.Label;
import com.fvs.taxe.guiobjects.LabelButton;

public class NewGameScene extends Scene   {

	SpriteComponent playerDetails;

	// DECLARING VARIABLES
	
	
	//Player Details-----
	//Stores the current and last selected difficulty
	static LabelButton currentDifficultyPlayer1, prevDifficultyPlayer1, currentDifficultyPlayer2, prevDifficultyPlayer2;
	//Stores the current and last selected start city.
	static LabelButton startCityPlayer1, prevStartCityPlayer1, startCityPlayer2, prevStartCityPlayer2;

	//Labels & Buttons
	//Labels for Fields (P1 & P2)
	static EditText player1NameText, player2NameText;
	Label title, player1TitleLabel, player1Difficulty, player1StartLocation;
	Label player2TitleLabel, player2NameLabel, player2Difficulty, player2StartLocation;
	
	//Difficulty Buttons
	LabelButton player1Easy, player1Med, player1Hard;
	LabelButton player2Easy, player2Med, player2Hard;

	//Start City Buttons
	LabelButton player1London, player1Paris, player1Berlin, player1Rome, player1Krakow, player1Lisbon, player1Madrid, player1Budapest, player1Moscow;
	LabelButton player2London, player2Paris, player2Berlin, player2Rome, player2Krakow, player2Lisbon, player2Madrid, player2Budapest, player2Moscow;
	
	//-------------------
	//-------------------

	// onCreate() runs as the scene is created
	public void onCreate()
	{
		// Create background image
		Texture trainsText = new Texture("new_player.png");
		playerDetails = new SpriteComponent(this, trainsText, Game.shopZ);
		playerDetails.setPosition(0, 0);
		playerDetails.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(playerDetails);
		// ---------------------
		
		// Draw the Scene Title
		
		drawTitle();
		
		// Draw player 1 info on create
		drawPlayer1Info();
		
		// Draw player 2 info on create
		drawPlayer2Info();
		
		// Draw buttons on create
		drawButtons();
		
		// Set defaults on create
		currentDifficultyPlayer1 = player1Easy;
		currentDifficultyPlayer2 = player2Easy;
		startCityPlayer1 = player1London;
		startCityPlayer2 = player2London;
		
	}
	
	// Draws the title text "Set up a new game"
	public void drawTitle()
	{
		Texture titleLabelText = new Texture("Clear_Button.png");
		title = new Label(this, titleLabelText, Label.genericFont(Color.BLUE,70));
		title.setText("Set up a new game");
		title.setPosition(36, 720);
		title.setAlignment(0);
		Add(title);
		
	}
	
	// All player 1 info drawn with required onClicks defined
	public void drawPlayer1Info()
	{
		// Create Title "Player 1"
		Texture player1LabelText = new Texture("Clear_Button.png");
		player1TitleLabel = new Label(this, player1LabelText, Label.genericFont(Color.BLUE, 50));
		player1TitleLabel.setText("Player 1");
		player1TitleLabel.setPosition(36, 580);
		player1TitleLabel.setAlignment(0);
		Add(player1TitleLabel);

		
		// Create player 1 name label
		player1NameText = new EditText(this, player1LabelText, Label.genericFont(Color.GRAY, 40), Game.shopZ);
		player1NameText.setText("Name");
		player1NameText.setSize(200, 50);
		player1NameText.setPosition(46, 450);
		player1NameText.setAlignment(0);
		Add(player1NameText);
		
		// Player 1 Difficulty
		player1Difficulty = new Label(this, player1LabelText, Label.genericFont(Color.BLUE, 40));
		player1Difficulty.setText("Difficulty");
		player1Difficulty.setPosition(36, 390);
		player1Difficulty.setAlignment(0);
		Add(player1Difficulty);

		
		// Player 1 Start Location
		player1StartLocation = new Label(this, player1LabelText, Label.genericFont(Color.BLUE, 40));
		player1StartLocation.setText("Starting Location");
		player1StartLocation.setPosition(36, 280);
		player1StartLocation.setAlignment(0);
		Add(player1StartLocation);

		
		// Player 1 Easy
		player1Easy = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.BLUE, 30)) {
			public void onClickEnd() {
				setDifficultyPlayer1(this);
			}
		};
		
		player1Easy.setText("Easy");
		player1Easy.setPosition(66, 310);
		player1Easy.setAlignment(0);
		Add(player1Easy);

		
		// Player 1 Medium
		player1Med = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				setDifficultyPlayer1(this);
			}
		};
		player1Med.setText("Medium");
		player1Med.setPosition(186, 310);
		player1Med.setAlignment(0);
		Add(player1Med);

		
		// Player 1 Hard
		player1Hard = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				setDifficultyPlayer1(this);
			
			}
		};
		player1Hard.setText("Hard");
		player1Hard.setPosition(326, 310);
		player1Hard.setAlignment(0);
		Add(player1Hard);

		
		//////--- Starting Location Labels
		
		// Player 1 London
		player1London = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.BLUE, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1London.setText("London");
		player1London.setPosition(66, 200);
		player1London.setAlignment(0);
		Add(player1London);

		
		// Player 1 Paris
		player1Paris = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Paris.setText("Paris");
		player1Paris.setPosition(66, 135);
		player1Paris.setAlignment(0);
		Add(player1Paris);

		
		// Player 1 Berlin
		player1Berlin = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Berlin.setText("Berlin");
		player1Berlin.setPosition(66, 70);
		player1Berlin.setAlignment(0);
		Add(player1Berlin);

		
		// Player 1 Rome
		player1Rome = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Rome.setText("Rome");
		player1Rome.setPosition(186, 200);
		player1Rome.setAlignment(0);
		Add(player1Rome);

		
		// Player 1 Krakow
		player1Krakow = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Krakow.setText("Krakow");
		player1Krakow.setPosition(186,135);
		player1Krakow.setAlignment(0);
		Add(player1Krakow);

		
		// Player 1 Lisbon
		player1Lisbon = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Lisbon.setText("Lisbon");
		player1Lisbon.setPosition(186, 70);
		player1Lisbon.setAlignment(0);
		Add(player1Lisbon);

		
		// Player 1 Madrid
		player1Madrid = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30))  {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Madrid.setText("Madrid");
		player1Madrid.setPosition(326, 200);
		player1Madrid.setAlignment(0);
		Add(player1Madrid);

		
		// Player 1 Budapest
		player1Budapest = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Budapest.setText("Budapest");
		player1Budapest.setPosition(326,135);
		player1Budapest.setAlignment(0);
		Add(player1Budapest);

		
		// Player 1 Moscow
		player1Moscow = new LabelButton(this, player1LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer1(this);
			}
		};
		player1Moscow.setText("Moscow");
		player1Moscow.setPosition(326, 70);
		player1Moscow.setAlignment(0);
		Add(player1Moscow);
				
	}
	
	// All player 2 info drawn with relevant onClicks defined
	
	public static EditText getPlayer1NameText(){
		return player1NameText;
	}
	
	public static EditText getPlayer2NameText(){
		return player2NameText;
	}
	
	public void drawPlayer2Info()
	{
		// Create Title "Player 2"
		Texture player2LabelText = new Texture("Clear_Button.png");
		player2TitleLabel = new Label(this, player2LabelText, Label.genericFont(Color.RED, 50));
		player2TitleLabel.setText("Player 2");
		player2TitleLabel.setPosition(550, 580);
		player2TitleLabel.setAlignment(0);
		Add(player2TitleLabel);

		// Create player 2 name label
		player2NameText = new EditText(this, player2LabelText, Label.genericFont(Color.GRAY, 40), Game.shopZ);
		player2NameText.setText("Name");
		player2NameText.setSize(200, 50);
		player2NameText.setPosition(560, 450);
		player2NameText.setAlignment(0);
		Add(player2NameText);
		
		// Player 2 Name Input
		
		
		// Player 2 Difficulty Title
		player2Difficulty = new Label(this, player2LabelText, Label.genericFont(Color.RED, 40));
		player2Difficulty.setText("Difficulty");
		player2Difficulty.setPosition(560, 390);
		player2Difficulty.setAlignment(0);
		Add(player2Difficulty);

		
		// Player 2 Start Location
		player2StartLocation = new Label(this, player2LabelText, Label.genericFont(Color.RED, 40));
		player2StartLocation.setText("Starting Location");
		player2StartLocation.setPosition(550, 280);
		player2StartLocation.setAlignment(0);
		Add(player2StartLocation);		
		
		// Player 2 Easy
		player2Easy = new LabelButton(this, player2LabelText, 65, 30, Label.genericFont(Color.RED, 30)) {
			public void onClickEnd() {
				setDifficultyPlayer2(this);
			}
		};
		player2Easy.setText("Easy");
		player2Easy.setPosition(580, 310);
		player2Easy.setAlignment(0);
		Add(player2Easy);
		
		// Player 2 Medium
		player2Med = new LabelButton(this, player2LabelText, 80, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				setDifficultyPlayer2(this);
			}
		};
		player2Med.setText("Medium");
		player2Med.setPosition(700, 310);
		player2Med.setAlignment(0);
		Add(player2Med);
		
		// Player 2 Hard
		player2Hard = new LabelButton(this, player2LabelText, 65, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				setDifficultyPlayer2(this);
			}
		};
		player2Hard.setText("Hard");
		player2Hard.setPosition(840, 310);
		player2Hard.setAlignment(0);
		Add(player2Hard);
		// ---------------------
		
		//////--- Starting Location Labels
		
		// Player 2 London
		player2London = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.RED, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2London.setText("London");
		player2London.setPosition(580, 200);
		player2London.setAlignment(0);
		Add(player2London);

		
		// Player 2 Paris
		player2Paris = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Paris.setText("Paris");
		player2Paris.setPosition(580, 135);
		player2Paris.setAlignment(0);
		Add(player2Paris);

		
		// Player 2 Paris
		player2Berlin = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Berlin.setText("Berlin");
		player2Berlin.setPosition(580, 70);
		player2Berlin.setAlignment(0);
		Add(player2Berlin);
		
		// Player 2 Rome
		player2Rome = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Rome.setText("Rome");
		player2Rome.setPosition(700, 200);
		player2Rome.setAlignment(0);
		Add(player2Rome);
		
		
		// Player 2 Krakow
		player2Krakow = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Krakow.setText("Krakow");
		player2Krakow.setPosition(700,135);
		player2Krakow.setAlignment(0);
		Add(player2Krakow);
		
		// Player 2 Lisbon
		player2Lisbon = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Lisbon.setText("Lisbon");
		player2Lisbon.setPosition(700, 70);
		player2Lisbon.setAlignment(0);
		Add(player2Lisbon);
		
		// Player 2 Madrid
		player2Madrid = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Madrid.setText("Madrid");
		player2Madrid.setPosition(840, 200);
		player2Madrid.setAlignment(0);
		Add(player2Madrid);

		
		// Player 2 Budapest
		player2Budapest = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Budapest.setText("Budapest");
		player2Budapest.setPosition(840,135);
		player2Budapest.setAlignment(0);
		Add(player2Budapest);

		
		// Player 2 Moscow
		player2Moscow = new LabelButton(this, player2LabelText, 70, 30, Label.genericFont(Color.GRAY, 30)) {
			public void onClickEnd() {
				startCityPlayer2(this);
			}
		};
		player2Moscow.setText("Moscow");
		player2Moscow.setPosition(840, 70);
		player2Moscow.setAlignment(0);
		Add(player2Moscow);		
	}
	
	public void drawButtons()
	{
		Texture buttonText = new Texture("Clear_Button.png");
		
		// Create Start Game button overlay
		Button startButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				startGame();
			}
		};
		startButton.setPosition(827, 667);
		startButton.setSize(152, 45);
		startButton.setTexture(buttonText);
		Add(startButton);		
		
		
		// Create button to go to Main Menu
		Button backButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				backToMenu();
			}
		};
		backButton.setPosition(644,667);
		backButton.setSize(152, 45);
		backButton.setTexture(buttonText);
		Add(backButton);	
	}
	
	//---------------------------------------------
	//---------------------------------------------
	//---------------------------------------------

	
	//Set Difficulties for P1 & P2, Highlight in GUI
	public void setDifficultyPlayer1(LabelButton label)
	{
		prevDifficultyPlayer1 = currentDifficultyPlayer1;
		prevDifficultyPlayer1.setFont(Label.genericFont(Color.GRAY, 30));

		label.setFont(Label.genericFont(Color.BLUE, 30));
		currentDifficultyPlayer1 = label;
		
		System.out.println("Player 1 difficulty set:" + label.getText());
	}
	
	public void setDifficultyPlayer2(LabelButton label)
	{
		prevDifficultyPlayer2 = currentDifficultyPlayer2;
		prevDifficultyPlayer2.setFont(Label.genericFont(Color.GRAY, 30));

		label.setFont(Label.genericFont(Color.RED, 30));
		currentDifficultyPlayer2 = label;
		
		System.out.println("Player 2 difficulty set:" + label.getText());
		
	}

	//---------------------------------------------
	//---------------------------------------------
	//---------------------------------------------

	// Sets Start City for P1, highlights in GUI
	public void startCityPlayer1(LabelButton label) {
		prevStartCityPlayer1 = startCityPlayer1;
		prevStartCityPlayer1.setFont(Label.genericFont(Color.GRAY, 30));
		
		label.setFont(Label.genericFont(Color.BLUE, 30));
		startCityPlayer1 = label;
	}
	
	// Sets Start City for P2, highlights in GUI
	public void startCityPlayer2(LabelButton label) {
		prevStartCityPlayer2 = startCityPlayer2;
		prevStartCityPlayer2.setFont(Label.genericFont(Color.GRAY, 30));
		
		label.setFont(Label.genericFont(Color.RED, 30));
		startCityPlayer2 = label;
	}
	
	//---------------------------------------------
	//---------------------------------------------
	//---------------------------------------------
	
	// Write information to each Player, begin game - *****THESE ARE TESTS!!
	public void startGame()
	{
		System.out.println("Game Started: " + currentDifficultyPlayer1.getText() + " " + currentDifficultyPlayer2.getText() + startCityPlayer1.getText() + startCityPlayer2.getText());	
		Player p1 = generatePlayer(currentDifficultyPlayer1, player1NameText, startCityPlayer1);
		Player p2 = generatePlayer(currentDifficultyPlayer2, player2NameText, startCityPlayer2);
		Game.setScene(new GameScene(p1, p2));
	}
	
	public Player generatePlayer(LabelButton currentDifficultyPlayer, EditText playerNameText, LabelButton currentLocationPlayer)
	{
		Player p = new Player();
		p.setName(playerNameText.getText());
		//Money and fuel changes with difficulty
		if(currentDifficultyPlayer.getText().equals("Easy"))
		{
			p.setMoney(500);
			p.setFuel(1000);
		}
		if(currentDifficultyPlayer.getText().equals("Medium"))
		{
			p.setMoney(200);
			p.setFuel(800);
		}
		if(currentDifficultyPlayer.getText().equals("Hard"))
		{
			p.setMoney(100);
			p.setFuel(500);
		}
		p.setStartLocation(currentLocationPlayer.getText());
		return p;
	}
	
	public void backToMenu()
	{
		System.out.println("Didn't want you to play anyway!");
		Game.setScene(new MainMenuScene());
	}
}

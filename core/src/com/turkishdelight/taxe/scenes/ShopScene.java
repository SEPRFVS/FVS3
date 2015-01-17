package com.turkishdelight.taxe.scenes;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.guiobjects.Label;
import com.turkishdelight.taxe.guiobjects.LabelButton;
import com.turkishdelight.taxe.guiobjects.Pane;
import com.turkishdelight.taxe.guiobjects.Scroller;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.routing.Train.Type;
import com.turkishdelight.taxe.worldobjects.Station;

public class ShopScene extends GameWindowedGUIScene {

	SpriteComponent shop;
	SpriteComponent scrollPaneBackground;
	Pane pane;
	
	private LabelButton steamButton;
	private LabelButton dieselButton;
	private LabelButton electricButton;
	private LabelButton nuclearButton;
	private LabelButton magLevButton;
	private LabelButton kingButton;
	
	public ShopScene(GameScene parent, Player player1, Player player2)
	{
		super(parent, player1, player2);
	}
	
	@Override
	public void drawGUIBackground()
	{
		// Create background image for shop
		Texture trainsText = new Texture("Shop_Trains.png");
		shop = new SpriteComponent(this, trainsText, Game.backgroundZ);
		shop.setPosition(0, 0);
		shop.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(shop);
		// ---------------------
		
		drawTrainsScrollpane();
	}
	
	public void drawTrainsScrollpane()
	{
		pane = new Pane(this, -1);
		pane.setSize(922, 800);
		pane.setPosition(50, 485 - pane.getHeight());
		Add(pane);
		
		Texture scrollPaneBackgroundText = new Texture("Trains_Scrollpane_Background.png");
		scrollPaneBackground = new SpriteComponent(this, scrollPaneBackgroundText, Game.guiZ);
		scrollPaneBackground.setSize(922,800);
		scrollPaneBackground.setLocalPosition(0, 0);
		pane.Add(scrollPaneBackground);
		
		//Min Y is the position the pane must be in to show it's lowest content
		//Max Y is the position the pane must be in to show it's highest content
		final float minY = 73;
		final float maxY = 485 - pane.getHeight();
		
		Texture scrollerText = new Texture("Scroller.png");
		final Scroller scrollPane = new Scroller(this, scrollerText, Game.guiZ) {
					@Override
					public void onMove(float percentage)
					{
						pane.setY(((maxY - minY) * percentage) + minY);
					}
		};
		scrollPane.setOrientation(false);
		scrollPane.setSize(15, 30);
		scrollPane.setRange(74, 454);
		scrollPane.setPosition(35, 454);
		Add(scrollPane);
		
		drawUpgradeButtons();
		drawTrainButtons();
	}
	
	public void drawTrainButtons()
	{
		// Create Steam button

				Player activePlayer = parentGame.activePlayer();
				Texture buyButtonText = new Texture("buy_bg.png");
				Texture sellButtonText = new Texture("sell_bg.png");
				steamButton = new LabelButton(this) {
				@Override
				public void onClickEnd()
				{
					steamPressed();
				}
				};
				if(activePlayer.hasTrain("Steam"))
				{
					steamButton.setTexture(sellButtonText);
					steamButton.setText("Sell: 10cr");
				}
				else
				{
					steamButton.setTexture(buyButtonText);
					steamButton.setText("Buy: 10cr");
				}
				steamButton.setLocalPosition(65, 555);
				steamButton.setSize(115, 34);
				steamButton.setAlignment(1);
				steamButton.setAlpha(1);
				steamButton.setFont(Label.genericFont(Color.WHITE, 22));
				pane.Add(steamButton);
				// ---------------------
				
				// Create Diesel button
				dieselButton = new LabelButton(this) {
				@Override
				public void onClickEnd()
					{
						dieselPressed();
					}
				};
				dieselButton.setLocalPosition(530, 555);
				dieselButton.setSize(115, 34);
				if(activePlayer.hasTrain("Diesel"))
				{
					dieselButton.setTexture(sellButtonText);
					dieselButton.setText("Sell: 30cr");
				}
				else
				{
					dieselButton.setTexture(buyButtonText);
					dieselButton.setText("Buy: 30cr");
				}
				dieselButton.setAlignment(1);
				dieselButton.setAlpha(1);
				dieselButton.setFont(Label.genericFont(Color.WHITE, 22));
				pane.Add(dieselButton);
				// ---------------------
						
				// Create Electric button
				electricButton = new LabelButton(this) {
					@Override
					public void onClickEnd()
					{
						electricPressed();
					}
				};
				electricButton.setLocalPosition(65, 305);
				electricButton.setSize(115, 34);
				if(activePlayer.hasTrain("Electric"))
				{
					electricButton.setTexture(sellButtonText);
					electricButton.setText("Sell: 90cr");
				}
				else
				{
					electricButton.setTexture(buyButtonText);
					electricButton.setText("Buy: 90cr");
				}
				electricButton.setAlignment(1);
				electricButton.setAlpha(1);
				electricButton.setFont(Label.genericFont(Color.WHITE, 22));
				pane.Add(electricButton);
				// ---------------------
						
				// Create Nuclear button
				nuclearButton = new LabelButton(this) {
					@Override
					public void onClickEnd()
					{
						nuclearPressed();
					}
				};
				nuclearButton.setLocalPosition(530, 305);
				nuclearButton.setSize(115, 34);
				if(activePlayer.hasTrain("Nuclear"))
				{
					nuclearButton.setTexture(sellButtonText);
					nuclearButton.setText("Sell: 200r");
				}
				else
				{
					nuclearButton.setTexture(buyButtonText);
					nuclearButton.setText("Buy: 200cr");
				}
				nuclearButton.setAlignment(1);
				nuclearButton.setAlpha(1);
				nuclearButton.setFont(Label.genericFont(Color.WHITE, 22));
				pane.Add(nuclearButton);
				// ---------------------
						
				// Create MagLev button
				magLevButton = new LabelButton(this) {
					@Override
					public void onClickEnd()
					{
						magLevPressed();
					}
				};
				magLevButton.setLocalPosition(65, 45);
				magLevButton.setSize(115, 34);
				if(activePlayer.hasTrain("Mag"))
				{
					magLevButton.setTexture(sellButtonText);
					magLevButton.setText("Sell: 500cr");
				}
				else
				{
					magLevButton.setTexture(buyButtonText);
					magLevButton.setText("Buy: 500cr");
				}
				magLevButton.setAlignment(1);
				magLevButton.setAlpha(1);
				magLevButton.setFont(Label.genericFont(Color.WHITE, 22));
				pane.Add(magLevButton);
				// ---------------------
						
				// Create King button
				kingButton = new LabelButton(this) {
					@Override
					public void onClickEnd()
					{
						kingPressed();
					}
				};
				kingButton.setLocalPosition(530, 45);
				kingButton.setSize(115, 34);
				if(activePlayer.hasTrain("TheKing"))
				{
					kingButton.setTexture(sellButtonText);
					kingButton.setText("Sell: 1000cr");
				}
				else
				{
					kingButton.setTexture(buyButtonText);
					kingButton.setText("Buy: 1000cr");
				}
				kingButton.setAlignment(1);
				kingButton.setAlpha(1);
				kingButton.setFont(Label.genericFont(Color.WHITE, 22));
				pane.Add(kingButton);
				// ---------------------
	}
	
	public void refreshButtons()
	{
		Player activePlayer = parentGame.activePlayer();
		Texture buyButtonText = new Texture("buy_bg.png");
		Texture sellButtonText = new Texture("sell_bg.png");
		//Refresh steam button
		if(activePlayer.hasTrain("Steam"))
		{
			steamButton.setTexture(sellButtonText);
			steamButton.setText("Sell: 10cr");
		}
		else
		{
			steamButton.setTexture(buyButtonText);
			steamButton.setText("Buy: 10cr");
		}
		// ---------------------
		
		// Refresh Diesel button
		if(activePlayer.hasTrain("Diesel"))
		{
			dieselButton.setTexture(sellButtonText);
			dieselButton.setText("Sell: 30cr");
		}
		else
		{
			dieselButton.setTexture(buyButtonText);
			dieselButton.setText("Buy: 30cr");
		}
		// Refresh Electric button
		if(activePlayer.hasTrain("Electric"))
		{
			electricButton.setTexture(sellButtonText);
			electricButton.setText("Sell: 90cr");
		}
		else
		{
			electricButton.setTexture(buyButtonText);
			electricButton.setText("Buy: 90cr");
		}
				
		// Refresh Nuclear button
		if(activePlayer.hasTrain("Nuclear"))
		{
			nuclearButton.setTexture(sellButtonText);
			nuclearButton.setText("Sell: 200r");
		}
		else
		{
			nuclearButton.setTexture(buyButtonText);
			nuclearButton.setText("Buy: 200cr");
		}
				
		// Refresh MagLev button
		if(activePlayer.hasTrain("Mag"))
		{
			magLevButton.setTexture(sellButtonText);
			magLevButton.setText("Sell: 500cr");
		}
		else
		{
			magLevButton.setTexture(buyButtonText);
			magLevButton.setText("Buy: 500cr");
		}
				
		// Refresh King button
		if(activePlayer.hasTrain("TheKing"))
		{
			kingButton.setTexture(sellButtonText);
			kingButton.setText("Sell: 1000cr");
		}
		else
		{
			kingButton.setTexture(buyButtonText);
			kingButton.setText("Buy: 1000cr");
		}
	}
	
	public void drawUpgradeButtons()
	{
		
		Texture upgradeButtonText = new Texture("upgrade_bg.png");
		
		int xCoord1 = 360;
		int xCoord2 = 820;
		int yCoord = 708;
		for (int i = 0; i < 18; i++)
		{
			
			// Create Upgrade buttons
			LabelButton upgradeButton = new LabelButton(this) {
				@Override
				public void onClickEnd()
				{
					upgradePressed();
				}
			};
			
			
			if (i % 6 == 0 && i != 0)
			{
				// Update y Coordinate for big jump after 6 buttons have been displayed
				yCoord = yCoord - 105;
				upgradeButton.setLocalPosition(xCoord1, yCoord);
			}
			else
			{
				if (i % 2 == 0)
				{
					upgradeButton.setLocalPosition(xCoord1, yCoord);
				}
				else
				{
					upgradeButton.setLocalPosition(xCoord2, yCoord);
					
					// Update y Coordinate for small jump after each row has been displayed
					yCoord = yCoord - 50;
				}
				
			}
			
			upgradeButton.setSize(80, 22);
			upgradeButton.setTexture(upgradeButtonText);
			upgradeButton.setText("Upgrade");
			upgradeButton.setAlignment(1);
			upgradeButton.setAlpha(1);
			upgradeButton.setFont(Label.genericFont(Color.WHITE, 18));
			pane.Add(upgradeButton);
			// ---------------------
			
		}
		// ---------------------
	}
	
	@Override
	public void drawWindowButtons()

	{
		//Create Exit Button
		Button exitButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				Game.popScene();
				Game.pushScene(parentGame);
			}
		};
		exitButton.setPosition(975, 582);
		exitButton.setSize(23, 17);
		Texture exitButtonText = new Texture("shopExitButton.png");
		exitButton.setTexture(exitButtonText);
		Add(exitButton);

		// Create Train button
		Button trainButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				trainPressed();
			}
		};
		trainButton.setPosition(418, 518);
		trainButton.setSize(180, 70);
		Texture buttonText = new Texture("Clear_Button.png");
		trainButton.setTexture(buttonText);
		Add(trainButton);
		// ---------------------
		
		// Create Obstacle button
		Button obstacleButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				obstaclePressed();
			}
		};
		obstacleButton.setPosition(608, 518);
		obstacleButton.setSize(180, 70);
		obstacleButton.setTexture(buttonText);
		Add(obstacleButton);
		// ---------------------
		
		// Create Resource button
		Button resourceButton = new Button(this) {
			@Override
			public void onClickEnd()
			{
				resourcePressed();
			}
		};
		resourceButton.setPosition(798, 518);
		resourceButton.setSize(180, 70);
		resourceButton.setTexture(buttonText);
		Add(resourceButton);
		// ---------------------
	}
	
	public void trainPressed()
	{
		System.out.println("trainPressed");
		
		// Create trains texture and set shop window background to be trains.
		Texture trainsText = new Texture("Shop_Trains.png");
		shop.setTexture(trainsText);
		// ---------------------
		
		// Create trains scrollpane background texture and assign to the scrollpane
		Texture scrollPaneBackgroundText = new Texture("Trains_Scrollpane_Background.png");
		scrollPaneBackground.setTexture(scrollPaneBackgroundText);
		// ---------------------
		
		// Set buy/sell buttons to be visible
		steamButton.setAlpha(1);
		dieselButton.setAlpha(1);
		electricButton.setAlpha(1);
		nuclearButton.setAlpha(1);
		magLevButton.setAlpha(1);
		kingButton.setAlpha(1);
		// ---------------------
		
	}
	
	public void obstaclePressed()
	{
		System.out.println("obstaclePressed");
		Game.pushScene(parentGame.makeDialogueScene("Coming soon!"));
		
	}
	
	public void resourcePressed()
	{
		System.out.println("resourcePressed");
		
		// Create resources texture and set shop window background to be resources.
		Texture resourcesText = new Texture("Shop_Resources.png");
		shop.setTexture(resourcesText);
		// ---------------------
		
		// Create resources scrollpane background texture and assign to the scrollpane
		Texture scrollPaneBackgroundText = new Texture("Obstacles_Scrollpane_Background.png");
		scrollPaneBackground.setTexture(scrollPaneBackgroundText);
		// ---------------------
		
		// Set buy/sell buttons to be transparent
		steamButton.setAlpha(0);
		dieselButton.setAlpha(0);
		electricButton.setAlpha(0);
		nuclearButton.setAlpha(0);
		magLevButton.setAlpha(0);
		kingButton.setAlpha(0);
		// ---------------------
		
	}
	
	@Override
	public void shopToolbarPressed() 
	{
		System.out.println("shopToolbarPressed");
		//DO Nothing
	}
	
	public void steamPressed()
	{
		if(!parentGame.activePlayer().hasTrain("Steam"))
		{
			buyPressed(parentGame.activePlayer(), Train.Type.STEAM, 10);
		}
		else
		{
			DialogueScene dial =  new DialogueScene("Are you sure?") {
				@Override
				public void onOkayButton()
				{
					parentGame.activePlayer().sellTrain("Steam", 10, parentGame);
					refreshButtons();
					updateValues();
				}
			};
			Game.pushScene(dial);
		}
	}
	
	public void dieselPressed()
	{
		System.out.println("dieselPressed");
		if(!parentGame.activePlayer().hasTrain("Diesel"))
		{
			buyPressed(parentGame.activePlayer(), Train.Type.DIESEL, 30);
		}
		else
		{
			DialogueScene dial =  new DialogueScene("Are you sure?") {
				@Override
				public void onOkayButton()
				{
					parentGame.activePlayer().sellTrain("Diesel", 30, parentGame);
					refreshButtons();
					updateValues();
				}
			};
			Game.pushScene(dial);
		}
	}
	
	public void electricPressed()
	{
		System.out.println("electricPressed");
	}
	
	public void nuclearPressed()
	{
		System.out.println("nuclearPressed");
	}
	
	public void magLevPressed()
	{
		System.out.println("magLevPressed");
	}
	
	public void kingPressed()
	{
		System.out.println("kingPressed");
	}
	
	public void upgradePressed()
	{
		System.out.println("upgradePressed");
	}
	
	public void buyPressed(final Player player, final Type trainType, final int price)
	{
		ArrayList<String> lstIn = new ArrayList<String>();
		lstIn.add("London");
		lstIn.add("Rome");
		lstIn.add("Moscow");
		lstIn.add("Lisbon");
		lstIn.add("Paris");
		lstIn.add("Berlin");
		lstIn.add("Madrid");
		lstIn.add("Budapest");
		SelectionScene locationSelectionScene = new SelectionScene(new Texture("locationselection.png"), lstIn) {
			@Override
			public void onSelectionEnd() {
				String selectedStation = (String)elements.get(selectedElementIndex);
				player.buyTrain(trainType, price, selectedStation, parentGame);
				Game.popScene();
				refreshButtons();
				updateValues();
			}
		};
		Game.pushScene(locationSelectionScene);
	}
	@Override
	public void onFocusGained()
	{
		super.onFocusGained();
		refreshButtons();
	}
}

package com.fvs.taxe.scenes;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.fvs.taxe.Game;
import com.fvs.taxe.Player;
import com.fvs.taxe.SpriteComponent;
import com.fvs.taxe.guiobjects.Button;
import com.fvs.taxe.guiobjects.Label;
import com.fvs.taxe.guiobjects.LabelButton;
import com.fvs.taxe.guiobjects.Pane;
import com.fvs.taxe.guiobjects.Scroller;
import com.fvs.taxe.routing.Train;
import com.fvs.taxe.routing.Train.Type;
import com.fvs.taxe.worldobjects.Obstacle;

public class ShopScene extends GameWindowedGUIScene {

	SpriteComponent shop;
	SpriteComponent scrollPaneBackground;
	Pane pane;
	private String paneType = "Train";
	
	private LabelButton steamButton;
	private LabelButton dieselButton;
	private LabelButton electricButton;
	private LabelButton nuclearButton;
	private LabelButton magLevButton;
	private LabelButton kingButton;
	private Label fuelLabel, fuelPriceLabel;
	private LabelButton fuelButton;
    private Label junctionObstacleLabel;
    private LabelButton junctionObstacleButton;
	ArrayList<LabelButton> upgradeButtons = new ArrayList<LabelButton>();
	
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
		pane = new Pane(this, -1);
		pane.setSize(922, 800);
		pane.setPosition(50, 485 - pane.getHeight());
		Add(pane);
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
		drawTrainsScrollpane();
	}
	
	public void drawTrainsScrollpane()
	{
		paneType = "Train";
		pane.clear();
		
		Texture scrollPaneBackgroundText = new Texture("Trains_Scrollpane_Background.png");
		scrollPaneBackground = new SpriteComponent(this, scrollPaneBackgroundText, Game.guiZ);
		scrollPaneBackground.setSize(922,800);
		scrollPaneBackground.setLocalPosition(0, 0);
		pane.Add(scrollPaneBackground);
		
		
		
		drawTrainButtons();
	}

    public void drawObstacleScrollPane()
    {
        paneType = "Obstacle";
        pane.clear();

        Texture scrollPaneBackgroundText = new Texture("Obstacles_Scrollpane_Background.png");
        scrollPaneBackground = new SpriteComponent(this, scrollPaneBackgroundText, Game.guiZ);
        scrollPaneBackground.setSize(922,800);
        scrollPaneBackground.setLocalPosition(0, 0);
        pane.Add(scrollPaneBackground);

        drawObstacleButtons();
    }
	
	public void drawResourceScrollPane()
	{
		paneType = "Resource";
		pane.clear();
		
		Texture scrollPaneBackgroundText = new Texture("Obstacles_Scrollpane_Background.png");
		scrollPaneBackground = new SpriteComponent(this, scrollPaneBackgroundText, Game.guiZ);
		scrollPaneBackground.setSize(922,800);
		scrollPaneBackground.setLocalPosition(0, 0);
		pane.Add(scrollPaneBackground);
		
		drawResourceButtons();
	}
	
	public void drawTrainButtons()
	{
		// Create Steam button

				Player activePlayer = parentGame.activePlayer();
				Texture buyButtonText = new Texture("buy_bg.png");
				Texture sellButtonText = new Texture("sell_bg.png");
				upgradeButtons = new ArrayList<LabelButton>();
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

    public void drawObstacleButtons() {
        final int junctionObstaclePrice = 300;
        Texture buyButtonText = new Texture("buy_bg.png");

        Texture LabelText = new Texture("Clear_Button.png");
        junctionObstacleLabel = new Label(this, LabelText, Label.genericFont(Color.BLACK, 40));
        junctionObstacleLabel.setText("Junction obstacle");
        junctionObstacleLabel.setLocalPosition(65, 800);
        junctionObstacleLabel.setAlignment(0);
        pane.Add(junctionObstacleLabel);


        junctionObstacleButton = new LabelButton(this) {
            @Override
            public void onClickEnd(){
                if(parentGame.activePlayer().getMoney() < junctionObstaclePrice)
                {
                    Game.pushScene(parentGame.makeDialogueScene("Requires " + junctionObstaclePrice + "cr"));
                    return;
                }
                ArrayList<String> lstIn = new ArrayList<String>();
                lstIn.add("Left junction");
                lstIn.add("Right junction");

                SelectionScene locationSelectionScene = new SelectionScene(new Texture("locationselection.png"), lstIn) {
                    @Override
                    public void onSelectionEnd() {
                        String selectedJunction = (String)elements.get(selectedElementIndex);
                        parentGame.activePlayer().buyObstacle(Obstacle.Type.JUNCTION, junctionObstaclePrice, selectedJunction, parentGame);
                        Game.popScene();
                        updateValues();
                    }
                };
                Game.pushScene(locationSelectionScene);
            }
        };
        junctionObstacleButton.setTexture(buyButtonText);
        junctionObstacleButton.setText("Buy: 300cr");
        junctionObstacleButton.setLocalPosition(65, 720);
        junctionObstacleButton.setSize(115, 34);
        junctionObstacleButton.setAlignment(1);
        junctionObstacleButton.setAlpha(1);
        junctionObstacleButton.setFont(Label.genericFont(Color.WHITE, 22));
        pane.Add(junctionObstacleButton);

    }

	public void drawResourceButtons()
	{
		Texture buyButtonText = new Texture("buy_bg.png");
		Texture emptyButtonText = new Texture("sell_bg.png");
		Texture LabelText = new Texture("Clear_Button.png");
		fuelLabel = new Label(this, LabelText, Label.genericFont(Color.BLUE, 40));
		fuelLabel.setText("Fuel Reserve: " + parentGame.fuel);
		fuelLabel.setLocalPosition(65, 800);
		fuelLabel.setAlignment(0);
		pane.Add(fuelLabel);
		fuelPriceLabel = new Label(this, LabelText, Label.genericFont(Color.BLUE, 40));
		fuelPriceLabel.setText("Price per 100 fuel: " + parentGame.crPer100Fuel + "cr");
		fuelPriceLabel.setLocalPosition(65, 740);
		fuelPriceLabel.setAlignment(0);
		pane.Add(fuelPriceLabel);
		fuelButton = new LabelButton(this) {
			@Override
			public void onClickEnd()
			{
				if(parentGame.fuel >= 100)
				{
					if(parentGame.activePlayer().getMoney() >= parentGame.crPer100Fuel)
					{
						Game.pushScene(parentGame.makeDialogueScene("Fuel bought!"));
						parentGame.activePlayer().setMoney(parentGame.activePlayer().getMoney() - parentGame.crPer100Fuel);
						parentGame.activePlayer().setFuel(parentGame.activePlayer().getFuel() + 100);
						parentGame.fuel += -100;
					}
					else
					{
						Game.pushScene(parentGame.makeDialogueScene("Not enough money!"));
					}
				}
			}
		};
		if(parentGame.fuel < 100)
		{
			fuelButton.setTexture(emptyButtonText);
			fuelButton.setText("Sold Out");
		}
		else
		{
			fuelButton.setTexture(buyButtonText);
			fuelButton.setText("Buy");
		}
		fuelButton.setLocalPosition(65, 650);
		fuelButton.setSize(115, 34);
		fuelButton.setAlignment(1);
		fuelButton.setAlpha(1);
		fuelButton.setFont(Label.genericFont(Color.WHITE, 22));
		pane.Add(fuelButton);

	}
	
	public void refreshButtons()
	{
		if(paneType.equals("Train"))
		{
		//If we are in the trains pane, refresh train buttons
		Player activePlayer = parentGame.activePlayer();
		Texture buyButtonText = new Texture("buy_bg.png");
		Texture sellButtonText = new Texture("sell_bg.png");
		for(LabelButton button : upgradeButtons)
		{
			pane.Remove(button);
		}
		upgradeButtons = new ArrayList<LabelButton>();
		//Refresh steam button
		if(activePlayer.hasTrain("Steam"))
		{
			steamButton.setTexture(sellButtonText);
			steamButton.setText("Sell: 10cr");
			drawUpgradeButtons(steamButton, activePlayer.getTrain("Steam"), activePlayer, 5);
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
			drawUpgradeButtons(dieselButton, activePlayer.getTrain("Diesel"), activePlayer, 10);
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
			drawUpgradeButtons(electricButton, activePlayer.getTrain("Electric"), activePlayer, 30);
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
			drawUpgradeButtons(nuclearButton, activePlayer.getTrain("Nuclear"), activePlayer, 50);
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
			drawUpgradeButtons(magLevButton, activePlayer.getTrain("Mag"), activePlayer, 100);
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
			drawUpgradeButtons(kingButton, activePlayer.getTrain("TheKing"), activePlayer, 150);
		}
		else
		{
			kingButton.setTexture(buyButtonText);
			kingButton.setText("Buy: 1000cr");
		}
		}
		else if (paneType.equals("Resource"))
		{

			Texture buyButtonText = new Texture("buy_bg.png");
			Texture emptyButtonText = new Texture("sell_bg.png");
			//If we are in the resource pane, refresh resource buttons
			if(parentGame.fuel < 100)
			{
				fuelButton.setTexture(emptyButtonText);
				fuelButton.setText("Sold Out");
			}
			else
			{
				fuelButton.setTexture(buyButtonText);
				fuelButton.setText("Buy");
			}
			fuelLabel.setText("Fuel Reserve: " + parentGame.fuel);
			fuelPriceLabel.setText("Price per 100 fuel: " + parentGame.crPer100Fuel + "cr");
		}
	}

	public void drawUpgradeButtons(LabelButton baseButton, Train t, Player p, int upgradePrice)
	{
		System.out.println("Drawing " + t.getName() + " upgrade buttons");
		int xdisplacement = 295;
		int ydisplacement = 153;
		int yjump = 50;
		LabelButton buttonSpeed = generateUpgradeButton(baseButton.getLocalX() + xdisplacement, baseButton.getLocalY() + ydisplacement, t, 0, p, upgradePrice);
		LabelButton buttonEfficiency = generateUpgradeButton(baseButton.getLocalX() + xdisplacement, baseButton.getLocalY() + ydisplacement - yjump, t, 1, p, upgradePrice);
		LabelButton buttonReliability = generateUpgradeButton(baseButton.getLocalX() + xdisplacement, baseButton.getLocalY() + ydisplacement - (2 * yjump), t, 2, p, upgradePrice);
		upgradeButtons.add(buttonSpeed);
		upgradeButtons.add(buttonEfficiency);
		upgradeButtons.add(buttonReliability);
		pane.Add(buttonSpeed);
		pane.Add(buttonEfficiency);
		pane.Add(buttonReliability);
	}
	
	public LabelButton generateUpgradeButton(int x, int y, final Train train, final int upgrade, final Player player, final int price)
	{
		Texture upgradeButtonText = new Texture("upgrade_bg.png");
		LabelButton upgradeButton = new LabelButton(this) {
			@Override
			public void onClickEnd()
			{
				if(!train.getUpgrade(upgrade))
				{
					upgradePressed(player, train, upgrade, price);
				}
			}
		};
		upgradeButton.setLocalPosition(x, y);
		upgradeButton.setSize(80, 22);
		upgradeButton.setTexture(upgradeButtonText);
		String text;
		if(!train.getUpgrade(upgrade))
		{
			text = "Upgrade";
		}
		else
		{
			text = "Owned";
		}
		upgradeButton.setText(text);
		upgradeButton.setAlignment(1);
		upgradeButton.setAlpha(1);
		upgradeButton.setFont(Label.genericFont(Color.WHITE, 18));
		return upgradeButton;
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
		
		this.drawTrainsScrollpane();
		this.refreshButtons();
		// Create trains texture and set shop window background to be trains.
		Texture trainsText = new Texture("Shop_Trains.png");
		shop.setTexture(trainsText);
		// ---------------------
		
	}
	
	public void obstaclePressed()
	{
		System.out.println("obstaclePressed");
        this.drawObstacleScrollPane();
        Texture obstaclesText = new Texture("Shop_Obstacles.png");
        shop.setTexture(obstaclesText);
		//Game.pushScene(parentGame.makeDialogueScene("Coming soon!"));
		
	}
	
	public void resourcePressed()
	{
		System.out.println("resourcePressed");
		this.drawResourceScrollPane();
		// Create resources texture and set shop window background to be resources.
		Texture resourcesText = new Texture("Shop_Resources.png");
		shop.setTexture(resourcesText);
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
		if(!parentGame.activePlayer().hasTrain("Electric"))
		{
			buyPressed(parentGame.activePlayer(), Train.Type.ELECTRIC, 90);
		}
		else
		{
			DialogueScene dial =  new DialogueScene("Are you sure?") {
				@Override
				public void onOkayButton()
				{
					parentGame.activePlayer().sellTrain("Electric", 90, parentGame);
					refreshButtons();
					updateValues();
				}
			};
			Game.pushScene(dial);
		}
	}
	
	public void nuclearPressed()
	{
		System.out.println("nuclearPressed");
		if(!parentGame.activePlayer().hasTrain("Nuclear"))
		{
			buyPressed(parentGame.activePlayer(), Train.Type.NUCLEAR, 200);
		}
		else
		{
			DialogueScene dial =  new DialogueScene("Are you sure?") {
				@Override
				public void onOkayButton()
				{
					parentGame.activePlayer().sellTrain("Nuclear", 200, parentGame);
					refreshButtons();
					updateValues();
				}
			};
			Game.pushScene(dial);
		}
	}
	
	public void magLevPressed()
	{
		System.out.println("magLevPressed");
		if(!parentGame.activePlayer().hasTrain("Mag"))
		{
			buyPressed(parentGame.activePlayer(), Train.Type.MAG_LEV, 500);
		}
		else
		{
			DialogueScene dial =  new DialogueScene("Are you sure?") {
				@Override
				public void onOkayButton()
				{
					parentGame.activePlayer().sellTrain("Mag", 500, parentGame);
					refreshButtons();
					updateValues();
				}
			};
			Game.pushScene(dial);
		}
	}
	
	public void kingPressed()
	{
		System.out.println("kingPressed");
		if(!parentGame.activePlayer().hasTrain("TheKing"))
		{
			buyPressed(parentGame.activePlayer(), Train.Type.THE_KING, 1000);
		}
		else
		{
			DialogueScene dial =  new DialogueScene("Are you sure?") {
				@Override
				public void onOkayButton()
				{
					parentGame.activePlayer().sellTrain("TheKing", 1000, parentGame);
					refreshButtons();
					updateValues();
				}
			};
			Game.pushScene(dial);
		}
	}
	
	public void upgradePressed(final Player player, final Train train, final int upgrade, final int price)
	{
		if(player.getMoney() < price)
		{
			Game.pushScene(parentGame.makeDialogueScene("Requires " + price + "cr"));
			return;
		}
		DialogueScene dial =  new DialogueScene("Cost: " + price + "cr") {
			@Override
			public void onOkayButton()
			{
				player.setMoney(player.getMoney() - price);
				train.setUpgrade(upgrade);
				refreshButtons();
				updateValues();
			}
		};
		Game.pushScene(dial);
		System.out.println("upgradePressed");
	}
	
	public void buyPressed(final Player player, final Type trainType, final int price)
	{
		if(player.getMoney() < price)
		{
			Game.pushScene(parentGame.makeDialogueScene("Requires " + price + "cr"));
			return;
		}
		else if(player.getTrainCount() > 2)
		{
			Game.pushScene(parentGame.makeDialogueScene("May only own 3 trains!"));
			return;
		}
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

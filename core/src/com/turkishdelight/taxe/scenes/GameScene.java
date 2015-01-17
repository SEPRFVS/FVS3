package com.turkishdelight.taxe.scenes;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.routing.AiSprite;
import com.turkishdelight.taxe.routing.Carriage;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.routing.Route;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.worldobjects.Location;

public class GameScene extends GameGUIScene {

	Texture mapText;
	SpriteComponent map;
	public ShopScene shopScene;
	public GoalsScene goalsScene;
	public CurrentResourcesScene resourceScene;
	public DialogueScene dialogueScene;
	
	private Location london;
	private Location rome;
	private Location moscow;
	private Location lisbon;
	private Location paris;
	private Location berlin;
	private Location madrid;
	private Location budapest;
	private Location krakow;

	private ArrayList<CurvedPath> curvedPaths;				// collection of curved paths (only one way for wach path) for drawing

	public static boolean collided;							// boolean to test whether a collision has occured in the game

	public GameScene(Player player1In, Player player2In){
		super(player1In, player2In, false);
		nextTurn();
		player1Go = true;
		delayedCreate();
	}
	
	@Override
	public void Draw(SpriteBatch batch) {
		super.Draw(batch);

		// for testing collision detection by drawing hitbox
		/*ShapeRenderer sr = new ShapeRenderer();

		Polygon p = train1.getPolygon();
		Polygon p2 = train2.getPolygon();

		sr.begin(ShapeType.Line);
		sr.polygon(p.getTransformedVertices());
		sr.polygon(p2.getTransformedVertices());
		sr.end();*/
	}
	
	public void delayedCreate() {
		// map setup
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.backgroundZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(map);

		//Locations setup
		curvedPaths = new ArrayList<CurvedPath>();
		london = createLocation(this, 210, 390);
		rome = createLocation(this, 415, 168);
		moscow = createLocation(this, 800, 450);
		lisbon = createLocation(this, 30, 120);
		paris = createLocation(this, 300, 340);
		berlin = createLocation(this, 410, 400);
		madrid = createLocation(this, 120, 150);
		budapest = createLocation(this, 510, 290);
		krakow = createLocation(this, 520, 350);
		System.out.println("Locations created");
		
		// setup connections
		HashMap<String, CurvedPath> paths = getPaths(); // returns all paths with their respective names
		connectLocations(london, paris, paths.get("LondonParis"), paths.get("ParisLondon")); // should be easier way to do this! pass in strings?
		connectLocations(paris, rome, paths.get("ParisRome"), paths.get("RomeParis"));
		connectLocations(rome, krakow, paths.get("RomeKrakow"), paths.get("KrakowRome"));
		connectLocations(london, lisbon, paths.get("LondonLisbon"), paths.get("LisbonLondon"));
		connectLocations(madrid, krakow, paths.get("MadridKrakow"), paths.get("KrakowMadrid"));
		connectLocations(lisbon, madrid, paths.get("LisbonMadrid"), paths.get("MadridLisbon"));
		connectLocations(paris, berlin, paths.get("ParisBerlin"), paths.get("BerlinParis"));
		connectLocations(berlin, budapest, paths.get("BerlinBudapest"), paths.get("BudapestBerlin"));
		connectLocations(krakow, moscow, paths.get("KrakowMoscow"), paths.get("MoscowKrakow"));

		// add trains
		Texture trainTexture = new Texture("traincropped.png"); // traincropped added to allow more accurate collision detection
		createTrainAndCarriage(trainTexture, 1, getSpritePath(), player1);
		createTrainAndCarriage(trainTexture, 2, getSpritePath2(), player2);
		
		// create route (with dotted line)
		Texture text = new Texture("route.png");
		final int divider = 10; // distance between 2 dots
		// go to every x * divider, find closest distance value from curvedPath array and use that to get corresponding position
		for (CurvedPath curvedPath: curvedPaths){
			for (int i = 0; i < curvedPath.getFinalDistance(); i+=divider) {
				int x = curvedPath.closestIndex(i, curvedPath.getDistances());
				SpriteComponent route = new SpriteComponent(this, text, Game.backgroundZ);
				route.setSize(2, 2);
				Vector2 point = curvedPath.getPoint(x);
				route.setPosition(point.x+2, point.y+2);
				Add(route);
			}
		}

		shopScene = new ShopScene(this, this.player1, this.player2);
		goalsScene = new GoalsScene(this, this.player1, this.player2);
		resourceScene = new CurrentResourcesScene(this, this.player1, this.player2);
		dialogueScene = new DialogueScene("");
	}

	private void createTrainAndCarriage(Texture trainTexture, int weight, Route route, Player player) {
		Train train = new Train(this, trainTexture, route, weight);
		Add(train);
		Carriage carriage = new Carriage(this, trainTexture, route, train);
		Add(carriage);
		player.addAiSprite(train);
		player.addAiSprite(carriage);
		train.addCarriage(carriage);
	}

	public void nextTurn() {
		if (map != null && !collided)
			if (getCollisions().size > 0) {
				collided = true;
				resolveCollisions();
				collided = false;		// TODO some way of only changing this back after collision has ended
				// have an array of colliding trains to keep track of ones in the middle of a collision?
			}
	}

	private void resolveCollisions() {
		Array<AiSprite> collisions = getCollisions();
		for (int i=0; i<collisions.size; i+=2){
			AiSprite p1Train = collisions.get(i);
			AiSprite p2Train = collisions.get(i+1);
			if (p1Train.getClass() == Carriage.class) { // if player1's aiSprite is a carriage
				((Carriage) p1Train).removeCarriage();
			} else if (p2Train.getClass() == Carriage.class){ // if player2's aiSprite is a carriage
				((Carriage) p2Train).removeCarriage();
			} else { // 2 trains colliding
				// TODO account for weight of carriages also!
				if ((p1Train.getWeight()*p1Train.getSpeed()) < (p2Train.getWeight()*p2Train.getSpeed())){
					System.out.println("p1 wins!");
					p2Train.stopSprite(); // TODO temporary to show collision has occured
					Carriage carriage = ((Train) p2Train).getCarriage();
					carriage.stopSprite();
					carriage.removeCarriage();
				} else if ((p1Train.getWeight()*p1Train.getSpeed()) > (p2Train.getWeight()*p2Train.getSpeed())){
					System.out.println("p2 wins!");
					p1Train.stopSprite();
					Carriage carriage = ((Train) p1Train).getCarriage();
					carriage.stopSprite(); // not neccessarily needed but more efficient
					carriage.removeCarriage();
				} else {
					System.out.println("its a draw!");
				}
			}
		}
	}

	private Array<AiSprite> getCollisions(){
		// returns array where every even element is player1 collided train, odd element is player2 collided train
		Array<AiSprite> collidedTrains = new Array<AiSprite>();
		for (AiSprite aiSprite1: player1.getAiSprites()) {
			for (AiSprite train2: player2.getAiSprites()) {
				if (collisionOccured(aiSprite1, train2)){
					collidedTrains.add(aiSprite1);
					collidedTrains.add(train2);
				}
			}
		}
		return collidedTrains;
	}

	private boolean collisionOccured(AiSprite aiSprite1, AiSprite aiSprite2) {
		// tests whether 2 trains have collided
		Polygon poly1 = aiSprite1.getPolygon();
		Polygon poly2 = aiSprite2.getPolygon();
		if (!(aiSprite1.getClass() == Carriage.class && aiSprite2.getClass() == Carriage.class)){ // collision cannot occur between 2 carriages
			if (Intersector.overlapConvexPolygons(poly1.getTransformedVertices(), poly2.getTransformedVertices(), null)){
				return true;
			}
		}
		return false;
	}

	private Location createLocation(Scene parentScene , int x , int y) {
		Location location = new Location(parentScene, x,y);
		location.setPosition(x, y);
		Add(location);
		return location;
	}	

	private void connectLocations(Location l1, Location l2, CurvedPath path1, CurvedPath path2){
		if (!(l1.isConnected(l2))) {
			l1.addConnection(l2, path1);
			l2.addConnection(l1, path2); 
		}
	}

	private Route getSpritePath(){
		// random function that returns one of 2 given paths- used for testing
		Route route;
		route = new Route(moscow, krakow, madrid);
		return route;
	}

	private Route getSpritePath2() {
		// random function that returns one of 2 given paths- used for testing
		Route route;
		route = new Route(lisbon, madrid, krakow);
		return route;
	}

	private HashMap<String, CurvedPath> getPaths() {
		// this creates all of the paths on the map
		// TODO currently requires making a route each way, clean up the process
		HashMap<String, CurvedPath> paths = new HashMap<String, CurvedPath>();

		// first last control points must be same due to catmullromspline maths
		Vector2[] dataSet1 = new Vector2[5];
		dataSet1[0] = (new Vector2(210, 390));
		dataSet1[1] = (new Vector2(210, 390));
		dataSet1[2] = (new Vector2(255, 345));
		dataSet1[3] = (new Vector2(300, 340));
		dataSet1[4] = (new Vector2(300, 340));
		CurvedPath londonParis = new CurvedPath(dataSet1, false);
		paths.put("LondonParis", londonParis);
		curvedPaths.add(londonParis);
		
		Vector2[] rdataSet1 = reverseDataset(dataSet1);
		CurvedPath parisLondon = new CurvedPath(rdataSet1, false);
		paths.put("ParisLondon" , parisLondon);
		


		Vector2[] dataSet2 = new Vector2[6];
		dataSet2[0] = (new Vector2(300, 340));
		dataSet2[1] = (new Vector2(300, 340));
		dataSet2[2] = (new Vector2(320, 300));
		dataSet2[3] = (new Vector2(360, 250));
		dataSet2[4] = (new Vector2(415, 168));
		dataSet2[5] = (new Vector2(415, 168));
		CurvedPath parisRome = new CurvedPath(dataSet2, false);
		paths.put("ParisRome", parisRome);
		curvedPaths.add(parisRome);
		
		Vector2[] rdataSet2 = reverseDataset(dataSet2);	
		CurvedPath romeParis = new CurvedPath(rdataSet2, false);
		paths.put("RomeParis", romeParis);

		

		Vector2[] dataSet3 = new Vector2[6];
		dataSet3[0] = (new Vector2(415, 168));
		dataSet3[1] = (new Vector2(415, 168));
		dataSet3[2] = (new Vector2(405, 245));
		dataSet3[3] = (new Vector2(450, 270));
		dataSet3[4] = (new Vector2(510, 290));
		dataSet3[5] = (new Vector2(510, 290));
		CurvedPath romeKrakow = new CurvedPath(dataSet3, false);
		paths.put("RomeKrakow", romeKrakow);
		curvedPaths.add(romeKrakow);
		
		Vector2[] rdataSet3 = reverseDataset(dataSet3);
		CurvedPath krakowRome = new CurvedPath(rdataSet3, false);
		paths.put("KrakowRome", krakowRome);

		

		Vector2[] dataSet4 = new Vector2[7];
		dataSet4[0] = (new Vector2(210, 390));
		dataSet4[1] = (new Vector2(210, 390));
		dataSet4[2] = (new Vector2(210, 365));
		dataSet4[3] = (new Vector2(180, 200));
		dataSet4[4] = (new Vector2(80, 220));
		dataSet4[5] = (new Vector2(30, 120));
		dataSet4[6] = (new Vector2(30, 120));
		CurvedPath londonLisbon = new CurvedPath(dataSet4, false);
		paths.put("LondonLisbon", londonLisbon);
		curvedPaths.add(londonLisbon);
		
		Vector2[] rdataSet4 = reverseDataset(dataSet4);
		CurvedPath lisbonLondon = new CurvedPath(rdataSet4, false);
		paths.put("LisbonLondon", lisbonLondon);
		
		

		Vector2[] dataSet5 = new Vector2[4];
		dataSet5[0] = (new Vector2(520, 350));
		dataSet5[1] = (new Vector2(520, 350));
		dataSet5[2] = (new Vector2(120, 150));
		dataSet5[3] = (new Vector2(120, 150));
		CurvedPath krakowMadrid = new CurvedPath(dataSet5, false);
		paths.put("KrakowMadrid", krakowMadrid);
		curvedPaths.add(krakowMadrid);
		
		Vector2[] rdataSet5 = reverseDataset(dataSet5);
		CurvedPath madridKrakow = new CurvedPath(rdataSet5, false);
		paths.put("MadridKrakow", madridKrakow);

		

		Vector2[] dataSet6 = new Vector2[4];
		dataSet6[0] = (new Vector2(30, 120));
		dataSet6[1] = (new Vector2(30, 120));
		dataSet6[2] = (new Vector2(120, 150));
		dataSet6[3] = (new Vector2(120, 150));
		CurvedPath lisbonMadrid = new CurvedPath(dataSet6, false);
		paths.put("LisbonMadrid", lisbonMadrid);
		curvedPaths.add(lisbonMadrid);
		
		Vector2[] rdataSet6 = reverseDataset(dataSet6);
		CurvedPath madridLisbon = new CurvedPath(rdataSet6, false);
		paths.put("MadridLisbon", madridLisbon);

		

		Vector2[] dataSet7 = new Vector2[5];
		dataSet7[0] = (new Vector2(300, 340));
		dataSet7[1] = (new Vector2(300, 340));
		dataSet7[2] = (new Vector2(350, 340));
		dataSet7[3] = (new Vector2(410,400));
		dataSet7[4] = (new Vector2(410,400));
		CurvedPath parisBerlin = new CurvedPath(dataSet7, false);
		paths.put("ParisBerlin", parisBerlin);
		curvedPaths.add(parisBerlin);

		Vector2[] rdataSet7 = reverseDataset(dataSet7);
		CurvedPath berlinParis = new CurvedPath(rdataSet7, false);
		paths.put("BerlinParis", berlinParis);

		

		Vector2[] dataSet8 = new Vector2[4];
		dataSet8[0] = (new Vector2(410, 400));
		dataSet8[1] = (new Vector2(410, 400));
		dataSet8[2] = (new Vector2(510, 290));
		dataSet8[3] = (new Vector2(510, 290));
		CurvedPath berlinBudapest = new CurvedPath(dataSet8, false);
		paths.put("BerlinBudapest", berlinBudapest);
		curvedPaths.add(berlinBudapest);
		
		Vector2[] rdataSet8 = reverseDataset(dataSet8);
		CurvedPath budapestBerlin = new CurvedPath(rdataSet8, false);
		paths.put("BudapestBerlin", budapestBerlin);

		

		Vector2[] dataSet9 = new Vector2[5];
		dataSet9[0] = (new Vector2(520, 350));
		dataSet9[1] = (new Vector2(520, 350));
		dataSet9[2] = (new Vector2(700, 370));
		dataSet9[3] = (new Vector2(800, 450));
		dataSet9[4] = (new Vector2(800, 450));
		CurvedPath krakowMoscow = new CurvedPath(dataSet9, false);
		paths.put("KrakowMoscow", krakowMoscow);
		curvedPaths.add(krakowMoscow);

		Vector2[] rdataSet9 = reverseDataset(dataSet9);
		CurvedPath moscowKrakow = new CurvedPath(rdataSet9, false);
		paths.put("MoscowKrakow", moscowKrakow);

		

		Vector2[] dataSet10 = new Vector2[6];
		dataSet10[0] = (new Vector2(510, 290));
		dataSet10[1] = (new Vector2(510, 290));
		dataSet10[2] = (new Vector2(600, 290));
		dataSet10[3] = (new Vector2(700, 420));
		dataSet10[4] = (new Vector2(800, 450));
		dataSet10[5] = (new Vector2(800, 450));
		CurvedPath budapestMoscow = new CurvedPath(dataSet10, false);
		paths.put("BudapestMoscow", budapestMoscow);
		curvedPaths.add(budapestMoscow);
		
		Vector2[] rdataSet10 = reverseDataset(dataSet10);
		CurvedPath moscowBudapest = new CurvedPath(rdataSet10, false);
		paths.put("MoscowBudapest", moscowBudapest);
		
		
		return paths;
	}

	private Vector2[] reverseDataset(Vector2[] dataSet){
		Vector2[] rdataSet1 = new Vector2[dataSet.length];
		for (int i=0; i < rdataSet1.length; i++){
			rdataSet1[rdataSet1.length-i-1] = dataSet[i];
		}
		return rdataSet1;
	}
	
	@Override
	public void player1Active()
	{
		super.player1Active();
		player1.updateTurn(true);
		player2.updateTurn(false);
	}
	
	@Override
	public void player2Active()
	{
		super.player2Active();
		player2.updateTurn(true);
		player1.updateTurn(false);
	}
	
	@Override
	public void nextGoPressed()
	{
		super.nextGoPressed();
		nextTurn();
	}

	@Override
	public void goalsToolbarPressed() 
	{
		System.out.println("goalsToolbarPressed");
		Game.pushScene(goalsScene);
	}
	
	@Override
	public void shopToolbarPressed() 
	{
		System.out.println("shopToolbarPressed");
		//Switch to shop scene
		Game.pushScene(shopScene);
	}
	
	@Override
	public void resourcesToolbarPressed() 
	{
		System.out.println("resourcesToolbarPressed");
		Game.pushScene(resourceScene);
	}
	
	public Scene makeDialogueScene(String text)
	{
		dialogueScene.setText(text);
		return dialogueScene;
	}
}

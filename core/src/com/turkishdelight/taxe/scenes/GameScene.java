package com.turkishdelight.taxe.scenes;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.routing.CurvedAiSprite;
import com.turkishdelight.taxe.routing.Path;
import com.turkishdelight.taxe.worldobjects.Location;

public class GameScene extends Scene {
	Player activePlayer;
	Player player1;
	Player player2;
	Texture mapText;
	SpriteComponent map;

	private Location london;
	private Location rome;
	private Location moscow;
	private Location lisbon;
	private Location paris;
	private Location berlin;
	private Location madrid;
	private Location budapest;
	private Location krakow;
	
	CurvedAiSprite curvedSprite1;
	private int k;											// fidelity of spline 
	private Array<Vector2[]> pointsarray;					// stores array of collection of points that make up the map (used for drawing)
	private CurvedAiSprite curvedSprite2;
	
	
	public GameScene(Player player1In, Player player2In)
	{
		super();
		player1 = player1In;
		player2 = player2In;
		activePlayer = player2;
		nextTurn();
		delayedCreate();
	}
	
	public void delayedCreate()
	{
		// map setup
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.mapZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		Add(map);
		
		//Locations setup
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
		HashMap<String, CatmullRomSpline<Vector2>> paths = getPaths(); // returns all paths with their respective names
		
		connectLocations(london, paris, paths.get("LondonParis"), paths.get("ParisLondon")); // should be easier way to do this! pass in strings?
		connectLocations(paris, rome, paths.get("ParisRome"), paths.get("RomeParis"));
		connectLocations(rome, krakow, paths.get("RomeKrakow"), paths.get("KrakowRome"));
		connectLocations(london, lisbon, paths.get("LondonLisbon"), paths.get("LisbonLondon"));
		connectLocations(madrid, krakow, paths.get("MadridKrakow"), paths.get("KrakowMadrid"));
		connectLocations(lisbon, madrid, paths.get("LisbonMadrid"), paths.get("MadridLisbon"));
		connectLocations(paris, berlin, paths.get("ParisBerlin"), paths.get("BerlinParis"));
		connectLocations(berlin, budapest, paths.get("BerlinBudapest"), paths.get("BudapestBerlin"));
		connectLocations(krakow, moscow, paths.get("KrakowMoscow"), paths.get("MoscowKrakow"));
		// add train
		Texture trainTexture = new Texture("train1.png");
		curvedSprite1 = new CurvedAiSprite(this, trainTexture, Game.objectsZ, getSpritePath());
		curvedSprite1.setSize(50, 50);
		curvedSprite1.setOriginCenter();
		Add(curvedSprite1);
		player1.addTrain(curvedSprite1);
		
		curvedSprite2 = new CurvedAiSprite(this, trainTexture, Game.objectsZ, getSpritePath2());
		curvedSprite2.setSize(50, 50);
		curvedSprite2.setOriginCenter();
		Add(curvedSprite2);
		player1.addTrain(curvedSprite2);
		// create route (with dotted line)
		Texture text = new Texture("route.png");
		float distance = 0; // used to work out distance along path
		final int divider = 5; // distance between 2 dots
		// calculate length between each point in each path- only draw a dot icon when the distance is >= divider
		for (Vector2[] points:pointsarray){
			for (int i = 1; i<k; i++) {
				if (distance < divider) {											
					distance += points[i].dst(points[i-1]);
				} else {
					SpriteComponent route = new SpriteComponent(this, text, Game.mapZ);
					route.setSize(2, 2);
					route.setPosition(points[i].x+2, points[i].y+2);
					Add(route);
					distance = distance - divider;
				}
			}
		}
		
		// add button for moving on to next turn
		Button b = new Button(this) {
			@Override
			public void onClickEnd()
			{
				nextTurn();
			}
		};
		b.setPosition(Game.targetWindowsWidth / 2, Game.targetWindowsHeight / 2);
		Add(b);
	}
	
	private Path getSpritePath2() {
		// TODO Auto-generated method stub
		Path path;
		if (MathUtils.randomBoolean()) {
			path = new Path(berlin, budapest);
		}
		else {
			path = new Path(paris, rome);
		}
		return path;
	}

	private void connectLocations(Location l1, Location l2, CatmullRomSpline<Vector2> path1, CatmullRomSpline<Vector2> path2){
		if (!(l1.isConnected(l2))) {
			l1.addConnection(l2, path1);
			l2.addConnection(l1, path2); 
		}
	}
	
	private Location createLocation(Scene parentScene , int x , int y) {
		Location location = new Location(parentScene, x,y);
		location.setPosition(x, y);
		Add(location);
		return location;
	}
	
	private Path getSpritePath(){
		// random function that returns one of 2 given paths- used for testing
		Path path;
		if (MathUtils.randomBoolean()) {
			path = new Path(krakow, madrid);
		}
		else {
			path = new Path(madrid, krakow);
		}
		return path;
	}
	
	private HashMap<String, CatmullRomSpline<Vector2>> getPaths() {
		// this creates all of the paths on the map
		// TODO currently requires making a path each way- is this necessary?
		HashMap<String, CatmullRomSpline<Vector2>> paths = new HashMap<String, CatmullRomSpline<Vector2>>();
		
		k = 100; // spline fidelity
		pointsarray = new Array<Vector2[]>();
		
		// first last control points must be same due to catmull rom spline maths
		Vector2[] dataSet1 = new Vector2[6];
		dataSet1[0] = (new Vector2(210, 390));
		dataSet1[1] = (new Vector2(210, 390));
		dataSet1[2] = (new Vector2(255, 365));
		dataSet1[3] = (new Vector2(270, 357));
		dataSet1[4] = (new Vector2(300, 340));
		dataSet1[5] = (new Vector2(300, 340));
		CatmullRomSpline<Vector2> londonParis = new CatmullRomSpline<Vector2>(dataSet1, false);

		Vector2[] points1 = new Vector2[k]; // collection of points on curve 
		for (int i = 0; i <k; ++i) {
			points1[i] = new Vector2();
			londonParis.valueAt(points1[i], ((float) i)/((float)k-1));
		}
		paths.put("LondonParis", londonParis);
		pointsarray.add(points1);
		
		Vector2[] rdataSet1 = reverseDataset(dataSet1);
		CatmullRomSpline<Vector2> parisLondon = new CatmullRomSpline<Vector2>(rdataSet1, false);
		paths.put("ParisLondon" , parisLondon);
		
		
		Vector2[] dataSet2 = new Vector2[6];
		dataSet2[0] = (new Vector2(300, 340));
		dataSet2[1] = (new Vector2(300, 340));
		dataSet2[2] = (new Vector2(320, 300));
		dataSet2[3] = (new Vector2(360, 250));
		dataSet2[4] = (new Vector2(415, 168));
		dataSet2[5] = (new Vector2(415, 168));
		CatmullRomSpline<Vector2> parisRome = new CatmullRomSpline<Vector2>(dataSet2, false);
		
		Vector2[] points2 = new Vector2[k];
		for (int i = 0; i <k; i++) {
			points2[i] = new Vector2();
			parisRome.valueAt(points2[i], ((float) i)/((float)k-1));
		}
		paths.put("ParisRome", parisRome);
		pointsarray.add(points2);
		
		Vector2[] rdataSet2 = reverseDataset(dataSet2);	
		CatmullRomSpline<Vector2> romeParis = new CatmullRomSpline<Vector2>(rdataSet2, false);
		paths.put("RomeParis", romeParis);
		
		
		Vector2[] dataSet3 = new Vector2[6];
		dataSet3[0] = (new Vector2(415, 168));
		dataSet3[1] = (new Vector2(415, 168));
		dataSet3[2] = (new Vector2(405, 245));
		dataSet3[3] = (new Vector2(450, 270));
		dataSet3[4] = (new Vector2(510, 290));
		dataSet3[5] = (new Vector2(510, 290));
		CatmullRomSpline<Vector2> romeKrakow = new CatmullRomSpline<Vector2>(dataSet3, false);
		
		Vector2[] points3 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points3[i] = new Vector2();
			romeKrakow.valueAt(points3[i], ((float) i)/((float)k-1));
		}
		paths.put("RomeKrakow", romeKrakow);
		pointsarray.add(points3);
		
		Vector2[] rdataSet3 = reverseDataset(dataSet3);
		CatmullRomSpline<Vector2> krakowRome = new CatmullRomSpline<Vector2>(rdataSet3, false);
		paths.put("KrakowRome", krakowRome);
		
		
		Vector2[] dataSet4 = new Vector2[7];
		dataSet4[0] = (new Vector2(210, 390));
		dataSet4[1] = (new Vector2(210, 390));
		dataSet4[2] = (new Vector2(210, 365));
		dataSet4[3] = (new Vector2(180, 200));
		dataSet4[4] = (new Vector2(80, 220));
		dataSet4[5] = (new Vector2(30, 120));
		dataSet4[6] = (new Vector2(30, 120));
		CatmullRomSpline<Vector2> londonLisbon = new CatmullRomSpline<Vector2>(dataSet4, false);
		
		Vector2[] points4 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points4[i] = new Vector2();
			londonLisbon.valueAt(points4[i], ((float) i)/((float)k-1));
		}
		paths.put("LondonLisbon", londonLisbon);
		pointsarray.add(points4);
		
		Vector2[] rdataSet4 = reverseDataset(dataSet4);
		CatmullRomSpline<Vector2> lisbonLondon = new CatmullRomSpline<Vector2>(rdataSet4, false);
		paths.put("LisbonLondon", lisbonLondon);
		
		Vector2[] dataSet5 = new Vector2[4];
		dataSet5[0] = (new Vector2(520, 350));
		dataSet5[1] = (new Vector2(520, 350));
		dataSet5[2] = (new Vector2(120, 150));
		dataSet5[3] = (new Vector2(120, 150));
		CatmullRomSpline<Vector2> krakowMadrid = new CatmullRomSpline<Vector2>(dataSet5, false);
		
		Vector2[] points5 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points5[i] = new Vector2();
			krakowMadrid.valueAt(points5[i], ((float) i)/((float)k-1));
		}
		paths.put("KrakowMadrid", krakowMadrid);
		pointsarray.add(points5);
		
		Vector2[] rdataSet5 = reverseDataset(dataSet5);
		CatmullRomSpline<Vector2> madridKrakow = new CatmullRomSpline<Vector2>(rdataSet5, false);
		paths.put("MadridKrakow", madridKrakow);
		
		Vector2[] dataSet6 = new Vector2[4];
		dataSet6[0] = (new Vector2(30, 120));
		dataSet6[1] = (new Vector2(30, 120));
		dataSet6[2] = (new Vector2(120, 150));
		dataSet6[3] = (new Vector2(120, 150));
		CatmullRomSpline<Vector2> lisbonMadrid = new CatmullRomSpline<Vector2>(dataSet6, false);
		
		Vector2[] points6 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points6[i] = new Vector2();
			lisbonMadrid.valueAt(points6[i], ((float) i)/((float)k-1));
		}
		paths.put("LisbonMadrid", lisbonMadrid);
		pointsarray.add(points6);
		
		Vector2[] rdataSet6 = reverseDataset(dataSet6);
		CatmullRomSpline<Vector2> madridLisbon = new CatmullRomSpline<Vector2>(rdataSet6, false);
		paths.put("MadridLisbon", madridLisbon);
		
		Vector2[] dataSet7 = new Vector2[5];
		dataSet7[0] = (new Vector2(300, 340));
		dataSet7[1] = (new Vector2(300, 340));
		dataSet7[2] = (new Vector2(350, 340));
		dataSet7[3] = (new Vector2(410,400));
		dataSet7[4] = (new Vector2(410,400));
		CatmullRomSpline<Vector2> parisBerlin = new CatmullRomSpline<Vector2>(dataSet7, false);
		
		Vector2[] points7 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points7[i] = new Vector2();
			parisBerlin.valueAt(points7[i], ((float) i)/((float)k-1));
		}
		paths.put("ParisBerlin", parisBerlin);
		pointsarray.add(points7);
		
		Vector2[] rdataSet7 = reverseDataset(dataSet7);
		CatmullRomSpline<Vector2> berlinParis = new CatmullRomSpline<Vector2>(rdataSet7, false);
		paths.put("BerlinParis", berlinParis);
		
		Vector2[] dataSet8 = new Vector2[4];
		dataSet8[0] = (new Vector2(410, 400));
		dataSet8[1] = (new Vector2(410, 400));
		dataSet8[2] = (new Vector2(510, 290));
		dataSet8[3] = (new Vector2(510, 290));
		CatmullRomSpline<Vector2> berlinBudapest = new CatmullRomSpline<Vector2>(dataSet8, false);
		
		Vector2[] points8 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points8[i] = new Vector2();
			berlinBudapest.valueAt(points8[i], ((float) i)/((float)k-1));
		}
		paths.put("BerlinBudapest", berlinBudapest);
		pointsarray.add(points8);
		
		Vector2[] rdataSet8 = reverseDataset(dataSet8);
		CatmullRomSpline<Vector2> budapestBerlin = new CatmullRomSpline<Vector2>(rdataSet8, false);
		paths.put("BudapestBerlin", budapestBerlin);
		
		
		Vector2[] dataSet9 = new Vector2[5];
		dataSet9[0] = (new Vector2(520, 350));
		dataSet9[1] = (new Vector2(520, 350));
		dataSet9[2] = (new Vector2(700, 370));
		dataSet9[3] = (new Vector2(800, 450));
		dataSet9[4] = (new Vector2(800, 450));
		CatmullRomSpline<Vector2> krakowMoscow = new CatmullRomSpline<Vector2>(dataSet9, false);
		
		Vector2[] points9 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points9[i] = new Vector2();
			krakowMoscow.valueAt(points9[i], ((float) i)/((float)k-1));
		}
		paths.put("KrakowMoscow", krakowMoscow);
		pointsarray.add(points9);
		
		Vector2[] rdataSet9 = reverseDataset(dataSet9);
		CatmullRomSpline<Vector2> moscowKrakow = new CatmullRomSpline<Vector2>(rdataSet9, false);
		paths.put("MoscowKrakow", moscowKrakow);
		
		
		
		Vector2[] dataSet10 = new Vector2[6];
		dataSet10[0] = (new Vector2(510, 290));
		dataSet10[1] = (new Vector2(510, 290));
		dataSet10[2] = (new Vector2(600, 290));
		dataSet10[3] = (new Vector2(700, 420));
		dataSet10[4] = (new Vector2(800, 450));
		dataSet10[5] = (new Vector2(800, 450));
		CatmullRomSpline<Vector2> budapestMoscow = new CatmullRomSpline<Vector2>(dataSet10, false);
		
		Vector2[] points10 = new Vector2[k];
		for (int i = 0; i <k; ++i) {
			points10[i] = new Vector2();
			budapestMoscow.valueAt(points10[i], ((float) i)/((float)k-1));
		}
		paths.put("BudapestMoscow", budapestMoscow);
		pointsarray.add(points10);
		
		Vector2[] rdataSet10 = reverseDataset(dataSet10);
		CatmullRomSpline<Vector2> moscowBudapest = new CatmullRomSpline<Vector2>(rdataSet10, false);
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
	
	public void nextTurn()
	{
		if(activePlayer == player1)
		{
			System.out.println("ActivePlayer = 1");
			activePlayer = player2;
			player2.updateTurn(true);
			player1.updateTurn(false);
		}
		else if(activePlayer == player2)
		{
			System.out.println("ActivePlayer = 2");
			activePlayer = player1;
			player1.updateTurn(true);
			player2.updateTurn(false);
		}
	}
}

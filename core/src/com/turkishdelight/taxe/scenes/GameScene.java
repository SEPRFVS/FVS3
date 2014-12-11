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
	
	CurvedAiSprite curvedSprite;
	private int k;											// fidelity of spline 
	private Array<Vector2[]> pointsarray;					// stores array of collection of points that make up the map (used for drawing)
	
	
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
		
		// add train
		Texture trainTexture = new Texture("train1.png");
		curvedSprite = new CurvedAiSprite(this, trainTexture, Game.objectsZ, getSpritePath());
		curvedSprite.setSize(50, 50);
		curvedSprite.setOriginCenter();
		Add(curvedSprite);
		player1.addTrain(curvedSprite);
		
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
			path = new Path(london, paris, london);
		}
		else {
			path = new Path(paris, rome, krakow);
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

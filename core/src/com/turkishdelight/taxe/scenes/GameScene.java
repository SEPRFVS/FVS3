package com.turkishdelight.taxe.scenes;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.turkishdelight.taxe.Game;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.Scene;
import com.turkishdelight.taxe.SpriteComponent;
import com.turkishdelight.taxe.guiobjects.Button;
import com.turkishdelight.taxe.routing.Connection;
import com.turkishdelight.taxe.routing.CurvedAiSprite;
import com.turkishdelight.taxe.routing.Path;
import com.turkishdelight.taxe.worldobjects.Location;

public class GameScene extends Scene {
	Player activePlayer;
	Player player1;
	
	Player player2;
	Texture mapText;
	SpriteComponent map;

	private ShapeRenderer sr;
	private Array<Connection> masterPath;

	private Location london;
	private Location rome;
	private Location moscow;
	private Location lisbon;
	private Location paris;
	
	CurvedAiSprite curvedSprite;
	
	private int k;
	private Array<Vector2[]> pointsarray;
	
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
		sr = new ShapeRenderer();
		
		mapText = new Texture("map.png");
		map = new SpriteComponent(this, mapText, Game.mapZ);
		map.setPosition(0, 0);
		map.setSize(Game.targetWindowsWidth, Game.targetWindowsHeight);
		
		// setup base sprite
		Texture texture = new Texture("train1.png");
	
		//Locations setup
		london = createLocation(this, 210, 390);
		london.setPosition(210, 390);

		rome = createLocation(this, 604, 168);
		rome.setPosition(604, 168);

		moscow = createLocation(this, 800, 500);
		moscow.setPosition(800,500);

		lisbon = createLocation(this, 100, 200);
		lisbon.setPosition(100, 200);

		paris = createLocation(this, 400, 400);
		paris.setPosition(400,400); 
		
		System.out.println("Locations created");
		
		// setup connections
		HashMap<String, CatmullRomSpline<Vector2>> paths = getPaths(); // returns all paths with their respective names
		masterPath = new Array<Connection>(); // contains all of points for drawing the route initially
		connectLocations(london, paris, paths.get("LondonParis"), paths.get("ParisLondon")); // should be easier way to do this! pass in strings?
		connectLocations(paris, rome, paths.get("ParisRome"), paths.get("RomeParis"));
		connectLocations(rome, moscow, paths.get("RomeMoscow"), paths.get("MoscowRome"));
		
		// add sprite component
		Add(london);
		Add(rome);
		Add(lisbon);
		Add(moscow);
		Add(paris);
		Add(map);
		
		// add train
		curvedSprite = new CurvedAiSprite(this, texture, Game.objectsZ, getSpritePath());
		curvedSprite.setSize(50, 50);
		curvedSprite.setOriginCenter();
		Add(curvedSprite);
		player1.addTrain(curvedSprite);
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
	
	@Override
	public void Draw(SpriteBatch batch) {
		super.Draw(batch);
		
		// draw train
		curvedSprite.draw(batch);
		
		// draw curved lines
		sr.begin(ShapeType.Line);
		for (Vector2[] points:pointsarray){
			for (int i = 0; i<k-1; ++i) {
				sr.line(points[i], points[i+1]); 
			}
		}
		sr.end();

		/*
		// draws points that current route is taking - untested with curves
		sr.begin(ShapeType.Filled);
		for (AISprite aiSprite :aiSprites) 
			for (Vector2 waypoint :aiSprite.getPath())
				sr.circle(waypoint.x, waypoint.y, 5); // every waypoint has circle (for ease of seeing them!)
		sr.end();*/
	}
	
	private void connectLocations(Location l1, Location l2, CatmullRomSpline<Vector2> path1, CatmullRomSpline<Vector2> path2){
		// currently untested
		if (!(l1.isConnected(l2))) {
			l1.addConnection(l2, path1);
			l2.addConnection(l1, path2); // may need to add it in reverse
			
			masterPath.add(new Connection(l1, path1));
		}
	}
	
	private Location createLocation(Scene parentScene , int x , int y) {
		// probably going to be easier to get rid of this, unless you can use set position here?
		Location location = new Location(parentScene, x,y);
		return location;
	}
	
	private Path getSpritePath(){
		Path path;
		if (MathUtils.randomBoolean()) {
			path = new Path(london, paris, london);
		}
		else {
			path = new Path(paris, rome, moscow);
		}
		return path;
	}
	
	private HashMap<String, CatmullRomSpline<Vector2>> getPaths() {
		// this creates all of the paths on the map
		// TODO currently requires making a path each way- is this necessary?
		HashMap<String, CatmullRomSpline<Vector2>> paths = new HashMap<String, CatmullRomSpline<Vector2>>();
		
		k = 100; // spline fidelity
		Vector2[] points1 = new Vector2[k]; // collection of points on curve 
		pointsarray = new Array<Vector2[]>();
		
		// TODO first, last lines arent drawn correctly-why?
		Vector2[] dataSet1 = new Vector2[6];
		dataSet1[0] = (new Vector2(210, 390));
		dataSet1[1] = (new Vector2(210, 390));
		dataSet1[2] = (new Vector2(260, 394));
		dataSet1[3] = (new Vector2(350, 396));
		dataSet1[4] = (new Vector2(400, 400));
		dataSet1[5] = (new Vector2(400, 400));
		CatmullRomSpline<Vector2> londonParis = new CatmullRomSpline<Vector2>(dataSet1, false);
		for (int i = 0; i <k; ++i) {
			points1[i] = new Vector2();
			londonParis.valueAt(points1[i], ((float) i)/((float)k-1));
		}
		paths.put("LondonParis", londonParis);
		pointsarray.add(points1);
		
		
		
		Vector2[] rdataSet1 = reverseDataset(dataSet1);
		CatmullRomSpline<Vector2> parisLondon = new CatmullRomSpline<Vector2>(rdataSet1, false);
		Vector2[] rpoints1 = new Vector2[k];
		for (int i = 0; i< k ; i++) {
			rpoints1[i] = new Vector2();
			parisLondon.valueAt(rpoints1[i], ((float) i)/((float)k-1));
		}
		paths.put("ParisLondon" , parisLondon);
		
		
		
		Vector2[] points2 = new Vector2[k];
		Vector2[] dataSet2 = new Vector2[6];
		dataSet2[0] = (new Vector2(400, 400));
		dataSet2[1] = (new Vector2(400, 400));
		dataSet2[2] = (new Vector2(500, 300));
		dataSet2[3] = (new Vector2(550, 200));
		dataSet2[4] = (new Vector2(604, 168));
		dataSet2[5] = (new Vector2(604, 168));
		CatmullRomSpline<Vector2> parisRome = new CatmullRomSpline<Vector2>(dataSet2, false);
		for (int i = 0; i <k; ++i) {
			points2[i] = new Vector2();
			parisRome.valueAt(points2[i], ((float) i)/((float)k-1));
		}
		paths.put("ParisRome", parisRome);
		pointsarray.add(points2);
		
		
		Vector2[] rdataSet2 = reverseDataset(dataSet2);	
		CatmullRomSpline<Vector2> romeParis = new CatmullRomSpline<Vector2>(rdataSet2, false);
		Vector2[] rpoints2 = new Vector2[k];
		for (int i = 0; i< k ; i++) {
			rpoints2[i] = new Vector2();
			parisLondon.valueAt(rpoints2[i], ((float) i)/((float)k-1));
		}
		paths.put("RomeParis", romeParis);
		
		
		
		
		Vector2[] points3 = new Vector2[k];
		Vector2[] dataSet3 = new Vector2[6];
		dataSet3[0] = (new Vector2(604, 168));
		dataSet3[1] = (new Vector2(604, 168));
		dataSet3[2] = (new Vector2(600, 300));
		dataSet3[3] = (new Vector2(600, 450));
		dataSet3[4] = (new Vector2(800, 500));
		dataSet3[5] = (new Vector2(800, 500));
		CatmullRomSpline<Vector2> romeMoscow = new CatmullRomSpline<Vector2>(dataSet3, false);
		for (int i = 0; i <k; ++i) {
			points3[i] = new Vector2();
			romeMoscow.valueAt(points3[i], ((float) i)/((float)k-1));
		}
		paths.put("RomeMoscow", romeMoscow);
		pointsarray.add(points3);
	
		
		
		Vector2[] rdataSet3 = reverseDataset(dataSet3);
		CatmullRomSpline<Vector2> moscowRome = new CatmullRomSpline<Vector2>(rdataSet3, false);
		Vector2[] rpoints3 = new Vector2[k];
		for (int i = 0; i< k ; i++) {
			rpoints3[i] = new Vector2();
			parisLondon.valueAt(rpoints3[i], ((float) i)/((float)k-1));
		}
		paths.put("MoscowRome", moscowRome);
		
		
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

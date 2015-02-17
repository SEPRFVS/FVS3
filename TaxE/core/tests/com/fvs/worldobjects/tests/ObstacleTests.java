package com.fvs.worldobjects.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Player;
import com.fvs.taxe.routing.CurvedPath;
import com.fvs.taxe.routing.Route;
import com.fvs.taxe.routing.Train;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.testrunners.GdxTestRunner;
import com.fvs.taxe.worldobjects.Junction;
import com.fvs.taxe.worldobjects.RouteLocation;
import com.fvs.taxe.worldobjects.Station;
import com.fvs.taxe.worldobjects.obstacles.Obstacle;
import com.fvs.taxe.worldobjects.obstacles.Obstacle.Type;
import com.fvs.taxe.worldobjects.obstacles.StationObstacle;

@RunWith(GdxTestRunner.class)
public class ObstacleTests {
	private static Texture texture;
	private static GameScene parentScene;
	private static Station station;
	private static Player player1;
	private static Player player2;
	private static Route route;
	private static CurvedPath londonParis;
	private static CurvedPath parisBerlin;
	private static CurvedPath berlinj1;
	private Train train;
	private static Route route1;
	private static CurvedPath j1Rome;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		texture = new Texture("electric.png");
		player1 = new Player();
		player1.setName("player1");
		player2 = new Player();
		player1.setStartLocation("London");
		player1.setFuel(700);
		player2.setFuel(700);
		player2.setStartLocation("Moscow");
		parentScene = new GameScene(player1, player2);
		
		Station london = new Station(parentScene, "London", 100, 100);
		Station paris = new Station(parentScene, "Paris", 500, 100);
		Station berlin = new Station(parentScene, "Berlin", 900, 100);
		Junction j1 = new Junction(parentScene, "J1", 1000, 100);
		Station rome = new Station(parentScene, "Rome", 1100, 100);
		
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(100, 100));
		dataSet1[1] = (new Vector2(100, 100));
		dataSet1[2] = (new Vector2(500, 100));
		dataSet1[3] = (new Vector2(500, 100));
		londonParis = new CurvedPath(dataSet1, false);
		
		Vector2[] dataSet2 = new Vector2[4];
		dataSet2[0] = (new Vector2(500, 100));
		dataSet2[1] = (new Vector2(500, 100));
		dataSet2[2] = (new Vector2(900, 100));
		dataSet2[3] = (new Vector2(900, 100));
		parisBerlin = new CurvedPath(dataSet2, false);
		
		Vector2[] dataSet3 = new Vector2[4];
		dataSet3[0] = (new Vector2(900, 100));
		dataSet3[1] = (new Vector2(900, 100));
		dataSet3[2] = (new Vector2(1000, 100));
		dataSet3[3] = (new Vector2(1000, 100));
		berlinj1 = new CurvedPath(dataSet3, false);
		
		Vector2[] dataSet4 = new Vector2[4];
		dataSet4[0] = (new Vector2(1000, 100));
		dataSet4[1] = (new Vector2(1000, 100));
		dataSet4[2] = (new Vector2(1100, 100));
		dataSet4[3] = (new Vector2(1100, 100));
		j1Rome = new CurvedPath(dataSet4, false);
		
		
		london.addConnection(paris, londonParis);
		paris.addConnection(berlin, parisBerlin);
		berlin.addConnection(j1, berlinj1);
		j1.addConnection(rome, j1Rome);
		
		ArrayList<RouteLocation> routeLocations = new ArrayList<RouteLocation>();
		routeLocations.add(london);
		routeLocations.add(paris);
		routeLocations.add(berlin);
		route = new Route(routeLocations);
		
		ArrayList<RouteLocation> route1Locations = new ArrayList<RouteLocation>();
		route1Locations.add(berlin);
		route1Locations.add(j1);
		route1Locations.add(rome);
		route1 = new Route(route1Locations);
		
		station = london;
	}
	
	@Before 
	public void beforeTest(){
		train = new Train(parentScene,player1,"train1", texture,station,10,10,10,0f);
	}
	
	@Test
	public void testPlaceObstactle() {
		Obstacle obstacle  = new StationObstacle(parentScene, ((Station) parentScene.getStationByName("Paris")));
		try {
			parentScene.generateObstacle(parentScene.getStationByName("Paris"), Type.STATION);
			assertEquals(parentScene.getStationByName("Paris").getObstacle().getClass(), obstacle.getClass());
		} catch (Exception e){
			//Fail
			assertEquals(true, false);
		}
	}
}

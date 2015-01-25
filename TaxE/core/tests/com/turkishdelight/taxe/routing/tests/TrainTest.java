package com.turkishdelight.taxe.routing.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.routing.Carriage;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.routing.Route;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.scenes.GameScene;
import com.turkishdelight.taxe.testrunners.GdxTestRunner;
import com.turkishdelight.taxe.worldobjects.Junction;
import com.turkishdelight.taxe.worldobjects.RouteLocation;
import com.turkishdelight.taxe.worldobjects.Station;

@RunWith(GdxTestRunner.class)
public class TrainTest {

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
	private Carriage carriage;
	private static Route route1;
	private static CurvedPath j1Rome;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		texture = new Texture("elec1.png");
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
		carriage = new Carriage(parentScene, texture, player1, station, train);
		train.setCarriage(carriage);
	}
	
	@Test
	public void testUpdateTurnNullRoute() {
		// test updateTurn if no route is set
		// will return that train doesnt move
		train.setRoute(null);
		Vector2 previousPosition = train.getPosition();
		train.updateTurn();
		Vector2 newPosition = train.getPosition();
		assertEquals(newPosition, previousPosition);
	}
	
	@Test
	public void testUpdateTurnCompletedRoute() {
		// test updateTurn if completed
		// will return that train doesnt move
		train.restoreRoute(route, 1, 1);
		train.updatePosition();
		Vector2 expectedPosition = train.getPosition();
		train.updateTurn();
		Vector2 actualPosition = train.getPosition();

		assertEquals(expectedPosition, actualPosition);
	}
	
	@Test
	public void testUpdateTurnPartlyAlongRoute() {
		// test UpdateTurn if partly along route
		// will return that train has moved position
		train.restoreRoute(route, 1, 0.5f);
		train.updatePosition();
		Vector2 expectedPosition = train.getPosition();
		train.updateTurn();
		Vector2 actualPosition = train.getPosition();

		assertNotEquals(expectedPosition, actualPosition);
	}

	@Test
	public void testOnClickEndNotSelectingRoute() {
		// test onClickEnd if the parent game scene isnt in route selection mode
		// the parent scene's selected trains should be null (as train not clickable)
		train.onClickEnd();
		Train trainActual = parentScene.getSelectedTrain();
		assertNull(trainActual);
	}
	
	@Test
	public void testOnClickEndSelectingRoute() {
		// test onClickEnd if the parent game scene is in route selection mode
		// the parent scene's selected trains should be the train
		parentScene.startSelectingRoute();
		train.onClickEnd();
		Train trainActual = parentScene.getSelectedTrain();
		assertEquals(train, trainActual);
		parentScene.endSelectingRoute();
	}

	@Test
	public void testUpdatePositionNullRoute() {
		// test updatePosition on a null route (shouldnt occur, blocked by updateTurn)
		// will return that no movement has occured
		train.setRoute(null);
		Vector2 expectedPosition = train.getPosition();
		train.updatePosition();
		Vector2 actualPosition = train.getPosition();
		
		assertEquals(expectedPosition, actualPosition);
		
	}
	
	@Test
	public void testUpdatePositionReachingStation() {
		// test updatePosition when train is going to reach station in that move
		// will return that train is fixed to the station
		train.restoreRoute(route, 0, 0.98f);
		train.updatePosition();
		assertNotNull(train.getStation());
		
		Vector2 expectedPosition = new Vector2(500-train.getWidth()/2,100-train.getHeight()/2); // 500, 100 take away the correction for centralising the sprite
		Vector2 actualPostion = train.getPosition();
		assertEquals(expectedPosition, actualPostion);
	}
	
	@Test
	public void testUpdatePositionReachingJunction() {
		// test updatePosition when train is going to reach junction in that move
		// will return that train passed the junction, moved by usual distance (train's speed)
		train.restoreRoute(route1, 0, 0.98f);
		
		float previousDistance = train.getRouteDistance();
		Vector2 previousPosition = train.getPosition();
		train.updatePosition();
		
		CurvedPath expectedPath = j1Rome;
		CurvedPath actualPath = train.getPath();
		assertEquals(expectedPath, actualPath);
		System.out.println(train.getPathDistance());
		float expectedDistance = previousDistance + train.getSpeed();
		float actualDistance = train.getRouteDistance();
		assertEquals(expectedDistance, actualDistance, 4);
		
		Vector2 newPosition = train.getPosition();
		assertNotEquals(previousPosition, newPosition);
		
		Vector2 expectedPosition = new Vector2(previousPosition.x + train.getSpeed(),  previousPosition.y);
		Vector2 actualPosition = train.getPosition();

		assertEquals(expectedPosition.x, actualPosition.x, 1);
		assertEquals(expectedPosition.y, actualPosition.y, 1);
	}
	
	@Test
	public void testUpdatePositionFinalWaypointRoute() {
		// test updatePosition with train reaching final waypoint in route in that move
		// willreturn that the train's position is the final station's location
		train.restoreRoute(route, 1, 0.98f);
		train.updatePosition();
		assertNotNull(train.getStation());
		
		Vector2 expectedPosition = new Vector2(900-train.getWidth()/2,100-train.getHeight()/2); // should be at berlin
		Vector2 actualPostion = train.getPosition();
		assertEquals(expectedPosition, actualPostion);
	}
	
	@Test
	public void testUpdatePositionPartiallyAlongPath() {
		// test updatePosition with train partially along a path
		// will return that the train's position is the old position + train's speed
		train.restoreRoute(route, 0, 0.5f);
		float previousDistance = train.getRouteDistance();
		Vector2 previousPosition = train.getPosition();
		train.updatePosition();
		
		float newDistance = previousDistance + train.getSpeed();
		float actualDistance = train.getRouteDistance();
		assertEquals(newDistance, actualDistance, 4);
		
		Vector2 newPosition = train.getPosition();
		assertNotEquals(previousPosition, newPosition);
		
		Vector2 expectedPosition = new Vector2(previousPosition.x + train.getSpeed(),  previousPosition.y);
		Vector2 actualPosition = train.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x, 1);
		assertEquals(expectedPosition.y, actualPosition.y, 1);
	}

	@Test
	public void testRestoreRouteNull() {
		// test restoreRoute with a null route
		// will return that the train has a null route
		train.restoreRoute(null, 0, 0);
		assertNull(train.getRoute());
	}
	
	@Test
	public void testRestoreRouteValidRoute() {
		// test restoreROute with a valid route (all locations connected)
		// will return that train is set to halfway position along path
		float expectedCurrent = 0.5f;
		int expectedWaypoint = 0;
		train.restoreRoute(route, expectedWaypoint, expectedCurrent);
		assertEquals(train.getRoute(), route);
		assertEquals(expectedWaypoint, train.getWaypoint());
		assertEquals(expectedCurrent, train.getCurrent(), 4);
		Vector2 expectedPosition = new Vector2(300-train.getWidth()/2,100-train.getHeight()/2);
		Vector2 actualPosition = train.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x,1 );
		assertEquals(expectedPosition.y, actualPosition.y,1 );
	}
	
	@Test
	public void testRestoreRouteEndOfRoute() {
		// test restoreROute with a valid route (all locations connected)
		// will return that train is set to halfway position along path
		float expectedCurrent = 1f;
		int expectedWaypoint = 1;
		train.restoreRoute(route, expectedWaypoint, expectedCurrent);
		assertEquals(train.getRoute(), route);
		assertEquals(expectedWaypoint, train.getWaypoint());
		assertEquals(expectedCurrent, train.getCurrent(), 4);
		Vector2 expectedPosition = new Vector2(900-train.getWidth()/2,100-train.getHeight()/2);
		Vector2 actualPosition = train.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x,1 );
		assertEquals(expectedPosition.y, actualPosition.y,1 );
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRestoreRouteInvalidWaypoint() {
		// test restoring route with an invalid waypoint (>route size -2)
		// will throw exception
		train.restoreRoute(route, 2, 0.5f);
	}


	@Test
	public void testSetUpgrade() {
		// test setUpgrade ffr each of the 3 possible upgrades
		// will return that the correct upgrade is set
		train.setUpgrade(0);
		assertTrue(train.getUpgrade(0));
		
		train.setUpgrade(1);
		assertTrue(train.getUpgrade(1));
		
		train.setUpgrade(2);
		assertTrue(train.getUpgrade(2));
	}
	
	@Test 
	public void testSetInvalidUpgrade() {
		// test setUpgrade for an invalid upgrade (<0 or > 2)
		// will throw exception
		train.setUpgrade(28);
	}
	
	@Test
	public void testGetStationNotAtStation() {
		// test getStation for when the train is not at a station
		// will return null
		train.restoreRoute(route, 0, 0.5f);
		Station station = train.getStation();
		assertNull(station);
	}
	
	@Test
	public void testGetStationAtStartStation() {
		// test getStation whilst train is set to startlocation 
		// will return station (london)
		Station station = train.getStation();
		assertEquals("London", station.getName());
	}
	
	@Test
	public void testGetStationAtStation() {
		// test getStation when train is reaching intermediate station along route
		// will return correct station (paris)
		train.restoreRoute(route, 0, 0.99f);
		train.updatePosition();
		Station station = train.getStation();
		assertEquals("Paris", station.getName());
	}
	
	// tests for null arguments passed int the constructor
	// will throw an exception
	@Test (expected = IllegalArgumentException.class)
	public void testTrainNullName() {
		@SuppressWarnings("unused")
		Train train = new Train(parentScene, player1, null, texture, station, 1, 1, 1, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testTrainInvalidWeight() {
		@SuppressWarnings("unused")
		Train train = new Train(parentScene, player1, "train1", texture, station, -1, 1, 1, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testTrainNullInvalidSpeed() {
		@SuppressWarnings("unused")
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, -1, 1, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testTrainNullInvalidFuelEfficiency() {
		@SuppressWarnings("unused")
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, 1, -1, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testTrainNullInvalidReliability() {
		@SuppressWarnings("unused")
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, 1, 1, -1);
	}
}

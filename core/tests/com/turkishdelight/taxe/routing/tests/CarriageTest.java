package com.turkishdelight.taxe.routing.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.routing.Carriage;
import com.turkishdelight.taxe.routing.Connection;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.routing.Route;
import com.turkishdelight.taxe.routing.Train;
import com.turkishdelight.taxe.scenes.GameScene;
import com.turkishdelight.taxe.testrunners.GdxTestRunner;
import com.turkishdelight.taxe.worldobjects.RouteLocation;
import com.turkishdelight.taxe.worldobjects.Station;


@RunWith(GdxTestRunner.class)
public class CarriageTest {

	private static Texture texture;
	private static GameScene parentScene;
	private static Station station;
	private static Player player1;
	private static CurvedPath londonParis;
	private static CurvedPath parisBerlin;
	private static Player player2;
	private static Route route;
	private static Station berlin;
	private static Station paris;
	private Train train;
	private Carriage carriage;
	

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
		paris = new Station(parentScene, "Paris", 100, 500);
		berlin = new Station(parentScene, "Berlin", 100, 900);
		
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
		
		london.addConnection(paris, londonParis);
		paris.addConnection(berlin, parisBerlin);
		
		System.out.println(parisBerlin);
		System.out.println(londonParis);
		ArrayList<RouteLocation> routeLocations = new ArrayList<RouteLocation>();
		routeLocations.add(london);
		routeLocations.add(paris);
		routeLocations.add(berlin);
		route = new Route(routeLocations);
		System.out.println("route is " + route);
		System.out.println("route is " + route.getConnection(0).getPath());
		System.out.println("route is " + route.getConnection(1).getPath());
		station = london;
	}

	@Before 
	public void beforeTest(){
		// every test requires a new train, carriage and for them to be set
		train = new Train(parentScene,player1,"train1", texture,station,10,10,10,0f);
		carriage = new Carriage(parentScene, texture, player1, station, train);
		train.setCarriage(carriage);
	}

	@Test
	public void testUpdateTurnNoRoute() {
		// test updateTurn if no route set
		// will not move anywhere
		Vector2 expectedPosition = carriage.getPosition();
		carriage.updateTurn();
		Vector2 actualPosition = carriage.getPosition();
		assertEquals(actualPosition, expectedPosition);
	}
	
	@Test
	public void testUpdateTurnCompleteRoute() {
		// test updateTurn if route completed
		// will not move anywhere
		train.restoreRoute(route, 1, 1);
		carriage.restoreRoute();
		train.updatePosition();
		carriage.updateTurn();
		Vector2 expectedPosition = carriage.getPosition();
		carriage.updateTurn();
		Vector2 actualPosition = carriage.getPosition();
		assertEquals(actualPosition, expectedPosition);
	}
	
	@Test
	public void testUpdateTurnAlongRoute() {
		// testUpdateTurn when along route
		// train will move (location will not be same)
		train.restoreRoute(route, 1, 0.5f);
		carriage.restoreRoute();
		train.updateTurn(); 
		Vector2 expectedPosition = carriage.getPosition();
		carriage.updateTurn();
		Vector2 actualPosition = carriage.getPosition();
		assertNotEquals(actualPosition, expectedPosition);
	}

	@Test
	public void testUpdatePositionNullRoute() {
		// test updatePosition with no route being set (shouldnt occur due to updateTurn blocking it)
		// will return carriage did not move position
		Vector2 prevPosition = carriage.getPosition();
		carriage.updatePosition();
		Vector2 nextPosition = carriage.getPosition();

		assertEquals(prevPosition, nextPosition);
	}

	@Test
	public void testUpdatePositionTrainCompletedRoute(){
		// test updatePosition with route being completed (shouldnt occur due to updateTurn blocking it)
		// will return carriage did not move position
		train.restoreRoute(route, 1, 1);
		carriage.restoreRoute();
		train.updatePosition();
		carriage.updatePosition();
		Vector2 prevPosition = carriage.getPosition();
		carriage.updatePosition();
		Vector2 nextPosition = carriage.getPosition();

		assertEquals(prevPosition, nextPosition);
	}


	@Test
	public void testUpdatePositionTrainPartlyAlongRoute(){
		// test updatePosition if partly along route
		// will move by trains speed value along path in pixels
		train.restoreRoute(route, 0, 0.5f);
		carriage.restoreRoute();
		train.updatePosition();
		carriage.updatePosition();
		Vector2 expectedPosition = new Vector2(300 + train.getSpeed()-train.getWidth()- carriage.getWidth()/2, 100-carriage.getHeight()/2);
		Vector2 actualPosition = carriage.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x, 1);
		assertEquals(expectedPosition.y, actualPosition.y, 1);
	}

	@Test
	public void testUpdatePositionTrainReachesWaypoint(){	
		// test updatePosition if train reaches waypoint
		// will return that carriage doesnt move
		train.restoreRoute(route, 0, 0.99f);
		carriage.restoreRoute();
		train.updatePosition();
		carriage.updatePosition();
		Vector2 expectedPosition = new Vector2(500 - train.getWidth()- (carriage.getWidth()/2), 100-carriage.getHeight()/2);
		Vector2 actualPosition = carriage.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x, 1);
		assertEquals(expectedPosition.y, actualPosition.y, 1);
	}

	@Test
	public void testUpdatePositionCarriageReachesWaypoint(){
		// test updatePosition if partly along route
		// will move by trains speed value along path in pixels
		train.restoreRoute(route, 1, 1f);
		carriage.restoreRoute();
		train.updatePosition();
		carriage.updatePosition();
		Vector2 expectedPosition = new Vector2(900-train.getWidth()- carriage.getWidth()/2, 100-carriage.getHeight()/2);
		Vector2 actualPosition = carriage.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x, 1);
		assertEquals(expectedPosition.y, actualPosition.y, 1);
	}
	
	@Test
	public void testUpdatePositionTrainReachesFinalWaypoint(){	
		// test updatePosition if train reaches waypoint
		// will return that carriage doesnt move
		train.restoreRoute(route, 1, 0.99f);
		carriage.restoreRoute();
		train.updatePosition();
		carriage.updatePosition();
		Vector2 expectedPosition = new Vector2(900 - train.getWidth()- (carriage.getWidth()/2), 100-carriage.getHeight()/2);
		Vector2 actualPosition = carriage.getPosition();
		assertEquals(expectedPosition.x, actualPosition.x, 1);
		assertEquals(expectedPosition.y, actualPosition.y, 1);
	}
	
	@Test
	public void testSetLabel(){
		// test setLabel
		// will return that label is set to position of carriage
		carriage.setLabel();
		assertEquals (carriage.getLabel().getX(), 0, 0);
		assertEquals (carriage.getLabel().getY(), 0, 0);
	}

	@Test
	public void testSetRouteNull() {
		// test setRoute if null route passed
		// will return that the carriages route is null
		train.setRoute(null);
		carriage.setRoute();
		assertNull(carriage.getRoute());
	}

	@Test
	public void testSetRouteValidRoute() {
		// test setRoute if valid route passed
		// will return that the route is(and carriages position is the start of ) London->Paris-Berlin
		System.out.println(train.getStation());
		train.setRoute(route);
		carriage.setRoute();
		assertEquals("LondonParisBerlin", carriage.getRoute().getName());
		assertEquals(0f, carriage.getCurrent(), 4);
		assertEquals(0f, carriage.getPathDistance(), 0);
		assertEquals(0f, carriage.getRouteDistance(), 0);
	}

	// nb train cannot be given an invalid route (see TrainTest.class) therefore setRoute cannot be used with an invalid route

	@Test
	public void testRestoreRouteTrainPartlyAlongPath() {
		// test restoreRoute with normal use (valid route, waypoint within route and current <= 1)
		// will return route set as route, current as the value correspondong to 50 pixels behind train (0.7638) 
		train.restoreRoute(route, 1, 0.9f);
		carriage.restoreRoute();
		Connection connection = new Connection(berlin, parisBerlin);
		CurvedPath path = parisBerlin;
		assertEquals(route, carriage.getRoute());
		assertEquals(connection, carriage.getConnection());
		assertEquals(path, carriage.getPath());
		assertEquals(0.7638f, carriage.getCurrent(), 0);
	}

	@Test
	public void testRestoreRouteTrainAtStartOfRoute() {
		// test restoreRoute if train set to the point where a carriage wouldnt fit (before the train has gone 50 pixels along route)
		// will return carriage at start of route
		train.restoreRoute(route, 0, 0.1f);
		carriage.restoreRoute();
		Connection connection = new Connection(paris, londonParis);
		CurvedPath path = londonParis;
		assertEquals(carriage.getRoute(), route);
		assertEquals(carriage.getConnection(), connection);
		assertEquals(carriage.getPath(), path);
		assertEquals(carriage.getCurrent(), 0f, 0);
	}

	@Test
	public void testRestoreRouteTrainAtStartOfNextPath() {
		// test restoreroute if train set to a point where it isnt 50 past the routelocation, but has completed a previous path (waypoint >= 1)
		// will return carriage on previous stage of route, with distance as 50pxs behind train
		train.restoreRoute(route, 1, 0f);
		carriage.restoreRoute();
		Connection connection = new Connection(paris, londonParis); 
		CurvedPath path = londonParis;
		assertEquals(carriage.getRoute(), route);
		assertEquals(carriage.getConnection(), connection);
		assertEquals(carriage.getPath(), path);
		System.out.println("current: " + carriage.getCurrent());
		assertEquals(carriage.getCurrent(), 0.8288, 2); 
		Vector2 actualPosition = carriage.getPosition();
		Vector2 intendedPosition = new Vector2(500- train.getWidth()-carriage.getWidth()/2, 100- carriage.getHeight()/2);
		assertEquals(actualPosition.x, intendedPosition.x, 1);
		assertEquals(actualPosition.y, intendedPosition.y, 1);
	}

	@Test
	public void testRestoreRouteNullRoute() {
		// test restoreRoute if train has a null route (shouldnt occur under normal use)
		// willl return the carriage being set to a null route
		train.setRoute(null);
		carriage.restoreRoute();
		assertNull(carriage.getRoute());
	}

	@Test
	public void testCarriageValidParameters() {
		// test constructor with valid parameters
		// will return position as (station position - centralising offset), train,player correctly and a label that isnt null
		Train train = new Train(parentScene,player1,"train1", texture,station,10,10,10,0f);
		Carriage carriage = new Carriage(parentScene, texture, player1, station, train);
		assertEquals(carriage.getTrain(), train);
		assertEquals(carriage.getPlayer(), player1);
		assertEquals(carriage.getPosition().add(carriage.getSizeOffset()), new Vector2(100,100));
		assertNotNull(carriage.getLabel());
	}

	
	@Test (expected = IllegalArgumentException.class)
	public void testCarriageNullTrain() {
		// test constructor with null player
		// will throw exception
		@SuppressWarnings("unused")
		Carriage carriage = new Carriage(parentScene, texture, player1, station, null);
	}	

}





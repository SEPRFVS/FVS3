package com.turkishdelight.taxe.scenes.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.routing.AiSprite;
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
public class GameSceneTest {

	private static Texture texture;
	private static GameScene parentScene;
	private static Station station;
	private static Player player1;
	private static Player player2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		texture = new Texture("elec1.png");
		player1 = new Player();
		player1.setName("player1");
		player2 = new Player();
		player1.setStartLocation("Moscow");
		player1.setFuel(700);
		player2.setFuel(700);
		player2.setStartLocation("London");
		parentScene = new GameScene(player1, player2);
		station = new Station(parentScene, "London", 100,100);
	}

	@Test
	public void testGameScene() {
		// test constructor, will set the activePlayer to player 1
		GameScene scene = new GameScene(player1, player2);
		assertEquals(player1, scene.activePlayer());
	}
	
	@Test
	public void testDelayedCreateStationsCreated() {
		// test delayed create that te correct stations and junctions are required for the map
		// will return true, all of the neccessary locations created
		GameScene scene = new GameScene(player1, player2);
		ArrayList<RouteLocation> locations = scene.getLocations();
		ArrayList<RouteLocation> expectedLocations = new ArrayList<RouteLocation>();

		expectedLocations.add(new Station(parentScene, "London", 210, 390));
		expectedLocations.add(new Station(parentScene,  "Rome", 415, 168));
		expectedLocations.add(new Station(parentScene,  "Moscow", 800, 450));
		expectedLocations.add(new Station(parentScene,  "Lisbon", 30, 120));
		expectedLocations.add(new Station(parentScene,  "Paris", 300, 340));
		expectedLocations.add(new Station(parentScene, "Berlin", 410, 400));
		expectedLocations.add(new Station(parentScene,  "Madrid", 120, 150));
		expectedLocations.add(new Station(parentScene,  "Budapest", 520, 280));
		expectedLocations.add(new Station(parentScene,  "Krakow", 520, 350));
		expectedLocations.add(new Station(parentScene,  "J1", 352, 268));
		expectedLocations.add(new Station(parentScene,  "J2", 455, 321));
		
		for (int i = 0; i < locations.size(); i++){
			assertEquals(expectedLocations.get(i).getName(), locations.get(i).getName());
		}
	}

	@Test
	public void testDelayedCreateConnectionsCreated() {
		// test delayed create for all of the neccessary connections for the locations are created, to allow routes to be created correctly
		// will return true, all of the neccessary connections created
		GameScene scene = new GameScene(player1, player2);
		ArrayList<RouteLocation> locations = scene.getLocations();
		
		Station london = (Station) locations.get(0);
		Station rome = (Station) locations.get(1);
		Station moscow = (Station) locations.get(2);
		Station lisbon = (Station) locations.get(3);
		Station paris = (Station) locations.get(4);
		Station berlin = (Station) locations.get(5);
		Station madrid = (Station) locations.get(6);
		Station budapest = (Station) locations.get(7);
		Station krakow = (Station) locations.get(8);
		Junction j1 = (Junction) locations.get(9);
		Junction j2 = (Junction) locations.get(10);
		
		System.out.println(london.isConnected(paris));
		
		assertTrue(london.isConnected(paris));
		assertTrue(london.isConnected(lisbon));
		assertTrue(rome.isConnected(j1));
		assertTrue(rome.isConnected(budapest));
		assertTrue(moscow.isConnected(berlin));
		assertTrue(moscow.isConnected(krakow));
		assertTrue(lisbon.isConnected(london));
		assertTrue(lisbon.isConnected(madrid));
		assertTrue(paris.isConnected(j1));
		assertTrue(paris.isConnected(berlin));
		assertTrue(paris.isConnected(london));
		assertTrue(berlin.isConnected(paris));
		assertTrue(berlin.isConnected(moscow));
		assertTrue(madrid.isConnected(lisbon));
		assertTrue(madrid.isConnected(j1));
		assertTrue(budapest.isConnected(rome));
		assertTrue(budapest.isConnected(j2));
		assertTrue(krakow.isConnected(j2));
		assertTrue(krakow.isConnected(moscow));
		assertTrue(j1.isConnected(paris));
		assertTrue(j1.isConnected(j2));
		assertTrue(j1.isConnected(madrid));
		assertTrue(j1.isConnected(rome));
		assertTrue(j1.isConnected(rome));
		assertTrue(j2.isConnected(krakow));
		assertTrue(j2.isConnected(berlin));
		assertTrue(j2.isConnected(budapest));
		assertTrue(j2.isConnected(j1));
	}
	
	@Test
	public void testRestoreRouteValidRoute() {
		// tests restoreRoute with a valid route (string of form LocationLocation.. with each location connected to following location)
		// will return route of form London->Paris->Berlin
		Route actualRoute = parentScene.restoreRoute("LondonParisBerlin");
		ArrayList<RouteLocation> newLocations = new ArrayList<RouteLocation>();
		ArrayList<RouteLocation> routeLocations = parentScene.getLocations();
		System.out.println(routeLocations.get(0).getConnections());
		newLocations.add((Station) routeLocations.get(0));
		newLocations.add((Station) routeLocations.get(4));
		newLocations.add((Station) routeLocations.get(5));
		System.out.println(((Station) routeLocations.get(0)).isConnected((Station) routeLocations.get(4)));
		Route expectedRoute = new Route(newLocations);
		assertEquals(expectedRoute.getName(), actualRoute.getName());
		
	}
	
	@Test
	public void testRestoreRouteInvalidRoute() {
		// test restore route with an invalid route; the locations are not connected - will return null route
		Route route1 = parentScene.restoreRoute("LondonBerlinParis");
		assertNull(route1);
	}
	
	@Test 
	public void testRestoreRouteInvalidStringRoute() {
		// test restore route with an invalid route; the string isnt the correct form - will return null route
		Route route1 = parentScene.restoreRoute("lOnDonparisBERLIN");
		assertNull(route1);
	}
	
	@Test (expected = NullPointerException.class)
	public void testReverseDatasetNull() {
		// testing trying to reverse a null dataset- will throw an exception
		Vector2[] dataSet1 =null;
		parentScene.reverseDataset(dataSet1);
	}
	
	@Test
	public void testReverseDatasetValidDataset() {
		// testing reversing a valid dataset 
		// will return the dataset in reverse order
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(100, 100));
		dataSet1[1] = (new Vector2(100, 100));
		dataSet1[2] = (new Vector2(500, 100));
		dataSet1[3] = (new Vector2(500, 100));
		Vector2[] actualDataSet = parentScene.reverseDataset(dataSet1);
		

		Vector2[] expectedDataSet = new Vector2[4];
		expectedDataSet[3] = (new Vector2(100, 100));
		expectedDataSet[2] = (new Vector2(100, 100));
		expectedDataSet[1] = (new Vector2(500, 100));
		expectedDataSet[0] = (new Vector2(500, 100));
		
		assertArrayEquals(expectedDataSet, actualDataSet);
	}
	
	@Test
	public void testGetStationByNameInvalidName() {
		// testing getStationByName with a string that isnt a name of any station
		// will return null station
		Station station = (Station) parentScene.getStationByName("Word");	
		assertNull(station);
	}

	@Test 
	public void testGetStationByNameValidName() {
		// testing getStationByName with a string that is a name of a station
		// will return True, returns london station
		Station actualStation = (Station) parentScene.getStationByName("London");	
		Station expectedStation = new Station(parentScene, "London", 210, 390);
		assertTrue(expectedStation.equals(actualStation));
	}
	
	@Test
	public void generateTrainAndCarriageTestValid() {
		// testing generateTrainAndCarriage with valid inputs
		// will return a new train with a connected carriage, and said train being in the players aiSprites.
		Player player = new Player();
		Train expectedTrain = parentScene.generateTrainAndCarriage(player, station, Train.Type.ELECTRIC);
		assertNotNull(expectedTrain);
		assertNotNull(expectedTrain.getCarriage());
		assertEquals(player.getAiSprites().get(0), expectedTrain);
		assertEquals(player.getAiSprites().get(1), expectedTrain.getCarriage());
		
	}
	
	@Test
	public void testGenerateTrainsAndCarriagesInvalidPlayer() {
		// testing generateTrainAndCarriage with null player
		// will return null train
		Train train = parentScene.generateTrainAndCarriage(null, station, Train.Type.ELECTRIC);
		assertNull(train);
	}
	
	@Test
	public void testCreateTrainsAndCarriagesInvalidStation() {
		// testing generateTrainAndCarriage with null station
		// will return null train
		Train train = parentScene.generateTrainAndCarriage(player1, null, Train.Type.ELECTRIC);
		assertNull(train);
	}
	
	@Test
	public void testConnectRouteLocationsValid() {
		// test connectRouteLocations, with valid locations and paths to and from said locations
		// should return that l1 is now connected to l2 via l1l2, and l2 connected to l1 via l2l1
		Station l1 = new Station(parentScene, "l1", 100, 100);
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(100, 100));
		dataSet1[1] = (new Vector2(100, 100));
		dataSet1[2] = (new Vector2(500, 100));
		dataSet1[3] = (new Vector2(500, 100));
		CurvedPath l1l2 = new CurvedPath(dataSet1, false);
		
		Station l2 = new Station(parentScene, "l2", 500, 100);
		Vector2[] dataSet2 = new Vector2[4];
		dataSet2[0] = (new Vector2(500, 100));
		dataSet2[1] = (new Vector2(500, 100));
		dataSet2[2] = (new Vector2(100, 100));
		dataSet2[3] = (new Vector2(100, 100));
		CurvedPath l2l1 = new CurvedPath(dataSet2, false);
		
		HashMap<String, CurvedPath> paths = new HashMap<String, CurvedPath>();
		paths.put("l1l2", l1l2);
		paths.put("l2l1", l2l1);
		
		parentScene.connectRouteLocations(l1, l2,paths );
		assertTrue(l1.isConnected(l2));
		assertTrue(l2.isConnected(l1));
		assertEquals(l1.getConnections().get(0).getPath(), l1l2);
		assertEquals(l2.getConnections().get(0).getPath(), l2l1);
	}
	
	@Test
	public void testConnectRouteLocationsInvalid() {
		// test connectroutelocations with paths that dont exist
		// will return that l1 will not be connected to l2, l2 will not be connected to l1
		Station l1 = new Station(parentScene, "l1", 100, 100);
		Station l2 = new Station(parentScene, "l2", 500, 100);
		HashMap<String, CurvedPath> paths = new HashMap<String, CurvedPath>();
		// path isnt present in hashmap
		parentScene.connectRouteLocations(l1, l2, paths);
		assertFalse(l1.isConnected(l2));
		assertFalse(l2.isConnected(l1));
	}

	@Test
	public void testStartSelectingRouteWhileSelectingRoute() {
		// test startSelectingRoute whilst already in route selection mode
		// will return no change- it will still be in route selection mode
		parentScene.startSelectingRoute();
		parentScene.startSelectingRoute();
		assertTrue(parentScene.isRouteSelecting());
		
		ArrayList<RouteLocation> locations = parentScene.getLocations();
		for (RouteLocation location:locations){
			assertTrue(location.getSelectingRoute());
		}
	}
	
	@Test
	public void testStartSelectingRouteWhileNotSelectingRoute() {
		// test startSelectingRoute whilst not already in route selection mode
		// all locations should now be selectable, should be in route selection mode
		parentScene.startSelectingRoute();
		assertTrue(parentScene.isRouteSelecting());
		
		ArrayList<RouteLocation> locations = parentScene.getLocations();
		for (RouteLocation location:locations){
			assertTrue(location.getSelectingRoute());
		}
	}

	@Test
	public void testSelectStartingLocation() {
		// test selectStartingLocation with a valid starting location
		// will return the newRoute as an array with just startLocation in
		RouteLocation startLocation = new Station(parentScene, "Start", 100,100);
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, 1, 1, 1);
		parentScene.setSelectingTrain(train);
		
		parentScene.selectStartingLocation(startLocation);
		ArrayList<RouteLocation> expectedArrayList = new ArrayList<RouteLocation>();
		expectedArrayList.add(startLocation);
		ArrayList<RouteLocation> actualArrayList = parentScene.getNewRoute();
		assertEquals(expectedArrayList, actualArrayList);
	}

	@Test
	public void testSelectLocationNoStartingLocation() {
		// test selctLocation with no startlocation set (through selectStartingLocation())
		// will not change the newRoute- will remain empty
		RouteLocation location = new Station(parentScene, "location", 100,100);
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, 1, 1, 1);
		parentScene.setSelectingTrain(train);
		
		parentScene.selectLocation(location);
		ArrayList<RouteLocation> actualRoute = parentScene.getNewRoute();
		ArrayList<RouteLocation> expectedRoute = new ArrayList<RouteLocation>();
		assertEquals(expectedRoute, actualRoute);
		
		parentScene.endSelectingRoute();
	}
	
	@Test
	public void testSelectLocationStartingLocationSetConnectedLocations() {
		// test selctLocation with startlocation set (through selectStartingLocation())
		// will add location to newRoute
		Station l1 = new Station(parentScene, "l1", 100, 100);
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(500, 100));
		dataSet1[1] = (new Vector2(500, 100));
		dataSet1[2] = (new Vector2(100, 100));
		dataSet1[3] = (new Vector2(100, 100));
		CurvedPath l2l1 = new CurvedPath(dataSet1, false);
		
		Station l2 = new Station(parentScene, "l2", 500, 100);
		l2.addConnection(l1, l2l1); 
		
		System.out.println("Is l1 connected to l2? " + l1.isConnected(l2));
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, 1, 1, 1);
		parentScene.setSelectingTrain(train);
		parentScene.selectStartingLocation(l1);
		parentScene.selectLocation(l2);
		
		ArrayList<RouteLocation> actualLocations = parentScene.getNewRoute();
		ArrayList<RouteLocation> expectedLocations = new ArrayList<RouteLocation>();
		expectedLocations.add(l1);
		expectedLocations.add(l2);
		assertEquals(expectedLocations, actualLocations);
		
		parentScene.endSelectingRoute();
	}
	
	@Test
	public void testSelectLocationStartingLocationSetDisconnectedLocations() {
		// test selctLocation with startlocation set (through selectStartingLocation()) but locations arent connected
		// newRoutw ill be just l1, set from selectStartingLocation
		Station l1 = new Station(parentScene, "l1", 100, 100);
		Station l2 = new Station(parentScene, "l2", 500, 100);
		
		Train train = new Train(parentScene, player1, "train1", texture, station, 1, 1, 1, 1);
		parentScene.setSelectingTrain(train);
		parentScene.selectStartingLocation(l1);
		parentScene.selectLocation(l2);
		
		ArrayList<RouteLocation> actualLocations = parentScene.getNewRoute();
		ArrayList<RouteLocation> expectedLocations = new ArrayList<RouteLocation>();
		expectedLocations.add(l1);
		assertEquals(expectedLocations, actualLocations);
		
		parentScene.endSelectingRoute();
	}
	
	@Test
	public void testGetCollisionsNoCollision() {
		// test getCollisions with no collisions occuring
		// will return an empty arraylist
		Player player = new Player();
		Player otherPlayer = new Player();
		GameScene scene = new GameScene(player, otherPlayer);
		Train train1 = new Train(scene, player, "train1", texture, station, 1, 1, 1, 1);
		Station station2 = new Station(scene, "station2", 10, 10);
		Train train2 = new Train(scene, otherPlayer, "train2", texture, station2 , 1, 1, 1, 1);
		
		player.addAiSprite(train1);
		otherPlayer.addAiSprite(train2);
		
		ArrayList<AiSprite> expectedCollisions = new ArrayList<AiSprite>();
		train1.setAiSpritePosition(100, 100);
		train2.setAiSpritePosition(500, 500);
		ArrayList<AiSprite> actualCollisions = scene.getCollisions();
		
		assertEquals(expectedCollisions, actualCollisions);
	}
	
	@Test
	public void testGetCollisionsOneCollision() {
		// test getCollisions with one collision occuring, between train1, train2
		// collisions returned will be [train1, train2]
		Player player = new Player();
		Player otherPlayer = new Player();
		GameScene scene = new GameScene(player, otherPlayer);
		Train train1 = new Train(scene, player, "train1", texture, station, 1, 1, 1, 1);
		Station station2 = new Station(scene, "station2", 10, 10);
		Train train2 = new Train(scene, otherPlayer, "train2", texture, station2 , 1, 1, 1, 1);
		
		player.addAiSprite(train1);
		otherPlayer.addAiSprite(train2);
		
		ArrayList<AiSprite> expectedCollisions = new ArrayList<AiSprite>();
		train1.setAiSpritePosition(100, 100);
		train2.setAiSpritePosition(100, 100);
		ArrayList<AiSprite> actualCollisions = scene.getCollisions();
		
		expectedCollisions.add(train1);
		expectedCollisions.add(train2);
		
		assertEquals(expectedCollisions, actualCollisions);
	}
	
	@Test
	public void testGetCollisionsMultipleCollision() {
		// testing get collisions with multiple collisions occuring in different places - train1-train2, train3-train4
		// will return [train1, train2, train3, train4]
		Player player = new Player();
		Player otherPlayer = new Player();
		GameScene scene = new GameScene(player, otherPlayer);
		Train train1 = new Train(scene, player, "train1", texture, station, 10, 1, 1, 1);
		Station station2 = new Station(scene, "station2", 10, 10);
		Train train2 = new Train(scene, otherPlayer, "train2", texture, station2 , 1, 1, 1, 1);
		Station station3 = new Station(scene, "station3", 1000, 10);
		Train train3 = new Train(scene, player, "train3", texture, station3, 10, 1, 1, 1);
		Station station4 = new Station(scene, "station4", 500, 10);
		Train train4 = new Train(scene, otherPlayer, "train4", texture, station4, 1, 1, 1, 1);
		
		player.addAiSprite(train1);
		player.addAiSprite(train3);
		otherPlayer.addAiSprite(train2);
		otherPlayer.addAiSprite(train4);
		
		ArrayList<AiSprite> expectedCollisions = new ArrayList<AiSprite>();
		train1.setAiSpritePosition(100, 100);
		train2.setAiSpritePosition(100, 100);
		train3.setAiSpritePosition(500, 500);
		train4.setAiSpritePosition(500, 500);
		ArrayList<AiSprite> actualCollisions = scene.getCollisions();
		
		expectedCollisions.add(train1);
		expectedCollisions.add(train2);
		expectedCollisions.add(train3);
		expectedCollisions.add(train4);
		
		assertEquals(expectedCollisions, actualCollisions);
	}
	
	@Test
	public void testCalculateCollisionsNoCollision() {
		// test calculateCollisions with no collision occuring
		// will return no change in either trains carriages (both will be 3)
		Player player = new Player();
		Player otherPlayer = new Player();
		GameScene scene = new GameScene(player, otherPlayer);
		Train train1 = new Train(scene, player, "train1", texture, station, 1, 1, 1, 1);
		Station station2 = new Station(scene, "station2", 10, 10);
		Train train2 = new Train(scene, otherPlayer, "train2", texture, station2 , 10, 1, 1, 1);
		Carriage carriage1 = new Carriage(scene, texture, player1, station, train1);
		Carriage carriage2 = new Carriage(scene, texture, player2, station2, train2);
		train1.setCarriage(carriage1);
		train2.setCarriage(carriage2);
		
		train1.setPosition(100, 100);
		train2.setPosition(500, 500);
		ArrayList<AiSprite> collisions = new ArrayList<AiSprite>();
		collisions.add(train1);
		collisions.add(train2);
		scene.calculateCollisions(collisions);
		assertEquals(3,carriage1.getCarriageCount());
		assertEquals(3,carriage2.getCarriageCount());
	}
	
	@Test
	public void testCalculateCollisionsOneCollision() {
		// test calculateCollisions with only one collision occuring, between train1 and train
		// will lower train1's carriage count by 1, as train2 is the heavier train
		Player player = new Player();
		Player otherPlayer = new Player();
		GameScene scene = new GameScene(player, otherPlayer);
		Train train1 = new Train(scene, player, "train1", texture, station, 1, 1, 1, 1);
		Station station2 = new Station(scene, "station2", 10, 10);
		Train train2 = new Train(scene, otherPlayer, "train2", texture, station2 , 10, 1, 1, 1);
		Carriage carriage1 = new Carriage(scene, texture, player1, station, train1);
		Carriage carriage2 = new Carriage(scene, texture, player2, station2, train2);
		train1.setCarriage(carriage1);
		train2.setCarriage(carriage2);
		
		train1.setAiSpritePosition(100, 100);
		train2.setAiSpritePosition(100, 100);
		ArrayList<AiSprite> collisions = new ArrayList<AiSprite>();
		collisions.add(train1);
		collisions.add(train2);
		scene.calculateCollisions(collisions);
		assertEquals(2, carriage1.getCarriageCount());
		assertEquals(3, carriage2.getCarriageCount());
	}
	
	@Test
	public void testCalculateCollisionsMultipleDifferentCollision() {
		// test calculateCollisions with multiple collisions- train1- train2, train 3-train4 in different collisions
		// will return train1 with a carriagecount of 3, train2 = 2 (as train1 heavier), train3 = 3, train4 =2 (as train3 heavier)
		Player player = new Player();
		Player otherPlayer = new Player();
		GameScene scene = new GameScene(player, otherPlayer);
		Train train1 = new Train(scene, player, "train1", texture, station, 10, 1, 1, 1);
		Station station2 = new Station(scene, "station2", 10, 10);
		Train train2 = new Train(scene, otherPlayer, "train2", texture, station2 , 1, 1, 1, 1);
		Station station3 = new Station(scene, "station3", 1000, 10);
		Train train3 = new Train(scene, player, "train3", texture, station3, 10, 1, 1, 1);
		Station station4 = new Station(scene, "station4", 500, 10);
		Train train4 = new Train(scene, otherPlayer, "train4", texture, station4, 1, 1, 1, 1);
		
		Carriage carriage1 = new Carriage(scene, texture, player, station, train1);
		Carriage carriage2 = new Carriage(scene, texture, otherPlayer, station2, train2);
		Carriage carriage3 = new Carriage(scene, texture, player, station3, train3);
		Carriage carriage4 = new Carriage(scene, texture, otherPlayer, station4, train4);
		
		train1.setCarriage(carriage1);
		train2.setCarriage(carriage2);
		train3.setCarriage(carriage3);
		train4.setCarriage(carriage4);

		train1.setAiSpritePosition(100, 100);
		train2.setAiSpritePosition(100, 100);
		train3.setAiSpritePosition(500, 500);
		train4.setAiSpritePosition(500, 500);
		
		ArrayList<AiSprite> collisions = new ArrayList<AiSprite>();
		collisions.add(train1);
		collisions.add(train2);
		collisions.add(train3);
		collisions.add(train4);
		scene.calculateCollisions(collisions);
		assertEquals(3, carriage1.getCarriageCount());
		assertEquals(2, carriage2.getCarriageCount());
		assertEquals(3, carriage3.getCarriageCount());
		assertEquals(2, carriage4.getCarriageCount());
	}
	
	@Test
	public void testCalculateCollisionsMultipleSameCollision() {
		// test calculatecollision, where it is called twice with the samecollision
		// will only register this as one collision, train1 carriage count = 2, train2 = 3, train3 = 2, train4 = 2
		GameScene scene = new GameScene(player1, player2);
		Train train1 = new Train(scene, player1, "train1", texture, station, 1, 1, 1, 1);
		Carriage carriage1 = new Carriage(scene, texture, player1, station, train1);
		Train train2 = new Train(scene, player2, "train2", texture, station, 10, 1, 1, 1);
		Carriage carriage2 = new Carriage(scene, texture, player2, station, train2);
		train1.setCarriage(carriage1);
		train2.setCarriage(carriage2);
		
		Train train3 = new Train(scene, player1, "train1", texture, station, 1, 1, 1, 1);
		Carriage carriage3 = new Carriage(scene, texture, player1, station, train3);
		Train train4 = new Train(scene, player2, "train2", texture, station, 10, 1, 1, 1);
		Carriage carriage4 = new Carriage(scene, texture, player2, station, train4);
		train3.setCarriage(carriage3);
		train4.setCarriage(carriage4);

		train1.setAiSpritePosition(100, 100);
		train2.setAiSpritePosition(100, 100);
		
		ArrayList<AiSprite> collisions = new ArrayList<AiSprite>();
		collisions.add(train1);
		collisions.add(train2);
		collisions.add(train3);
		collisions.add(train4);
		scene.calculateCollisions(collisions);
		scene.calculateCollisions(collisions);
		assertEquals(2, carriage1.getCarriageCount());
		assertEquals(3, carriage2.getCarriageCount());
		assertEquals(2, carriage3.getCarriageCount());
		assertEquals(3, carriage4.getCarriageCount());
	}
}

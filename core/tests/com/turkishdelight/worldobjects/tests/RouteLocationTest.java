package com.turkishdelight.worldobjects.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.Player;
import com.turkishdelight.taxe.routing.CurvedPath;
import com.turkishdelight.taxe.scenes.GameScene;
import com.turkishdelight.taxe.testrunners.GdxTestRunner;
import com.turkishdelight.taxe.worldobjects.RouteLocation;
import com.turkishdelight.taxe.worldobjects.Station;

@RunWith(GdxTestRunner.class)
public class RouteLocationTest {
	
	private static Texture texture;
	private static GameScene parentScene;
	private static Player player1;
	private static Player player2;
	private static CurvedPath path;
	private static Station london;
	private static Station paris;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		texture = new Texture("location.png");
		player1 = new Player();
		player1.setName("player1");
		player2 = new Player();
		player1.setStartLocation("London");
		player1.setFuel(700);
		player2.setFuel(700);
		player2.setStartLocation("London");
		parentScene = new GameScene(player1, player2);
		
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(100, 100));
		dataSet1[1] = (new Vector2(100, 100));
		dataSet1[2] = (new Vector2(500, 100));
		dataSet1[3] = (new Vector2(500, 100));
		path = new CurvedPath(dataSet1, false);
		
		london = new Station(parentScene, "London", 100, 100);
		paris = new Station(parentScene, "Paris", 100, 500);
		
	}

	@Test
	public void testRouteLocation() {
		// test a valid constructor
		// will return that name, position is set correctly
		RouteLocation routeLocation = new RouteLocation(parentScene, texture, "l1", 0, 0) {	
		};
		assertEquals(routeLocation.getName(), "l1");
		assertEquals(new Vector2(0,0), routeLocation.getPosition());
	}

	@Test
	public void testAddConnection() {
		// test adding a connection with addConnection
		// will return the correct path, (path) target location(paris) for the connection
		RouteLocation routeLocation = new RouteLocation(parentScene, texture, "l1", 100, 100) {	
		};
		routeLocation.addConnection(paris, path);
		assertEquals(paris, routeLocation.getConnections().get(0).getTargetLocation());
		assertEquals(path, routeLocation.getConnections().get(0).getPath());
	}
	
	@Test
	public void testisConnected() {
		// test isConnected with a connection added, and with no connection present
		// will return no connection with london, a connection with paris
		RouteLocation routeLocation = new RouteLocation(parentScene, texture, "l1", 100, 100) {	
		};
		routeLocation.addConnection(paris, path);
		assertTrue(routeLocation.isConnected(paris));
		assertFalse(routeLocation.isConnected(london));
	}

}

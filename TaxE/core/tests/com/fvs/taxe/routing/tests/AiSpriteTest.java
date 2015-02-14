package com.fvs.taxe.routing.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.fvs.taxe.Player;
import com.fvs.taxe.routing.AiSprite;
import com.fvs.taxe.routing.CurvedPath;
import com.fvs.taxe.scenes.GameScene;
import com.fvs.taxe.testrunners.GdxTestRunner;
import com.fvs.taxe.worldobjects.Station;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class AiSpriteTest {

	private static Texture texture;
	private static GameScene parentScene;
	private static Station station;
	private static Player player1;
	private static Player player2;
	private static AiSprite aiSprite;
	private static CurvedPath path;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		texture = new Texture("electric.png");
		player1 = new Player();
		player1.setName("player1");
		player2 = new Player();
		player1.setStartLocation("London");
		player1.setFuel(700);
		player2.setFuel(700);
		player2.setStartLocation("London");
		parentScene = new GameScene(player1, player2);
		station = new Station(parentScene, "London", 100,100);
		aiSprite = new AiSprite(parentScene, texture, player1, station) {

			@Override
			public void updateTurn() {
				return;
			}

			@Override
			public void updatePosition() {
				return;
			}

			@Override
			public int getWeight() {
				return 0;
			}
		};
		
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(100, 100));
		dataSet1[1] = (new Vector2(100, 100));
		dataSet1[2] = (new Vector2(900, 100));
		dataSet1[3] = (new Vector2(900, 100));
		path = new CurvedPath(dataSet1, false);

	}
	

	@Test
	public void testAiSprite() {
		// test constructor
		// will return position as the location, with a polygon set to same location
		aiSprite = new AiSprite(parentScene, texture, player1, station) {

			@Override
			public void updateTurn() {
				return;
			}

			@Override
			public void updatePosition() {
				return;
			}

			@Override
			public int getWeight() {
				return 0;
			}
		};
		
		
		Vector2 position = aiSprite.getPosition();
		Vector2 stationPosition = station.getPosition();
		assertEquals(position.x, stationPosition.x - AiSprite.SPRITEWIDTH/2, 4);
		assertEquals(position.y, stationPosition.y - AiSprite.SPRITEHEIGHT/2, 4);
		
		Polygon polygon = aiSprite.getPolygon();
		assertEquals(position.x, polygon.getX(), 4);
		assertEquals(position.y, polygon.getY(), 4);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testAiSpriteNullPlayer() {
		// test constructor with null player
		// will throw exception
		aiSprite = new AiSprite(parentScene, texture, null, station) {

			@Override
			public void updateTurn() {
				return;
			}

			@Override
			public void updatePosition() {
				return;
			}

			@Override
			public int getWeight() {
				return 0;
			}
		};
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAiSpriteNullStation() {
		// test constructor with null station
		// will throw exception
		aiSprite = new AiSprite(parentScene, texture, player1, null) {

			@Override
			public void updateTurn() {
				return;
			}

			@Override
			public void updatePosition() {
				return;
			}

			@Override
			public int getWeight() {
				return 0;
			}
		};
	}
	
	@Test
	public void testMoveStart() {
		// test move() with the current set to start of path
		// will return new position as the start location on path (100, 100) minus the offset for centralising the sprite
		aiSprite.setPath(path);
		aiSprite.setCurrent(0);
		aiSprite.move();
		Vector2 actualPosition = aiSprite.getPosition();
		Vector2 expectedPosition = new Vector2(75,90);
		assertEquals(expectedPosition, actualPosition);
	}
	
	@Test
	public void testMoveMiddle() {
		// test move() with the current set to start of path
		// will return new position as the middle location on path (500, 100) minus the offset for centralising the sprite
		aiSprite.setPath(path);
		aiSprite.setCurrent(0.5f);
		aiSprite.move();
		Vector2 actualPosition = aiSprite.getPosition();
		Vector2 expectedPosition = new Vector2(475, 90);
		assertEquals(expectedPosition, actualPosition);
	}
	
	@Test
	public void testMoveEnd() {
		// test move() with the current set to start of path
		// will return new position as the end location on path (1000, 100) minus the offset for centralising the sprite
		aiSprite.setPath(path);
		aiSprite.setCurrent(1f);
		aiSprite.move();
		Vector2 actualPosition = aiSprite.getPosition();
		Vector2 expectedPosition = new Vector2(875,90);
		assertEquals(expectedPosition, actualPosition);
	}

}

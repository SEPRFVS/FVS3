package com.turkishdelight.taxe.routing.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.turkishdelight.taxe.routing.CurvedPath;

public class CurvedPathTest {

	private static CurvedPath path;
	private static Vector2 startLocation;
	private static Vector2 endLocation;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Vector2[] dataSet1 = new Vector2[4];
		dataSet1[0] = (new Vector2(0, 100));
		dataSet1[1] = (new Vector2(0, 100));
		dataSet1[2] = (new Vector2(1000, 100));
		dataSet1[3] = (new Vector2(1000, 100));
		path = new CurvedPath(dataSet1, false);
		startLocation = new Vector2(0,100);
		endLocation = new Vector2(1000,100);
	}

	@Test (expected = NullPointerException.class)
	public void testCurvedPathNull() {
		@SuppressWarnings("unused")
		CurvedPath path = new CurvedPath(null, false);
	}

	@Test
	public void testOnDelayedCreateDistanceVals() {
		float firstDistance = 0;
		ArrayList<Float> actualDistances = path.getDistances();
		assertEquals(firstDistance, actualDistances.get(0), 4);
		
		float middleDistance = 500;
		assertEquals(middleDistance, actualDistances.get(499), 1);
		
		float finalDistance = 1000;
		assertEquals(finalDistance, actualDistances.get(1000), 1);
	}
	
	@Test
	public void testOnDelayedCreateTVals() {
		float firstCurrent = 0;
		assertEquals(firstCurrent, path.getT(0), 4);
		
		float middleCurrent = 0.5f;
		assertEquals(middleCurrent,path.getT(499), 1);
		
		float finalCurrent = 1;
		assertEquals(finalCurrent, path.getT(1000), 1);
	}
	
	@Test
	public void testOnDelayedCreatePoints() {
		Vector2 firstPoint = new Vector2(0,100);
		Vector2 actualFirstPoint = path.getPoint(0);
		assertEquals(firstPoint.x, actualFirstPoint.x, 4);
		assertEquals(firstPoint.y, actualFirstPoint.y, 4);
		
		Vector2 middlePoint = new Vector2(500,100);
		Vector2 actualMiddlePoint = path.getPoint(499);
		assertEquals(middlePoint.x, actualMiddlePoint.x, 4);
		assertEquals(middlePoint.y, actualMiddlePoint.y, 4);
		
		Vector2 lastPoint = new Vector2(1000,100);
		Vector2 actualLastPoint = path.getPoint(999);
		assertEquals(lastPoint.x, actualLastPoint.x, 4);
		assertEquals(lastPoint.y, actualLastPoint.y, 4);
	}

	@Test
	public void testGetDistanceFromTStart() {
		float expectedDistance = 0f;
		float actualDistance = path.getDistanceFromT(0);
		assertEquals(expectedDistance, actualDistance, 1);
	}
	
	@Test
	public void testGetDistanceFromTMiddle() {
		float expectedDistance = 500f;
		float actualDistance = path.getDistanceFromT(0.5f);
		assertEquals(expectedDistance, actualDistance, 1);
	}
	
	@Test
	public void testGetDistanceFromTEnd() {
		float expectedDistance = 1000f;
		float actualDistance = path.getDistanceFromT(1f);
		assertEquals(expectedDistance, actualDistance, 1);
	}


	@Test
	public void testGetTFromDistanceStart() {
		float expectedt = 0f;
		float actualt = path.getTFromDistance(0f);
		assertEquals(expectedt, actualt, 4);
	}
	
	@Test
	public void testGetTFromDistanceMiddle() {
		float expectedt = 0.5f;
		Float actualt = path.getTFromDistance(startLocation.dst(endLocation)/2);
		assertEquals(expectedt, actualt, 4);
	}
	
	@Test
	public void testGetTFromDistanceEnd() {
		float expectedt = 1f;
		float actualt = path.getTFromDistance(startLocation.dst(endLocation));
		assertEquals(expectedt, actualt, 4);
	}

	@Test
	public void testGetPointFromTStart() {
		Vector2 expectedPoint = new Vector2(0,100);
		Vector2 actualPoint = path.getPointFromT(0);
		assertEquals(expectedPoint.x, actualPoint.x, 4);
		assertEquals(expectedPoint.y, actualPoint.y, 4);
	}
	
	@Test
	public void testGetPointFromTMiddle() {
		Vector2 expectedPoint = new Vector2(500,100);
		Vector2 actualPoint = path.getPointFromT(0.5f);
		assertEquals(expectedPoint.x, actualPoint.x, 4);
		assertEquals(expectedPoint.y, actualPoint.y, 4);
	}
	
	@Test
	public void testGetPointFromTEnd() {
		Vector2 expectedPoint = new Vector2(1000,100);
		Vector2 actualPoint = path.getPointFromT(1);
		assertEquals(expectedPoint.x, actualPoint.x, 4);
		assertEquals(expectedPoint.y, actualPoint.y, 4);
	}
	
	@Test
	public void testGetFinalDistance() {
		float expectedDistance = startLocation.dst(endLocation);
		float actualDistance = path.getFinalDistance();
		assertEquals(expectedDistance, actualDistance, 4);
	}


	@Test
	public void testClosestIndexEmptyList() {
		int expectedValue = -1;
		int actualValue = path.closestIndex(2, new ArrayList<Float>());
		assertEquals(expectedValue, actualValue);
	}
	
	@Test
	public void testClosestIndexFirstElement() {
		
		ArrayList<Float> testArray = new ArrayList<Float>();
		testArray.add(0.1f);
		testArray.add(0.2f);
		testArray.add(0.3f);
		testArray.add(0.4f);
		testArray.add(0.5f);
		
		int expectedIndex = 0;
		int actualIndex = path.closestIndex(0.1f, testArray );
		assertEquals(expectedIndex, actualIndex);
	}
	
	
	@Test
	public void testClosestIndexLastElement() {
		ArrayList<Float> testArray = new ArrayList<Float>();
		testArray.add(0.1f);
		testArray.add(0.2f);
		testArray.add(0.3f);
		testArray.add(0.4f);
		testArray.add(0.5f);
		int expectedIndex = 4;
		int actualIndex = path.closestIndex(2, testArray);
		assertEquals(expectedIndex, actualIndex);
	}
	
	@Test
	public void testClosestIndexApproximateValue() {
		ArrayList<Float> testArray = new ArrayList<Float>();
		testArray.add(0.15f);
		testArray.add(0.22f);
		testArray.add(0.31f);
		testArray.add(0.48f);
		testArray.add(0.56f);
		int expectedIndex = 3;
		int actualIndex = path.closestIndex(0.4f, testArray);
		assertEquals(expectedIndex, actualIndex);
	}
	
	@Test
	public void testClosestIndexLargerValue() {
		ArrayList<Float> testArray = new ArrayList<Float>();
		testArray.add(0.15f);
		testArray.add(0.22f);
		testArray.add(0.31f);
		testArray.add(0.48f);
		testArray.add(0.56f);
		int expectedIndex = 4;
		int actualIndex = path.closestIndex(28f, testArray);
		assertEquals(expectedIndex, actualIndex);
	}
	
	@Test
	public void testClosestIndexSmallerValue() {
		ArrayList<Float> testArray = new ArrayList<Float>();
		testArray.add(0.15f);
		testArray.add(0.22f);
		testArray.add(0.31f);
		testArray.add(0.48f);
		testArray.add(0.56f);
		int expectedIndex = 0;
		int actualIndex = path.closestIndex(0f, testArray);
		assertEquals(expectedIndex, actualIndex);
	}

}

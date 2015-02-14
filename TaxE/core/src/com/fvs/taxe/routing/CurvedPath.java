package com.fvs.taxe.routing;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CurvedPath extends CatmullRomSpline<Vector2> {
	// a curved route is a curve from one location to another. represented by t values (depends on curve, speed etc), 
	// distances along curve in pixels and the pixel coordinate location of the point on curve
	
	private ArrayList<Vector2> points = new ArrayList<Vector2>();	// the points/positions on the curve (corresponds to index of tvals)
	private ArrayList<Float> distances = new ArrayList<Float>();		// the distances on the curve (corresponds to index of tvals)
	private ArrayList<Float> tvals = new ArrayList<Float>();			// the corresponding tvals to the points/routeDistance index values
	private int k = 1000;										// fidelity of curve (increasing increases load time, accuracy)

	public CurvedPath(Vector2[] dataSet1, boolean b) {
		super(dataSet1, b);
		onDelayedCreate();
	}
	
	public void onDelayedCreate() {
		// set up the arrays			
		Vector2 out = new Vector2();	
		points.add(valueAt(out, 0));	// array
		tvals.add(0f);					// initial
		distances.add(0f);				// values
		float distance = 0;
		for (int i = 1; i <= k; i++) {
			Vector2 point = new Vector2();
			float t = ((float) i)/((float)k-1);
			float roundedt = (float) (Math.round(t*10000f))/10000f;
			valueAt(point, t);
			points.add(point);
			tvals.add(roundedt);
			distance += point.dst(points.get(i-1));
			distances.add(distance);
		}
	}
	public ArrayList<Float> getDistances(){
		return distances;
	}
	public float getDistanceFromT(float t){
		return distances.get(closestIndex(t,tvals));
	}
	
	public float getFinalDistance() {
		return distances.get(distances.size()-1);
	}
	
	public Float getTFromDistance(Float distance){
		// from a given routeDistance, get the corresponding t value
		return tvals.get(closestIndex(distance,distances));
	}
	
	public float getTotalDistance(){
		float total = (float) 0;
        for (Float distance : distances) {
            total += distance;
        }
		return total;
	}

	public Float getT(int i){
		return tvals.get(i);
	}
	
	public Vector2 getPoint(int i) {
		return points.get(i);
	}

	public Vector2 getPointFromT(float tval) {
		// return point associated with given t value
		return points.get(closestIndex(tval, tvals));
	}

	public int closestIndex(float valueToFind, ArrayList<Float> values) {
		// TODO more efficient algorithm that takes advantage of sorted-ness
		// returns INDEX OF closest value in array
		float min = Integer.MAX_VALUE;
		//float closest = number;  // used for keeping track of closest value to return
		int i = -1;
		for (float value : values) {
			final float difference = Math.abs(value - valueToFind);
			if (difference < min) {
				min = difference;
				//closest = v;
				i++;
			} else {
				break;
			}
		}
		return i;
		
	}

	
	
}
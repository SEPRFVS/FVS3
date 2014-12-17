package com.turkishdelight.taxe.routing;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CurvedRoute extends CatmullRomSpline<Vector2> {

	private List<Vector2> points = new ArrayList<Vector2>();	// the points/positions on the curve (corresponds to index of tvals)
	private List<Float> distances = new ArrayList<Float>();	// the distances on the curve (corresponds to index of tvals)
	private List<Float> tvals = new ArrayList<Float>();		// the corresponding tvals to the points/distance index values
	private int k = 1000;									// fidelity of spline TODO link with gameScene k?

	public CurvedRoute(Vector2[] dataSet1, boolean b) {
		super(dataSet1, b);
		onDelayedCreate();
	}
	
	public void onDelayedCreate() {
		// set up the arrays
		float distance = 0;				
		Vector2 out = new Vector2();	
		points.add(valueAt(out, 0));	// array
		tvals.add(0f);					// initial
		distances.add(0f);				// values
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
		return (ArrayList<Float>) distances;
	}
	public float getDistanceFromT(float t){
		return distances.get(closestIndex(t,tvals));
	}
	
	public float getFinalDistance() {
		return distances.get(distances.size()-1);
	}
	
	public Float getTFromDistance(Float distance){
		// from a given distance, get the corresponding t value
		return tvals.get(closestIndex(distance,distances));
	}

	public Vector2 getPointFromT(float tval) {
		// return point associated with given t value
		return points.get(closestIndex(tval, tvals));
	}

	public int closestIndex(float number, List<Float> in) {
		// TODO more efficient algorithm that takes advantage of sorted-ness
		// returns INDEX OF closest value in array
		float min = Integer.MAX_VALUE;
		//float closest = number;  // used for keeping track of closest value to return
		int i = 0;
		for (float v : in) {
			final float difference = Math.abs(v - number);
			if (difference < min) {
				min = difference;
				//closest = v;
				i++;
			} else {
				break;
			}
		}
		return i;
		
		
		// more efficient algorithm TODO actually get the closest (max?)
		/*if (in.size() == 1){
			return 0;
		}
		float min = Integer.MAX_VALUE;
		int i = in.size()/2;
		Float currentval = in.get(i);
		
		if (currentval == number ) {
			return i;
		} else if (currentval > number ){
			return closestIndex(number, in.subList(0, i));
		} else {
			return (closestIndex(number,in.subList(i, in.size())) + i);
		}	*/
		
		
	}

	public Vector2 getPoint(int i) {
		return points.get(i);
	}

	
}
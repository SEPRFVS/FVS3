package com.turkishdelight.taxe.routing;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CurvedRoute extends CatmullRomSpline<Vector2> {
	
	private Array<Vector2> points = new Array<Vector2>();	// the points/positions on the curve (corresponds to index of tvals)
	private Array<Float> distances = new Array<Float>();	// the distances on the curve (corresponds to index of tvals)
	private Array<Float> tvals = new Array<Float>();		// the corresponding tvals to the points/distance index values
	private int k = 10000;
	
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
		for (int i = 1; i < k; i++) {
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
	
	public Float getT(Float distance){
		// from a given distance, get the corresponding t value
		return tvals.get(closest(distance,distances));
	}
	
	public Vector2 getPoint(float tval) {
		// return point associated with given t value
		return points.get(closest(tval, tvals));
	}
	
	public int closest(float number, Array<Float> in) {
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
	        }
	    }
	    return i;
	}
}
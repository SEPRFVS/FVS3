package com.turkishdelight.taxe.goals;

import com.turkishdelight.taxe.routing.Train;

public class Event {
//This class simply stores data, a train, an event type, a train type, a number of carriages and a station
public Train train;
public String EventType;
public String TrainType;
public int TrainCarriages;
public String Station;

public Event(Train t, String eT, int tC, String s)
{
	train = t;
	EventType = eT;
	TrainCarriages = tC;
	Station = s;
}
}

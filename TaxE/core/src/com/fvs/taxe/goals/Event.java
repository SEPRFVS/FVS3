package com.fvs.taxe.goals;

import com.fvs.taxe.routing.Train;

public class Event {
//This class simply stores data, a train, an event type, a train type and a station
public Train train;
public String EventType;
public String TrainType;
public String Station;

public Event(Train t, String eT, String s)
{
	train = t;
	EventType = eT;
	Station = s;
}
}

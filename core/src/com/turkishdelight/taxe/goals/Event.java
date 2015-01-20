package com.turkishdelight.taxe.goals;

import com.turkishdelight.taxe.routing.Train;

public class Event {
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

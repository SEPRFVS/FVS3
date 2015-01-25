package com.fvs.taxe.goals;

import java.util.ArrayList;


public class EventHandler {
//The event handler class acts as a list that can be pushed to, with an index viewable. This acts as a log which
//Cannot have Events removes
private int index = 0;
private ArrayList<Event> Events = new ArrayList<Event>();

public void pushEvent(Event e)
{
	Events.add(index, e);
	index++;
	System.out.println(e.Station);
}

public Event getEvent(int index)
{
	return Events.get(index);
}

public int getIndex()
{
	return index;
}
}

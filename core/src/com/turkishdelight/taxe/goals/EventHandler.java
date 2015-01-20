package com.turkishdelight.taxe.goals;

import java.util.ArrayList;


public class EventHandler {
	
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

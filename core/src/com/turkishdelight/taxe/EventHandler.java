package com.turkishdelight.taxe;

import java.util.ArrayList;

public class EventHandler {
	
int index = 0;
ArrayList<Event> Events = new ArrayList<Event>();

public void pushEvent(Event e)
{
	Events.add(index, e);
	index++;
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

package edu.gordon.eventsmanager;

import java.util.EventObject;

import com.google.common.eventbus.EventBus;

public class EventsManager {
	
	private static EventsManager INSTANCE = new EventsManager();
	
	private EventsManager()
	{ }
	
	public static EventsManager getInstance()
	{	
		return INSTANCE;
	}
	
	public static void post(EventObject evt) {
		bus.post(evt);
	}
	
	public static void register(Object obj) {
		bus.register(obj);
	}
	
	public static void unregister(Object obj) {
		bus.unregister(obj);
	}
	
	private static EventBus bus = new EventBus();
}

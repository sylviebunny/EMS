package com.enfec.sb.eventapi.repository;

import com.enfec.sb.eventapi.model.EventTable;

public interface EventRepository {
	
	public Object getEventInfoByID(int organizer_id);
	public Object getEventInfoByName(String event_name);
	
	public int createEvent(EventTable organizerTable);
	public int updateEvent(EventTable organizerTable);
	
	public int deleteEvent(int event_id); 
	
}

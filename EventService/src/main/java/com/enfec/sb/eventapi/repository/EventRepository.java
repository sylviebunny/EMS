package com.enfec.sb.eventapi.repository;

import java.util.List;

import com.enfec.sb.eventapi.model.EventTable;

public interface EventRepository {
	
	public Object getEventInfo(Integer event_id, String event_name, String type_code, String commercial_type,
			Integer organizer_id, Integer venue_id); 
	
	public int createEvent(EventTable eventTable);
	public int updateEvent(EventTable eventTable);
	
	public int deleteEvent(int event_id);


	
}

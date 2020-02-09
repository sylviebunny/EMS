package com.enfec.sb.eventapi.repository;

import java.util.List;

import com.enfec.sb.eventapi.model.EventTable;

public interface EventRepository {
	
	public Object getEventInfo(Integer event_id, String event_name, String type_code, Boolean free_or_commercial,
			Integer organizer_id, Integer venue_id); 
	
	public int createEvent(EventTable organizerTable);
	public int updateEvent(EventTable organizerTable);
	
	public int deleteEvent(int event_id);


	
}

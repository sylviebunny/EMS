package com.enfec.sb.eventapi.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.enfec.sb.eventapi.model.EventTable;

public interface EventRepository {
	
	public int createEvent(EventTable eventTable);
	public int updateEvent(EventTable eventTable);
	
	public int deleteEvent(int event_id);

	public List<Map> getFilteredEvents(List<EventTable> allEvent, String str);
	public List<Map> getFilteredEvents(List<EventTable> allEvent, Timestamp start_date, Timestamp end_date);
	
	public List<EventTable> getAllEvents();


	
}

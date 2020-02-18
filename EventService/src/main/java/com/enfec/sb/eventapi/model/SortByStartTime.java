package com.enfec.sb.eventapi.model;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;

public class SortByStartTime implements Comparator<Map>{
	
	@Override
	public int compare(Map e1, Map e2) {
		if (!e1.containsKey("event_start_time") && !e2.containsKey("event_start_time")) {
			return 0; 
		}
		
		if (!e1.containsKey("event_start_time")) {
			return e2.get("event_start_time") == null ? 0 : 1; 
		} else if (!e2.containsKey("event_start_time")) {
			return e1.get("event_start_time") == null ? 0 : -1; 
		}
		
		Timestamp e1_event_start_time = Timestamp.valueOf(e1.get("event_start_time").toString()); 
		Timestamp e2_event_start_time = Timestamp.valueOf(e2.get("event_start_time").toString()); 
		
		if (e1_event_start_time.equals(e2_event_start_time)) {
			return 0; 
		}
		return e1_event_start_time.before(e2_event_start_time) ? -1 : 1; 
	}
	
}

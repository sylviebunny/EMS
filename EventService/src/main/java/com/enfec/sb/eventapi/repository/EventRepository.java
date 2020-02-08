package com.enfec.sb.eventapi.repository;

import com.enfec.sb.eventapi.model.EventTable;

public interface EventRepository {
	
	public Object getOrganizerInfo(int organizer_id);
	public int registerOrganizer(EventTable organizerTable);
	public int updateOrganizer(EventTable organizerTable);
	
}

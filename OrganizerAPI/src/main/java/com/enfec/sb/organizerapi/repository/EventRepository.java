package com.enfec.sb.organizerapi.repository;

import com.enfec.sb.organizerapi.model.EventTable;

public interface EventRepository {
	
	public Object getOrganizerInfo(int organizer_id);
	public int registerOrganizer(EventTable organizerTable);
	public int updateOrganizer(EventTable organizerTable);
	
}

package com.enfec.sb.organizerapi.repository;

import com.enfec.sb.organizerapi.model.OrganizerContactTable;
import com.enfec.sb.organizerapi.model.OrganizerTable;

public interface OrganizerRepository {

	public Object getOrganizerInfo(int organizer_id);
	public int registerOrganizer(OrganizerTable organizerTable);
	public int createOrganizerContact(OrganizerContactTable organizerContactTable); 
	
	public int updateOrganizer(OrganizerTable organizerTable);
}

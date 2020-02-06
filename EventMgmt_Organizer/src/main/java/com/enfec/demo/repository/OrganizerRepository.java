package com.enfec.demo.repository;

import com.enfec.demo.model.OrganizerTable;

public interface OrganizerRepository {
	
	public Object getOrganizerInfo(int Organizer_ID);
	public int registerOrganizer(OrganizerTable organizerTable);
	public int updateOrganizer(OrganizerTable organizerTable);
}

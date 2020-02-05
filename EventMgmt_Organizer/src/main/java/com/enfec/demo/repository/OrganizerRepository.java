package com.enfec.demo.repository;

import com.enfec.demo.model.OrganizerTable;

public interface OrganizerRepository {
	
	public Object getOrganizerInfo(int organizer_id);
	public int registerOrganizer(OrganizerTable deviceInfoTable);
	public int updateOrganizer(OrganizerTable deviceInfoTable);
}

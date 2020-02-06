package com.enfec.sb.organizerapi.repository;

import com.enfec.sb.organizerapi.model.OrganizerTable;

public interface OrganizerRepository {

	public Object getOrganizerInfo(int accnt_id);
	public int registerOrganizer(OrganizerTable organizerTable);
	
	public int updateOrganizer(OrganizerTable organizerTable);
	
}

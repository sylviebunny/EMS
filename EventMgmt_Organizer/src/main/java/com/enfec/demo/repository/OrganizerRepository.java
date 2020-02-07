package com.enfec.demo.repository;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.OrganizerTable;

public interface OrganizerRepository {
	
	public Object getOrganizerInfo(int Organizer_ID);
	public int createOrganizer(OrganizerTable organizerTable);
	public int createAddress(Address address);
	public int updateOrganizer(OrganizerTable organizerTable);
	public int updateAddress(Address address);
	public int deleteOrganizer(int Organizer_ID);
	
}

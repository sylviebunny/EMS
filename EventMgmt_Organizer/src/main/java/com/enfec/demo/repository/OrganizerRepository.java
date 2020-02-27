package com.enfec.demo.repository;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.OrganizerContactTable;
import com.enfec.demo.model.OrganizerTable;

public interface OrganizerRepository {
	
	public int createOrganizer(OrganizerTable organizerTable);	
	public Object getOrganizerInfo(int Organizer_ID);
	public int updateOrganizer(OrganizerTable organizerTable);	
	public int deleteOrganizer(int Organizer_ID);

	public int createAddress(Address address);
	public Object getAddressInfo(int Organizer_ID);
	public int updateAddress(Address address);

	public int createOrganizerContact(OrganizerContactTable organizerContactTable); 
	public Object getOrganizerContactInfo(int organizer_id); 
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable); 
	
	public boolean isMatching(String OEmail, String oPwd);
}

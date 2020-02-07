package com.enfec.demo.repository;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.OrganizerContactTable;
import com.enfec.demo.model.OrganizerTable;

public interface OrganizerRepository {
	
	public Object getOrganizerInfo(int Organizer_ID);
	public int createOrganizer(OrganizerTable organizerTable);	
	public int updateOrganizer(OrganizerTable organizerTable);	
	public int deleteOrganizer(int Organizer_ID);
	
	public Object getAddressInfo(int Organizer_ID);
	public int createAddress(Address address);
	public int updateAddress(Address address);
	
	public Object getOrganizerContactInfo(int organizer_id); 
	public int createOrganizerContact(OrganizerContactTable organizerContactTable); 
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable); 
}

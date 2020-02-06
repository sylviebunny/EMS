package com.enfec.sb.organizersapi.repository;

import com.enfec.sb.organizersapi.model.OrganizersTable;

public interface OrganizersRepository {

	public Object getOrganizer(String organizer_id); //id type!!!!!
	public int registerOrganizer(OrganizersTable organizersTable);
	
	public int updateOrganizer(OrganizersTable organizersTable);
	
	public int deleteOrganizer(OrganizersTable organizersTable);
	
}

package com.enfec.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class OrganizerContactRowmapper implements RowMapper<OrganizerContactTable>{
	
	@Override
	public OrganizerContactTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrganizerContactTable organizerContactTable = new OrganizerContactTable();
		organizerContactTable.setOrganizer_id(rs.getInt("Organizer_ID"));
		organizerContactTable.setContact_name((rs.getString("Contact_Name")));
		organizerContactTable.setTelephone((rs.getString("Telephone")));
		organizerContactTable.setWeb_site_address((rs.getString("Web_Site_Address")));
		organizerContactTable.setAddress_id(rs.getInt("Address_ID"));
		
		return organizerContactTable;
	}
}

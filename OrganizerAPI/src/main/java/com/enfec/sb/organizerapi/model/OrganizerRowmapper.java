package com.enfec.sb.organizerapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;


public class OrganizerRowmapper implements RowMapper<OrganizerTable> {


	
	
	@Override
	public OrganizerTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrganizerTable organizerTable = new OrganizerTable();
		organizerTable.setOrganizer_id(rs.getInt("Organizer_ID"));
		organizerTable.setOrganizer_name((rs.getString("Organizer_Name")));
		organizerTable.setEmail_address((rs.getString("Email_Address")));
		organizerTable.setPassword((rs.getString("Password")));
		organizerTable.setOther_details((rs.getString("Other_Details")));
		
		return organizerTable;
	}
	
	
	
}

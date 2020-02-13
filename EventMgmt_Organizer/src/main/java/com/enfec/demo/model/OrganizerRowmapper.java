package com.enfec.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;



public class OrganizerRowmapper implements RowMapper<OrganizerTable>{
	@Override
	public OrganizerTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrganizerTable organizerTable = new OrganizerTable();
		organizerTable.setOrganizer_ID(rs.getInt("Organizer_ID"));
		organizerTable.setOrganizer_Name((rs.getString("Organizer_Name")));
		organizerTable.setEmail_Address((rs.getString("Email_Address")));
		organizerTable.setPassword((rs.getString("Password")));
		organizerTable.setOther_Details((rs.getString("Other_Details")));
		
		return organizerTable;
	}
}

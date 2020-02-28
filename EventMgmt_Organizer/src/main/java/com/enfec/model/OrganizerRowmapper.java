package com.enfec.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Organizer Rowmapper for 'Organizers' table
* Class: OrganizerRowmapper
*
************************************************/
public class OrganizerRowmapper implements RowMapper<OrganizerTable> {

	/**
	 * Map each MySql column's content to organizer table
	 * 
	 * @param rs: ResultSet
	 * @param rowNum
	 * @throws SQLException when column doesn't exist in database
	 * @return OrganizerTable
	 */
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

package com.enfec.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Organizer contact Rowmapper for 'Contacts' table
* Class: OrganizerContactRowmapper
*
************************************************/
public class OrganizerContactRowmapper implements RowMapper<OrganizerContactTable> {

	/**
	 * Map each MySql column's content to organizer contact table
	 * 
	 * @param rs:    ResultSet
	 * @param rowNum
	 * @throws SQLException when column doesn't exist in database
	 * @return OrganizerContactTable
	 */
	@Override
	public OrganizerContactTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrganizerContactTable organizerContactTable = new OrganizerContactTable();
		organizerContactTable.setContact_id(rs.getInt("Contact_ID"));
		organizerContactTable.setOrganizer_id(rs.getInt("Organizer_ID"));
		organizerContactTable.setContact_name((rs.getString("Contact_Name")));
		organizerContactTable.setTelephone((rs.getString("Telephone")));
		organizerContactTable.setWeb_site_address((rs.getString("Web_Site_Address")));
		organizerContactTable.setAddress_id(rs.getInt("Address_ID"));

		return organizerContactTable;
	}
}

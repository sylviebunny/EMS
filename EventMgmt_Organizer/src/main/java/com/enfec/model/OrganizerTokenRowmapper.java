package com.enfec.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/************************************************
* Author: Chad Chai
* Assignment: Organizer Token Rowmapper for 'Organizer_Token' table
* Class: OrganizerTokenRowmapper
************************************************/
public class OrganizerTokenRowmapper implements RowMapper<OrganizerTokenTable> {
	/**
     * Map each MySql column's content to organizer token table
     * 
     * @param rs: ResultSet
     * @param rowNum
     * @throws SQLException when column doesn't exist in database 
     * @return OrganizerTokenTable
     */
	@Override
	public OrganizerTokenTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		OrganizerTokenTable oTokenTable = new OrganizerTokenTable();
		oTokenTable.setOrganizerTokenID(rs.getInt("OT_ID"));
		oTokenTable.setOrganizerEmail(rs.getString("OEmail"));
		oTokenTable.setOrganizerToken(rs.getString("OToken"));
		oTokenTable.setOrganizerExpiryDate(rs.getTimestamp("OTExpire"));
		
		return oTokenTable;
	}

}

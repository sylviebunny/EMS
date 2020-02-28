package com.enfec.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class OrganizerTokenRowmapper implements RowMapper<OrganizerTokenTable>{
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

package com.enfec.sb.organizersapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;


public class OrganizersRowmapper implements RowMapper<OrganizersTable> {


	
	
	@Override
	public OrganizersTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrganizersTable organizersTable = new OrganizersTable();
		organizersTable.setOrganizer_id(rs.getInt("Organizer_ID"));
		organizersTable.setOrganizer_name((rs.getString("Organizer_Name")));
		organizersTable.setEmail((rs.getString("Email_Address")));
		//organizersTable.setPassword(rs.getString("Password"));
		organizersTable.setPassword(rs.getString("Password")==null ? null:(rs.getString("Password")));
		organizersTable.setDetails((rs.getString("Other_Details")));
		
		
		return organizersTable;
	}
	
	
	
}

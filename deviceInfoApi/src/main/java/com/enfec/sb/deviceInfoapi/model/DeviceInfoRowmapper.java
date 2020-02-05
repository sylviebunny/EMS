package com.enfec.sb.deviceInfoapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;


public class DeviceInfoRowmapper implements RowMapper<DeviceInfoTable> {


	
	
	@Override
	public DeviceInfoTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		DeviceInfoTable deviceInfoTable = new DeviceInfoTable();
		deviceInfoTable.setOrganizer_id(rs.getInt("Organizer_ID"));
		deviceInfoTable.setOrganizer_name((rs.getString("Organizer_Name")));
		deviceInfoTable.setEmail_address((rs.getString("Email_Address")));
		deviceInfoTable.setPassword((rs.getString("Password")));
		deviceInfoTable.setOther_details((rs.getString("Other_Details")));
		
		return deviceInfoTable;
	}
	
	
	
}

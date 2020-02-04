package com.enfec.sb.deviceInfoapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;


public class DeviceInfoRowmapper implements RowMapper<DeviceInfoTable> {


	
	
	@Override
	public DeviceInfoTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		DeviceInfoTable deviceInfoTable = new DeviceInfoTable();
		deviceInfoTable.setAccnt_id(rs.getInt("ACCNT_ID"));
		deviceInfoTable.setChip_id((rs.getString("CHIP_ID")));
		deviceInfoTable.setFirmware((rs.getString("FIRMWARE")));
		deviceInfoTable.setMake((rs.getString("MAKE")));
		deviceInfoTable.setModel((rs.getString("MODEL")));
		deviceInfoTable.setSerial_number((rs.getString("SERIAL_NUMBER")));
		deviceInfoTable.setWifi_pwd(rs.getString("WIFI_PWD")==null ? null:new String((Base64.getDecoder().decode(rs.getString("WIFI_PWD")))));
		deviceInfoTable.setWifi_ssid((rs.getString("WIFI_SSID")));
		
		
		return deviceInfoTable;
	}
	
	
	
}

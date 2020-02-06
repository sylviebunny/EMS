package com.enfec.sb.organizerapi.repository;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.enfec.sb.organizerapi.model.OrganizerRowmapper;
import com.enfec.sb.organizerapi.model.OrganizerTable;

@Component
public class OrganizerRepositoryImpl implements OrganizerRepository {
	private static final Logger logger = LoggerFactory.getLogger(OrganizerRepositoryImpl.class);
	
	final String SELECT_DEVICE = "select Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details from Organizers where Organizer_ID = ?; ";

//	final String REGISTER_DEVICE = "INSERT INTO DEVICE_INFO(ACCNT_ID,CHIP_ID,SERIAL_NUMBER,MAKE,MODEL,FIRMWARE,WIFI_SSID,WIFI_PWD) VALUES "
//			+ "(:accnt_id,:chip_id,:serial_number,:make,:firmware,:model,:wifi_ssid,:wifi_pwd)";
	final String REGISTER_DEVICE = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details) VALUES "
			+ "(:organizer_id,:organizer_name,:email_address,:password,:other_details)";
	
//	final String UPDATE_DEVICE_INFO = "UPDATE DEVICE_INFO SET SERIAL_NUMBER = :serial_number ,MAKE=:make,MODEL=:model"
//			+ ",FIRMWARE=:firmware,WIFI_SSID=:wifi_ssid,WIFI_PWD=:wifi_pwd where ACCNT_ID = :accnt_id AND CHIP_ID =:chip_id" ;	
	final String UPDATE_DEVICE_INFO = "UPDATE Organizers SET Organizer_Name =:organizer_name, Email_Address= :email_address, PASSWORD =:password, Other_Details =:other_details WHERE Organizer_ID =:organizer_id"; 
	
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public  List<OrganizerTable> getOrganizerInfo(int accnt_id) {
		
		return jdbcTemplate.query(SELECT_DEVICE,new Object[] { accnt_id }, new OrganizerRowmapper());
	}

	@Override
	public int registerDevice(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_DEVICE, pramSource);
		
		return affectedRow;
	}

	@Override
	public int updateDevice(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_DEVICE_INFO, pramSource);
		
		return affectedRow;

	}
	
	private Map<String, Object> organizerMap(OrganizerTable organizerTable) {
		Map<String, Object>param = new HashMap<>();
	
			if (organizerTable.getOrganizer_id() != 0) {
				param.put("organizer_id", organizerTable.getOrganizer_id());
			} else {
				throw new NullPointerException("Accnt_id cannot be null");
			}
		
		param.put("organizer_name", organizerTable.getOrganizer_name().isEmpty() ? null:organizerTable.getOrganizer_name());
		param.put("email_address", organizerTable.getEmail_address().isEmpty() ? null:organizerTable.getEmail_address());
		param.put("password", organizerTable.getPassword().isEmpty() ? null:organizerTable.getPassword());
		param.put("other_details", organizerTable.getOther_details().isEmpty() ? null:organizerTable.getOther_details());
		return param;
	}

	

}


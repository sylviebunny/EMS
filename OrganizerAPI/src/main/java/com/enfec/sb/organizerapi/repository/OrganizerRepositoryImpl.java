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
	
	final String SELECT_ORGANIZER = "select Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details from Organizers where Organizer_ID = ?; ";

	final String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details) VALUES "
			+ "(:organizer_id,:organizer_name,:email_address,:password,:other_details)";
	
	final String UPDATE_ORGANIZER_INFO = "UPDATE Organizers SET Organizer_Name =:organizer_name, Email_Address= :email_address, PASSWORD =:password, Other_Details =:other_details WHERE Organizer_ID =:organizer_id"; 
	
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public  List<OrganizerTable> getOrganizerInfo(int accnt_id) {
		
		return jdbcTemplate.query(SELECT_ORGANIZER,new Object[] { accnt_id }, new OrganizerRowmapper());
	}

	@Override
	public int registerOrganizer(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);
		
		return affectedRow;
	}

	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_INFO, pramSource);
		
		return affectedRow;

	}
	
	private Map<String, Object> organizerMap(OrganizerTable organizerTable) {
		Map<String, Object>param = new HashMap<>();
	
			if (organizerTable.getOrganizer_id() != 0) {
				param.put("organizer_id", organizerTable.getOrganizer_id());
			} else {
				throw new NullPointerException("Organizer_ID cannot be null");
			}
		
		param.put("organizer_name", organizerTable.getOrganizer_name().isEmpty() ? null:organizerTable.getOrganizer_name());
		param.put("email_address", organizerTable.getEmail_address().isEmpty() ? null:organizerTable.getEmail_address());
		param.put("password", organizerTable.getPassword().isEmpty() ? null:organizerTable.getPassword());
		param.put("other_details", organizerTable.getOther_details().isEmpty() ? null:organizerTable.getOther_details());
		return param;
	}

	

}


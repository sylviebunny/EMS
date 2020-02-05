package com.enfec.demo.repository;

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

import com.enfec.demo.model.OrganizerRowmapper;
import com.enfec.demo.model.OrganizerTable;

@Component
public class OrganizerRepositoryImpl implements OrganizerRepository{
	private static final Logger logger = LoggerFactory.getLogger(OrganizerRepositoryImpl.class);
	
	final String SELECT_ORGANIZER = "select Organizer_ID,Organizer_Name,Email_Address,Password,Other_Details from Organizers where Organizer_ID =?";

	final String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, Password, Other_Details) VALUES "
			+ "(:organizer_id,:organizer_name,:email_address,:password,:other_details)";
	
	final String UPDATE_ORGANIZER_INFO = "UPDATE Organizers SET Email_Address = :Email_Address ,Password=:Password,Other_Details=:Other_Details"
			+ "where ACCNT_ID = :accnt_id AND CHIP_ID =:chip_id" ;	
	
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<OrganizerTable> getOrganizerInfo(int Organizer_ID) {
		return jdbcTemplate.query(SELECT_ORGANIZER,new Object[] { Organizer_ID }, new OrganizerRowmapper());
	}
	
	@Override
	public int registerOrganizer(OrganizerTable OrganizerTable) {
		int affectedRow;
		Map<String, Object> param = OrganizerMap(OrganizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);
		
		return affectedRow;
	}
	
	@Override
	public int updateOrganizer(OrganizerTable OrganizerTable) {
		int affectedRow;
		Map<String, Object> param = OrganizerMap(OrganizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating Organizer Info : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_INFO, pramSource);
		
		return affectedRow;
	}
	
	
	private Map<String, Object> OrganizerMap(OrganizerTable OrganizerTable) {
		Map<String, Object>param = new HashMap<>();
	
		if (OrganizerTable.getOrganizer_ID() != 0) {
			param.put("Organizer_ID", OrganizerTable.getOrganizer_ID());
		} else {
			throw new NullPointerException("Organizer_ID cannot be null");
		}
		
		param.put("Organizer_Name", OrganizerTable.getOrganizer_Name().isEmpty() ? null:OrganizerTable.getOrganizer_Name());
		param.put("Email_Address", OrganizerTable.getEmail_Address().isEmpty() ? null:OrganizerTable.getEmail_Address());
		param.put("Password", OrganizerTable.getPassword().isEmpty() ? null:OrganizerTable.getPassword());
		param.put("Other_Details", OrganizerTable.getOther_Details().isEmpty() ? null:OrganizerTable.getOther_Details());
		return param;
	}
}

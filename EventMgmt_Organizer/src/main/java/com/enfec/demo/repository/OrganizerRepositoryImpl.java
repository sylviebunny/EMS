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
//	private static final Logger logger = LoggerFactory.getLogger(OrganizerRepositoryImpl.class);
	
	final String SELECT_ORGANIZER = "select Organizer_ID,Organizer_Name,Email_Address,Password,Other_Details FROM Organizers where Organizer_ID = ?; ";

	final String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, Password, Other_Details) VALUES "
			+ "(:Organizer_ID,:Organizer_Name,:Email_Address,:Password,:Other_Details)";
	
	final String UPDATE_ORGANIZER = "UPDATE Organizers SET Email_Address = :Email_Address, Password = :Password, Other_Details = :Other_Details WHERE Organizer_ID = :Organizer_ID AND Organizer_Name = :Organizer_Name";	
	
	final String DELETE_ORGANIZER = "DELETE FROM Organizers WHERE Organizer_ID = ?; ";
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<OrganizerTable> getOrganizerInfo(int Organizer_ID) {
		return jdbcTemplate.query(SELECT_ORGANIZER,new Object[] { Organizer_ID }, new OrganizerRowmapper());
	}
	
	@Override
	public int registerOrganizer(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = OrganizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);
		
		return affectedRow;
	}
	
	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = OrganizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
//		logger.info("Updating Organizer Info : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER, pramSource);
		
		return affectedRow;
	}
	
	@Override
	public int deleteOrganizer(int Organizer_ID) {
		int affectedRow = jdbcTemplate.update(DELETE_ORGANIZER, Organizer_ID);
		return affectedRow;
	}
	
	
	private Map<String, Object> OrganizerMap(OrganizerTable organizerTable) {
		Map<String, Object>param = new HashMap<>();
	
		if(organizerTable.getOrganizer_ID() != 0) {
			param.put("Organizer_ID", organizerTable.getOrganizer_ID());
		} else {
			throw new NullPointerException("Organizer_ID cannot be null");
		}
		
		param.put("Organizer_Name", organizerTable.getOrganizer_Name().isEmpty() ? null:organizerTable.getOrganizer_Name());
		param.put("Email_Address", organizerTable.getEmail_Address().isEmpty() ? null:organizerTable.getEmail_Address());
		param.put("Password", organizerTable.getPassword().isEmpty() ? null:organizerTable.getPassword());
		param.put("Other_Details", organizerTable.getOther_Details().isEmpty() ? null:organizerTable.getOther_Details());
		return param;
	}
}

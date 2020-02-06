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

import com.enfec.sb.organizerapi.model.OrganizerContactTable;
import com.enfec.sb.organizerapi.model.OrganizerRowmapper;
import com.enfec.sb.organizerapi.model.OrganizerTable;

@Component
public class OrganizerRepositoryImpl implements OrganizerRepository {
	private static final Logger logger = LoggerFactory.getLogger(OrganizerRepositoryImpl.class);
	
	final String SELECT_ORGANIZER = "select Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details from Organizers where Organizer_ID = ?; ";

	final String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details) VALUES "
			+ "(:organizer_id, :organizer_name,:email_address,:password,:other_details)";
	
	String UPDATE_ORGANIZER_INFO_PREFIX = "UPDATE Organizers SET "; 
	String UPDATE_ORGANIZER_INFO_SUFFIX = " WHERE Organizer_ID = :organizer_id";
	
	final String CREATE_REGISTER_CONTACT_INFO = "INSERT INTO Contacts(Organizer_ID, Address_ID, Contact_Name, Telephone, Web_Site_Address) VALUES"
			+ "(:organizer_id, :address_id, :contact_name, :telephone, :web_site_address)"; 
	
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
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);
		
		return affectedRow; 
	}
	
	@Override
	public int createOrganizerContact(OrganizerContactTable organizerContactTable) {
		// Insert contact information of organizer into organizer table
		try {
			int affectedRow; 
			Map<String, Object> param = getOrganizerContactMap(organizerContactTable); 
			
			SqlParameterSource pramSource = new MapSqlParameterSource(param); 
			affectedRow = namedParameterJdbcTemplate.update(CREATE_REGISTER_CONTACT_INFO, pramSource); 
			return affectedRow; 
		} catch (RuntimeException rt) {
			return -1; 
		}
	}
	
	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_ORGANIZER_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("organizer_id"))
			{
				UPDATE_ORGANIZER_INFO.append(key + "=:" + key + ",");
			}
		}
		// remove the last colon
		UPDATE_ORGANIZER_INFO = UPDATE_ORGANIZER_INFO.deleteCharAt(UPDATE_ORGANIZER_INFO.length() - 1); 
		
		String UPDATE_ORGANIZER = UPDATE_ORGANIZER_INFO_PREFIX + UPDATE_ORGANIZER_INFO + UPDATE_ORGANIZER_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER, pramSource);
		
		return affectedRow;

	}
	
	private Map<String, Object> organizerMap(OrganizerTable organizerTable) {
		// Mapping organizer's information query's variable to URL POST body
		Map<String, Object>param = new HashMap<>();
	
			if (organizerTable.getOrganizer_id() != 0) {
				param.put("organizer_id", organizerTable.getOrganizer_id());
			} else {
				throw new NullPointerException("Organizer_ID cannot be null");
			}
		
		param.put("organizer_name", organizerTable.getOrganizer_name() == null || organizerTable.getOrganizer_name().isEmpty() ? null:organizerTable.getOrganizer_name());
		param.put("email_address", organizerTable.getEmail_address() == null || organizerTable.getEmail_address().isEmpty() ? null:organizerTable.getEmail_address());
		param.put("password", organizerTable.getPassword() == null || organizerTable.getPassword().isEmpty() ? null:organizerTable.getPassword());
		param.put("other_details", organizerTable.getOther_details() == null || organizerTable.getOther_details().isEmpty() ? null:organizerTable.getOther_details());
		return param;
	}
	
	private Map<String, Object> getOrganizerContactMap(OrganizerContactTable organizerContactTable) {
		Map<String, Object> contactMap = new HashMap<>(); 
		contactMap.put("organizer_id", organizerContactTable.getOrganizer_id()); 
		contactMap.put("address_id", organizerContactTable.getAddress_id()); 
		contactMap.put("contact_name", organizerContactTable.getContact_name() == null || organizerContactTable.getContact_name().isEmpty() ? null : organizerContactTable.getContact_name()); 
		contactMap.put("telephone", organizerContactTable.getTelephone() == null || organizerContactTable.getTelephone().isEmpty() ? null : organizerContactTable.getTelephone()); 
		contactMap.put("web_site_address", organizerContactTable.getWeb_site_address() == null || organizerContactTable.getWeb_site_address().isEmpty() ? null : organizerContactTable.getWeb_site_address()); 
		return contactMap; 
	}

}


package com.enfec.sb.organizerapi.repository;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.enfec.sb.organizerapi.model.EventRowmapper;
import com.enfec.sb.organizerapi.model.EventTable;

@Component
public class EventRepositoryImpl implements EventRepository {
	private static final Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);

	final String SELECT_ORGANIZER = "select * from Events where Event_ID = ?; ";

	final String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, PASSWORD, Other_Details) VALUES "
			+ "(:organizer_id, :organizer_name,:email_address,:password,:other_details)";
	
	String UPDATE_ORGANIZER_INFO_PREFIX = "UPDATE Organizers SET "; 
	String UPDATE_ORGANIZER_INFO_SUFFIX = " WHERE Organizer_ID = :organizer_id";
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public  List<EventTable> getOrganizerInfo(int accnt_id) {
		
		return jdbcTemplate.query(SELECT_ORGANIZER,new Object[] { accnt_id }, new EventRowmapper());
	}

	@Override
	public int registerOrganizer(EventTable organizerTable) {
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);
		
		return affectedRow; 
	}
	
	@Override
	public int updateOrganizer(EventTable organizerTable) {
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
	
	private Map<String, Object> organizerMap(EventTable organizerTable) {
		// Mapping organizer's information query's variable to URL POST body
		Map<String, Object>param = new HashMap<>();
	
			if (organizerTable.getOrganizer_id() != 0) {
				param.put("organizer_id", organizerTable.getOrganizer_id());
			} else {
				throw new NullPointerException("Organizer_ID cannot be null");
			}
		
//		param.put("organizer_name", organizerTable.getOrganizer_name() == null || organizerTable.getOrganizer_name().isEmpty() ? null:organizerTable.getOrganizer_name());
		return param;
	}
	
}


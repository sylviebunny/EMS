package com.enfec.sb.eventapi.repository;

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

import com.enfec.sb.eventapi.model.EventRowmapperByID;
import com.enfec.sb.eventapi.model.EventTable;

@Component
public class EventRepositoryImpl implements EventRepository {
	private static final Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);

	private static final String SELECT_EVENT_BY_NAME = "SELECT * FROM Events WHERE Event_Name =?";

	final String SELECT_EVENT_BY_ID = "SELECT * FROM Events WHERE Event_ID=?;";

	final String REGISTER_EVENT = "INSERT INTO Events(Event_Status_Code, Event_Type_Code, Free_or_Commercial_Code, Organizer_ID, Venue_ID, "
			+ "Event_Name, Event_Start_Date, Event_End_Date, Number_of_Participants, Derived_Days_Duration, Event_Cost, Discount, Comments) VALUES "
			+ "(:event_status_code, :event_type_code, :free_or_commercial_code, :organizer_id, "
			+ ":venue_id, :event_name, :event_start_date, :event_end_date, :number_of_participants, :derived_days_duration, :event_cost, :discount, :comments)";
	
	String UPDATE_EVENT_INFO_PREFIX = "UPDATE Events SET "; 
	String UPDATE_EVENT_INFO_SUFFIX = " WHERE Organizer_ID = :organizer_id";
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public  List<EventTable> getEventInfoByID(int event_id) {
		// Implementation for GET event by ID
		EventRowmapperByID e = new EventRowmapperByID(); 
		return jdbcTemplate.query(SELECT_EVENT_BY_ID, new Object[] {event_id}, e);
	}
	
	public List<EventTable> getEventInfoByName(String event_name) {
		// Implementation for GET event by EVENT_NAME
		return jdbcTemplate.query(SELECT_EVENT_BY_NAME, new EventRowmapperByID(), event_name);
	}

	@Override
	public int createEvent (EventTable eventTable) {
		// Create an event
		int affectedRow;
		Map<String, Object> param = eventMap(eventTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_EVENT, pramSource);
		
		return affectedRow; 
	}
	
	@Override
	public int updateEvent(EventTable eventTable) {
		int affectedRow;
		Map<String, Object> param = eventMap(eventTable);
		
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
		
		String UPDATE_ORGANIZER = UPDATE_EVENT_INFO_PREFIX + UPDATE_ORGANIZER_INFO + UPDATE_EVENT_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER, pramSource);
		
		return affectedRow;

	}
	
	private Map<String, Object> eventMap(EventTable eventTable) {
		// Mapping event's information query's variable to URL POST body
		Map<String, Object>param = new HashMap<>();
		
		param.put("event_status_code", eventTable.getEvent_status_code() == null || eventTable.getEvent_status_code().isEmpty() ? 
				null : eventTable.getEvent_status_code());
		
		param.put("event_type_code", eventTable.getEvent_type_code() == null || eventTable.getEvent_type_code().isEmpty() ? 
				null : eventTable.getEvent_type_code());
		
		param.put("free_or_commercial_code", eventTable.getFree_or_commercial_code() == null ? 
				null : eventTable.getFree_or_commercial_code());
		
		param.put("organizer_id", eventTable.getOrganizer_id());		// Cannot be null
		
		param.put("venue_id", eventTable.getVenue_id());			// Cannot be null 
		
		param.put("event_name", eventTable.getEvent_name() == null || eventTable.getEvent_name().isEmpty() ? 
				null : eventTable.getEvent_name());
		
		param.put("event_start_date", eventTable.getEvent_start_date() == null ? 
				null : eventTable.getEvent_start_date());
		
		param.put("event_end_date", eventTable.getEvent_end_date() == null ? 
				null : eventTable.getEvent_end_date());
		
		param.put("number_of_participants", eventTable.getNumber_of_participants() == null ? 
				null : eventTable.getNumber_of_participants());
		
		param.put("derived_days_duration", eventTable.getDerived_days_duration() == null || eventTable.getDerived_days_duration().isEmpty() ? 
				null : eventTable.getDerived_days_duration());	
		
		param.put("event_cost", eventTable.getEvent_cost() == null ? 
				null : eventTable.getEvent_cost());
		
		param.put("discount", eventTable.getDiscount() == null ? 
				null : eventTable.getDiscount());
		
		param.put("comments", eventTable.getComments() == null || eventTable.getComments().isEmpty() ? 
				null :eventTable.getComments());
				
		return param;
	}
	
}


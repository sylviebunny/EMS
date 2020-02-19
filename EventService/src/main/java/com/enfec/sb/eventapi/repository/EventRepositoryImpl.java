package com.enfec.sb.eventapi.repository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.rmi.NotBoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.enfec.sb.eventapi.model.EventRowmapper;
import com.enfec.sb.eventapi.model.EventTable;
import com.enfec.sb.eventapi.model.SortByDistance;
import com.enfec.sb.eventapi.model.SortByStartTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class EventRepositoryImpl implements EventRepository {
	private static final Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);

	final String REGISTER_EVENT = "INSERT INTO Events(Event_Status_Code, Event_Type_Code, Commercial_Type, Organizer_ID, Venue_ID, "
			+ "Event_Name, Event_Start_Time, Event_End_Time, Timezone, Number_of_Participants, Derived_Days_Duration, Event_Cost, Discount, Comments) VALUES "
			+ "(:event_status_code, :event_type_code, :commercial_type, :organizer_id, "
			+ ":venue_id, :event_name, :event_start_time, :event_end_time, :timezone, :number_of_participants, :derived_days_duration, :event_cost, :discount, :comments)";
	
	String UPDATE_EVENT_INFO_PREFIX = "UPDATE Events SET "; 
	String UPDATE_EVENT_INFO_SUFFIX = " WHERE Event_ID = :event_id AND Organizer_ID =:organizer_id";
	
	private static final String DELETE_EVENT = "DELETE FROM Events WHERE Event_ID =?";

	private static final String GET_ALL_EVENT = "SELECT Event_ID, Events.Event_Status_Code, Events.Event_Type_Code, Commercial_Type, Events.Organizer_ID, Events.Venue_ID, Event_Name, Event_Start_Time, Event_End_Time, Timezone, Number_of_Participants, Derived_Days_Duration, Event_Cost, Discount, Comments, Venue_Name, Venues.Other_Details, Street1, Street2, City, State, Zipcode, Latitude, Longitude, Event_Status_Description, Event_Type_Description, Organizer_Name\n" + 
			"FROM Events, Venues, Venue_Address, Ref_Event_Status, Ref_Event_Types, Organizers\n" + 
			"WHERE Events.Venue_ID = Venues.Venue_ID AND Events.Venue_ID = Venue_Address.Venue_ID AND Events.Event_Status_Code = Ref_Event_Status.Event_Status_Code AND Events.Event_Type_Code = Ref_Event_Types.Event_Type_Code AND Organizers.Organizer_ID = Events.Organizer_ID; ";

private static final String GET_ALL_VENUE = "SELECT * FROM Venue_Address";
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate; 
	
	/* 
	 * ---------------------------------- Event GET ----------------------------------
	 * 
	 */
	
	/*
	 * 	----------------------------------------------------------------------------------------------------
	 * 		Get filtered events by typing anything like state/city/commericial_type/event_type/
	 * 		zipcode/event_name ...
	 *  ----------------------------------------------------------------------------------------------------
	 */
	@Override
	public List<Map> getFilteredEvents(List<EventTable> allEvent, String str) throws NotBoundException {
		// Use filter bar, anyone can type anything, like WA/seattle/non-profitable/festival/98002
		// We should not match str with ID or code information. 
		
		// Convert EventTable to Map
		List<Map> allEventMap = new ArrayList<>(); 
		for (EventTable et: allEvent) {
			allEventMap.add(eventMap(et, et.getEvent_id())); 
		} 
		
		// str can be null, if it's null, then print out allEvents
		if (str == null || str.isEmpty()) {
			return allEventMap; 
		}
		
		// If str is a valid zipcode, then we need to get the events mapping from zipcode to location info
		Integer zipcode = getZipcode(str); 
		if (zipcode != null) {
			try {
				return getEventByZipcode(allEventMap, zipcode); 
			} catch (NotBoundException nbe) {
				throw nbe; 
			}
		}
		
		List<Map> afterFilter = new ArrayList<>(); 
		
		// We don't match str with IDs
		final HashSet<String> ignoreKeys = getIgnoreKeys(); 
		
		// Iterate through whole events map and find events that fit into str
		for (Map<String,Object> eachEvent: allEventMap) {
			for (String key: eachEvent.keySet()) {
				if (!ignoreKeys.contains(key) && eachEvent.get(key) != null) {
					String val = eachEvent.get(key).toString();
					if (val.toLowerCase().contains(str.toLowerCase())) {
						afterFilter.add(eachEvent); 
						break; 
					}
				}
			}
		}
		Collections.sort(afterFilter, new SortByStartTime());
		return afterFilter; 
	}
	
	private HashSet<String> getIgnoreKeys() {
		
		HashSet<String> ignoreKeys = new HashSet<>(); 
		
		ignoreKeys.add("event_type_code"); 
		ignoreKeys.add("event_id"); 
		ignoreKeys.add("event_status_code"); 
		ignoreKeys.add("organizer_id"); 
		ignoreKeys.add("venue_id"); 
		
		return ignoreKeys; 
	}
	
	/*
	 * 	----------------------------------------------------------------------------------------------------
	 * 				Get all the events within a date range, combining with other criteria
	 *  ----------------------------------------------------------------------------------------------------
	 */
	/*
	 * 	Get all events within a date range 
	 */
	@Override
	public List<Map> getFilteredEvents(List<EventTable> allEvent, Timestamp start_date, Timestamp end_date) {
		// Convert EventTable to Map
		List<Map> allEventMap = new ArrayList<>(); 
		for (EventTable et: allEvent) {
			allEventMap.add(eventMap(et, et.getEvent_id())); 
		} 
		
		
		List<Map> dateRangeEvents = new ArrayList<>();
		for (Map eachEvent: allEventMap) {
			Timestamp eventStartTime = Timestamp.valueOf((String)eachEvent.get("event_start_time")); 
			Timestamp eventEndTime = Timestamp.valueOf((String)eachEvent.get("event_end_time"));
			if (eventStartTime.after(start_date) && eventStartTime.before(end_date)) {
				// the event that is within the date period
				dateRangeEvents.add(eachEvent); 
			}
		}
		
		Collections.sort(dateRangeEvents, new SortByStartTime()); 
		return dateRangeEvents; 
	}
	
	@Override
	public List<Map> getEventByEventType(List<Map> inputEvents, String event_type) {
		if (event_type == null || event_type.length() == 0) {
			return inputEvents; 
		}
		
		List<Map> resultEvents = new ArrayList<>();
		
		// Get all events that matches this type
		for (Map eachEvent: inputEvents) {
			String currType = eachEvent.get("event_type_description").toString(); 
			if (currType.toLowerCase().contains(event_type.toLowerCase())) {
				resultEvents.add(eachEvent); 
			}
		}
		return resultEvents; 
	}
	
	/*
	 * 	----------------------------------------------------------------------------------------------------
	 * 				Get an event based on event_id
	 *  ----------------------------------------------------------------------------------------------------
	 */
	public List<EventTable> getEventByID(int event_id) {
		// Implementation for GET event by EVENT_ID
		List<EventTable> allEvents = getAllEvents(); 
		
		List<EventTable> resultEvent = new ArrayList<>(); 
		
		for (EventTable eachEvent: allEvents) {
			int currEventID = eachEvent.getEvent_id(); 
			if (currEventID == event_id) {
				resultEvent.add(eachEvent); 
			}
		}
		return resultEvent; 
	}
	
	/*
	 * 	----------------------------------------------------------------------------------------------------
	 * 				zip code related functions
	 *  ----------------------------------------------------------------------------------------------------
	 */
	/*
	 * 		Based on given zipcode, get all events within a distance. 
	 */
	public List<Map> getEventByZipcode(List<Map> inputEventList, int zipcode) throws NotBoundException {
		// Reorganize all List<Map> from nearest to farthest based on given zipcode. 
		
		// Check venue_address table, and call RapidAPI for filling out all zipcode information. 
		int inserted_row = storeZipcodeInfo(); 
		
		// Get zipcode info for given zipcode
		Map<String, Object> mapedGivenZipcode = callRapidGetZipCodeInfo(String.valueOf(zipcode)); 
		// mapedGivenZipcode can be null, like 10000, it is not a valid zipcode
		if (mapedGivenZipcode == null) {
			throw new NotBoundException(); 
		}
		double givenLat = (double)mapedGivenZipcode.get("lat"); 
		double givenLng = (double)mapedGivenZipcode.get("lng"); 
	
		
		for (Map eachEvent: inputEventList) {
			// Get distance between the coordinates of given zipcode and the coordinates of each event
			Double currLat = (Double)eachEvent.get("latitude"); 
			Double currLng = (Double)eachEvent.get("longitude"); 
			if (currLat != null && currLng != null && currLat != 0.0 && currLng != 0.0) {
				double distance = getDistance(givenLat, givenLng, currLat, currLng);
				// Add distance info to current event's map
				eachEvent.put("distance_between", distance); 
			}
		}
		
		// Filter all events that are within 50 miles
		List<Map> resultList = new ArrayList<>(); 
		for (Map eachEvent: inputEventList) {
			Object currDis = eachEvent.get("distance_between"); 
			if (currDis != null && (double)currDis < 50.0) {
				resultList.add(eachEvent); 
			}
		}
		
		// If no event in result list, then print out all events 
		if (resultList.isEmpty()) {
			Collections.sort(inputEventList, new SortByDistance());
			return inputEventList; 
		} else {
			Collections.sort(resultList, new SortByDistance()); 
			return resultList;
		}
	}

	private double getDistance(double givenLat, double givenLng, double currLat, double currLng) {
		// Get distance between two coordinates (latitude, longitude)
		givenLng = Math.toRadians(givenLng); 
        currLng = Math.toRadians(currLng); 
        givenLat = Math.toRadians(givenLat); 
        currLat = Math.toRadians(currLat); 
  
        // Haversine formula  
        double dlon = currLng - givenLng;  
        double dlat = currLat - givenLat; 
        double a = Math.pow(Math.sin(dlat / 2), 2) 
                 + Math.cos(givenLat) * Math.cos(currLat) 
                 * Math.pow(Math.sin(dlon / 2),2); 
              
        double c = 2 * Math.asin(Math.sqrt(a)); 
  
        // Radius of earth in kilometers. Use 3956 for miles, 6371 for kilometers. 
        double r = 3956; 
  
        // calculate the result 
        return(c * r); 
	}
	
	private static final String FILL_ZIPCODE_INFO = "UPDATE Venue_Address SET Latitude =:lat,Longitude =:lng WHERE Zipcode =:zip_code";
	/* 
	 *  	Store zipcodes information in Venue_Address table after calling public API. 
	 */
	private int storeZipcodeInfo() {
		// Get all venue_address's zipcodes, call public APIs get latitude and longitude, then store in 
		// venue_address table 
		List<EventTable> allVenue = jdbcTemplate.query(GET_ALL_VENUE, new EventRowmapper());
		
		int count_affected_row = 0; 
		
		for (EventTable eachVenue: allVenue) {
			if (eachVenue.getZipcode() != null && eachVenue.getZipcode() != 0) {
				if (eachVenue.getLatitude() == 0.0 || eachVenue.getLongitude() == 0.0) {
					
					// Don't have zipcode information, so need to call public API to get current zip code info 
					Map<String, Object> zipInfo = callRapidGetZipCodeInfo(eachVenue.getZipcode().toString());
					
					// Store zipcode info into database
					SqlParameterSource pramSource = new MapSqlParameterSource(zipInfo);
					count_affected_row += namedParameterJdbcTemplate.update(FILL_ZIPCODE_INFO, pramSource);
				}
			}
		}
		return count_affected_row;
	}
	
	/*
	 * 		For a given zipcode, call API to get latitude and zipcode information. 
	 */
	private final ObjectMapper mapper = new ObjectMapper(); 
	
	private Map<String, Object> callRapidGetZipCodeInfo(String zipcode) {
		Map<String, Object> zipInfo = new HashMap<>(); 
		
		StringBuilder URLlink = new StringBuilder("https://redline-redline-zipcode.p.rapidapi.com/rest/multi-info.json/"); 
		URLlink.append(zipcode); 
		URLlink.append("/degrees"); 
		
		try {
			HttpResponse<String> response = Unirest.get(URLlink.toString())
					.header("x-rapidapi-host", HOST_NAME)
					.header("x-rapidapi-key", ACCESS_KEY)
					.asString();
			
			try {
				// Parse from JSON to Map<String, Object>
				zipInfo = mapper.readValue(response.getBody(), new TypeReference<HashMap<String,Object>>(){});
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} 
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return (Map<String, Object>) zipInfo.get(zipcode); 
	}
	
	/*
	 * 		Check if a string is a valid zipcode and get zipcode in int type
	 */
	private Integer getZipcode(String str) {
		// Check if str is a 5-digit zipcode. 
		if (str != null && str.length() != 5) {
			return null; 
		}
		
		int zipcode = 0; 
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < '0' || str.charAt(i) > '9') {
				return null; 
			}
			zipcode = zipcode * 10 + (str.charAt(i) - '0'); 
		}
		return zipcode; 
	}

	/*
	 * 	----------------------------------------------------------------------------------------------------
	 * 				Get all the event related information and store them in List<EventTable>
	 *  ----------------------------------------------------------------------------------------------------
	 */
	@Override
	public List<EventTable> getAllEvents() {
		// Get all related event information
		return jdbcTemplate.query(GET_ALL_EVENT, new EventRowmapper()); 
	}
	
	/* 
	 * ---------------------------------- Event POST ----------------------------------
	 * 
	 */
	
	@Override
	public int createEvent (EventTable eventTable) {
		// Create an event
		int affectedRow;
		Map<String, Object> param = eventMap(eventTable, Integer.MIN_VALUE);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_EVENT, pramSource);
		
		return affectedRow; 
	}
	
	/* 
	 * ---------------------------------- Event UPDATE ----------------------------------
	 * 
	 */
	
	@Override
	public int updateEvent(EventTable eventTable) {
		// Update an event by matching event_id and organizer_id
		int affectedRow;
		Map<String, Object> param = eventMap(eventTable, eventTable.getEvent_id());
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		
		// Decide which part we need to update
		StringBuilder UPDATE_EVENT_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("organizer_id") && !key.equals("event_id"))
			{
				UPDATE_EVENT_INFO.append(key + "=:" + key + ",");
			}
		}
		// remove the last comma
		UPDATE_EVENT_INFO = UPDATE_EVENT_INFO.deleteCharAt(UPDATE_EVENT_INFO.length() - 1); 
		
		String UPDATE_EVENT = UPDATE_EVENT_INFO_PREFIX + UPDATE_EVENT_INFO + UPDATE_EVENT_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_EVENT, pramSource);
		
		return affectedRow;

	}
	
	/* 
	 * ---------------------------------- Event DELETE ----------------------------------
	 * 
	 */
	@Override
	public int deleteEvent(int event_id) {
		// Delete an event by event_id
		List<EventTable> et = getEventByID(event_id); 
		
		if (et == null || et.size() == 0) {
			// Didn't find this event; 
			return Integer.MIN_VALUE; 
		} else {
			int affectedRow = jdbcTemplate.update(DELETE_EVENT, event_id);
			return affectedRow; 
		}
	}
	
	/* 
	 * ---------------------------------- Event mapping ----------------------------------
	 * 
	 */
	private Map<String, Object> eventMap(EventTable eventTable, Integer event_id) {
		// Mapping event's information query's variable to URL POST body
		Map<String, Object>param = new HashMap<>();
		
		if (event_id != null && event_id != Integer.MIN_VALUE) {
			// Means we need to update event 
			param.put("event_id", event_id); 
		}
		
		param.put("event_status_code", eventTable.getEvent_status_code());
		
		param.put("event_type_code", eventTable.getEvent_type_code());
		
		param.put("commercial_type", eventTable.getCommercial_type() == null ? 
				null : eventTable.getCommercial_type());
		
		param.put("organizer_id", eventTable.getOrganizer_id());		// Cannot be null
		
		param.put("event_name", eventTable.getEvent_name() == null || eventTable.getEvent_name().isEmpty() ? 
				null : eventTable.getEvent_name());
		
		param.put("event_start_time", eventTable.getEvent_start_time() == null ? 
				null : eventTable.getEvent_start_time());
		
		param.put("event_end_time", eventTable.getEvent_end_time() == null ? 
				null : eventTable.getEvent_end_time());
		
		param.put("timezone", eventTable.getTimezone() == null || eventTable.getTimezone().length() == 0 ?
				"UTC" : eventTable.getTimezone()); 
		
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
				
		param.put("venue_id", eventTable.getVenue_id());			// Cannot be null 
		
		param.put("venue_name", eventTable.getVenue_name() == null || eventTable.getVenue_name().isEmpty() ? 
				null :eventTable.getVenue_name());
		
		param.put("other_details", eventTable.getOther_details() == null || eventTable.getOther_details().isEmpty() ? 
				null :eventTable.getOther_details());
		
		param.put("street1", eventTable.getStreet1() == null || eventTable.getStreet1().isEmpty() ? 
				null :eventTable.getStreet1());
		
		param.put("street2", eventTable.getStreet2() == null || eventTable.getStreet2().isEmpty() ? 
				null :eventTable.getStreet2());
		
		param.put("city", eventTable.getCity() == null || eventTable.getCity().isEmpty() ? 
				null :eventTable.getCity());
		
		param.put("state", eventTable.getState() == null || eventTable.getState().isEmpty() ? 
				null :eventTable.getState());
		
		param.put("zipcode", eventTable.getZipcode() == null ? 
				null :eventTable.getZipcode());
		
		param.put("latitude", eventTable.getLatitude() == null ? 
				null : eventTable.getLatitude());
		
		param.put("longitude", eventTable.getLongitude() == null ? 
				null : eventTable.getLongitude()); 
		
		param.put("event_type_description", eventTable.getEvent_type_description() == null || eventTable.getEvent_type_description().isEmpty() ? 
				null :eventTable.getEvent_type_description());
		
		param.put("event_status_description", eventTable.getEvent_status_description() == null || eventTable.getEvent_status_description().isEmpty() ? 
				null :eventTable.getEvent_status_description());
		
		param.put("organizer_name", eventTable.getOrganizer_name() == null || eventTable.getOrganizer_name().isEmpty() ? 
				null :eventTable.getOrganizer_name());
		
		return param;
	}
	
}


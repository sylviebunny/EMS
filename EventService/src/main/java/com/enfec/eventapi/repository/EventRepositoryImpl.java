package com.enfec.eventapi.repository;

import com.enfec.eventapi.model.EventComparatorByDistance;
import com.enfec.eventapi.model.EventComparatorByStartTime;
import com.enfec.eventapi.model.EventRowmapper;
import com.enfec.eventapi.model.EventTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.rmi.NotBoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event repository implementation class
 * @author heidi huo
 *
 */
@Component
@Transactional
public class EventRepositoryImpl implements EventRepository {

    /**
     * {@value #REGISTER_EVENT} Query for event registration 
     */
	private static final String REGISTER_EVENT = "INSERT INTO Events(Event_Status_Code, Event_Type_Code, Commercial_Type, Organizer_ID, Venue_ID, "
			+ "Event_Name, Event_Start_Time, Event_End_Time, Timezone, Number_of_Participants, Derived_Days_Duration, Event_Cost, Discount, Comments) VALUES "
			+ "(:event_status_code, :event_type_code, :commercial_type, :organizer_id, "
			+ ":venue_id, :event_name, :event_start_time, :event_end_time, :timezone, :number_of_participants, :derived_days_duration, :event_cost, :discount, :comments)";
	
    /**
     * {@value #UPDATE_EVENT_INFO} Query prefix and suffix for updating an event
     */
	private static final String UPDATE_EVENT_INFO_PREFIX = "UPDATE Events SET "; 
	private static final String UPDATE_EVENT_INFO_SUFFIX = " WHERE Event_ID = :event_id AND Organizer_ID =:organizer_id";
	
	/**
	 * {@value #DELETE_EVENT} Query for deleting event
	 */
	private static final String DELETE_EVENT = "DELETE FROM Events WHERE Event_ID =?";

	/**
	 * {@value #GET_ALL_EVENT} Query for collecting all event information 
	 */
	private static final String GET_ALL_EVENT = "SELECT Event_ID, Events.Event_Status_Code, Events.Event_Type_Code, Commercial_Type, Events.Organizer_ID, Events.Venue_ID, Event_Name, Event_Start_Time, Event_End_Time, Timezone, Number_of_Participants, Derived_Days_Duration, Event_Cost, Discount, Comments, Venue_Name, Venues.Other_Details, Street1, Street2, City, State, Zipcode, Latitude, Longitude, Event_Status_Description, Event_Type_Description, Organizer_Name\n" + 
			"FROM Events, Venues, Venue_Address, Ref_Event_Status, Ref_Event_Types, Organizers\n" + 
			"WHERE Events.Venue_ID = Venues.Venue_ID AND Events.Venue_ID = Venue_Address.Venue_ID AND Events.Event_Status_Code = Ref_Event_Status.Event_Status_Code AND Events.Event_Type_Code = Ref_Event_Types.Event_Type_Code AND Organizers.Organizer_ID = Events.Organizer_ID; ";

	/**
	 * {@value #GET_ALL_VENUE} Query for collecting all venue address information 
	 */
	private static final String GET_ALL_VENUE = "SELECT * FROM Venue_Address";
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate; 
	
	/**
	 * Use filter bar, anyone can type anything, like WA/seattle/non-profitable/festival/98002. 
	 * Should not match str with ID or code information. 
	 * {@inheritDoc}
	 * @throws NotBoundException when input zipcode is not a valid zipcode
	 */
	@Override
	public List<Map> getFilteredEventsByRefinedZipcode(List<EventTable> allEvent, String str) throws NotBoundException {
	
		List<Map> allEventMap = new ArrayList<>();    // Convert EventTable to Map
		for (EventTable et: allEvent) {
			allEventMap.add(eventMap(et, et.getEvent_id())); 
		} 
		
		if (str == null || str.isEmpty()) {   // For empty filtered bar, print out allEvents
		    Collections.sort(allEventMap, new EventComparatorByStartTime()); 
			return allEventMap; 
		}
		
		Integer zipcode = getZipcode(str);    
		if (zipcode != null) {           
			try { 
				return getEventByZipcode(allEventMap, zipcode); 
			} catch (NotBoundException nbe) {
				throw nbe;          // This is not a valid zipcode on file 
			}
		}
		
		List<Map> afterFilter = new ArrayList<>(); 
		
		final HashSet<String> ignoreKeys = getIgnoreKeys(); 
		
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
		Collections.sort(afterFilter, new EventComparatorByStartTime());      // Sort events based on start time
		return afterFilter; 
	}
	
	/**
	 * Keys that don't need to search 
	 * @return A set of key name
	 */
	private HashSet<String> getIgnoreKeys() {
		
		HashSet<String> ignoreKeys = new HashSet<>(); 
		
		ignoreKeys.add("event_type_code"); 
		ignoreKeys.add("event_id"); 
		ignoreKeys.add("event_status_code"); 
		ignoreKeys.add("organizer_id"); 
		ignoreKeys.add("venue_id"); 
		
		return ignoreKeys; 
	}
	
	/**
	 * {@inheritDoc}
	 * <pre>start_date - start date, inclusive</pre>
	 * <pre>end_date - end date, inclusive</pre>
	 */
	@Override
	public List<Map> getFilteredEvents(List<EventTable> allEvent, Timestamp start_date, Timestamp end_date) {
		List<Map> allEventMap = new ArrayList<>(); 
		for (EventTable et: allEvent) {
			allEventMap.add(eventMap(et, et.getEvent_id())); 
		} 
		
		List<Map> dateRangeEvents = new ArrayList<>();
		for (Map eachEvent: allEventMap) {
			Timestamp eventStartTime = Timestamp.valueOf((String)eachEvent.get("event_start_time")); 
			if (eventStartTime.after(start_date) && eventStartTime.before(end_date) || 
			        eventStartTime.equals(start_date) || eventStartTime.equals(end_date)) {
				dateRangeEvents.add(eachEvent); 
			}
		}
		
		Collections.sort(dateRangeEvents, new EventComparatorByStartTime());  // Sort based on start time
		return dateRangeEvents; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Map> getEventByEventType(List<Map> inputEvents, String event_type) {
		if (event_type == null || event_type.length() == 0) {
			return inputEvents; 
		}
		
		List<Map> resultEvents = new ArrayList<>();
		
		for (Map eachEvent: inputEvents) {
			String currType = eachEvent.get("event_type_description").toString(); 
			if (currType.toLowerCase().contains(event_type.toLowerCase())) {
				resultEvents.add(eachEvent); 
			}
		}
		return resultEvents; 
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public List<EventTable> getEventByID(int event_id) {
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
	
	/**
	 * Based on given zipcode, get all events within distance (50 miles)
	 * <p>Reorganize all List<Map> from nearest to farthest based on given zipcode.</p>
	 * @param inputEventList
	 * @param zipcode
	 * @return a list of events that match such criteria
	 * @throws NotBoundException if input zipcode is not a valid zipcode
	 */
	public List<Map> getEventByZipcode(List<Map> inputEventList, int zipcode) throws NotBoundException {
	 
		int inserted_row = storeZipcodeInfo();    // Update venue_address for collecting specific location for each event's zipcode   
		
		Map<String, Object> mapedGivenZipcode = callRapidGetZipCodeInfo(String.valueOf(zipcode)); 
		if (mapedGivenZipcode == null) {      // Input zipcode is not a valid zipcode stored in database
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
				eachEvent.put("distance_between", distance);    // Put distance into result list
			}
		}
		
		// Filter all events that are within 50 miles
		List<Map> resultList = new ArrayList<>(); 
		for (Map eachEvent: inputEventList) {
			Object currDis = eachEvent.get("distance_between"); 
			if (currDis != null && (double)currDis <= 50.0) {
				resultList.add(eachEvent); 
			}
		}
		
		if (resultList.isEmpty()) {
		    Collections.sort(inputEventList, new EventComparatorByDistance()); // If no event in result list, then print out all events 
			return inputEventList; 
		} else {
			Collections.sort(resultList, new EventComparatorByDistance()); 
			return resultList;
		}
	}

	/**
	 * Calculate distance between two geographical coordinates
	 * @param coordinate1's latitude
	 * @param coordinate1's longitude
	 * @param coordinate2's latitude
	 * @param coordinate2's longitude
	 * @return distance between two coordinates in mile 
	 */
	private double getDistance(double givenLat, double givenLng, double currLat, double currLng) {
		givenLng = Math.toRadians(givenLng); 
        currLng = Math.toRadians(currLng); 
        givenLat = Math.toRadians(givenLat); 
        currLat = Math.toRadians(currLat); 
  
        double dlon = currLng - givenLng;  
        double dlat = currLat - givenLat; 
        double a = Math.pow(Math.sin(dlat / 2), 2) 
                 + Math.cos(givenLat) * Math.cos(currLat) 
                 * Math.pow(Math.sin(dlon / 2),2); 
              
        double c = 2 * Math.asin(Math.sqrt(a)); 
  
        // Radius of earth in kilometers. Use 3956 for miles, 6371 for kilometers. 
        double r = 3956; 

        return(c * r); 
	}
	
	/**
	 * {@value #FILL_ZIPCODE_INFO} Query for filling out coordinates information in venue_address table
	 */
	private static final String FILL_ZIPCODE_INFO = "UPDATE Venue_Address SET Latitude =:lat,Longitude =:lng WHERE Zipcode =:zip_code";
	
	/**
	 * Get zipcode coordinates information with calling public API and store them in database
	 * @return affected row
	 */
	private int storeZipcodeInfo() {
		List<EventTable> allVenue = jdbcTemplate.query(GET_ALL_VENUE, new EventRowmapper());
		
		int count_affected_row = 0; 
		
		for (EventTable eachVenue: allVenue) {
			if (eachVenue.getZipcode() != null && eachVenue.getZipcode() != 0) {
				if (eachVenue.getLatitude() == 0.0 || eachVenue.getLongitude() == 0.0) {
					 
					Map<String, Object> zipInfo = callRapidGetZipCodeInfo(eachVenue.getZipcode().toString());
					
					SqlParameterSource pramSource = new MapSqlParameterSource(zipInfo);
					count_affected_row += namedParameterJdbcTemplate.update(FILL_ZIPCODE_INFO, pramSource);
				}
			}
		}
		return count_affected_row;
	}
	
	private final ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * Private token information for calling third party API
	 */
    private final String HOST_NAME = ""; 
    private final String ACCESS_KEY = ""; 
	
	/**
	 * Call third API to get zipcode information and convert JSON body to map
	 * @param zipcode
	 * @return mapped zipcode information 
	 */
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
	
	/**
	 * Check if a string is a valid zipcode and convert it to int type
	 * @param str
	 * @return zipcode in int type or null if input is not a valid 5-digit zipcode
	 */
	private Integer getZipcode(String str) {
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

	/**
	 * {@inheritDoc}
	 * with including completed event location information 
	 */
	@Override
	public List<EventTable> getAllEvents() {
		return jdbcTemplate.query(GET_ALL_EVENT, new EventRowmapper()); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createEvent (EventTable eventTable) {
		int affectedRow;
		Map<String, Object> param = eventMap(eventTable, Integer.MIN_VALUE);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_EVENT, pramSource);
		
		return affectedRow; 
	}
	
	/**
	 * Update an event by matching event_id and organizer_id
	 * {@inheritDoc}
	 */
	@Override
	public int updateEvent(EventTable eventTable) {
		int affectedRow;
		Map<String, Object> param = eventMap(eventTable, eventTable.getEvent_id());
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		
		StringBuilder UPDATE_EVENT_INFO = new StringBuilder();            // Decide which part we need to update
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("organizer_id") && !key.equals("event_id")) {
				UPDATE_EVENT_INFO.append(key + "=:" + key + ",");
			}
		}
		
		UPDATE_EVENT_INFO = UPDATE_EVENT_INFO.deleteCharAt(UPDATE_EVENT_INFO.length() - 1);       // remove the last comma
		
		String UPDATE_EVENT = UPDATE_EVENT_INFO_PREFIX + UPDATE_EVENT_INFO + UPDATE_EVENT_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_EVENT, pramSource);
		
		return affectedRow;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteEvent(int event_id) {
		List<EventTable> et = getEventByID(event_id); // Check if event exists in database
		
		if (et == null || et.size() == 0) {   // Given event id is not in database
			return Integer.MIN_VALUE; 
		} else {
			int affectedRow = jdbcTemplate.update(DELETE_EVENT, event_id);
			return affectedRow; 
		}
	}
	
	/**
	 * Map event's information between URL body information and database variable attributes
	 * @param eventTable
	 * @param event_id
	 * @return Map contains variable and it's corresponding information 
	 */
	private Map<String, Object> eventMap(EventTable eventTable, Integer event_id) {
		Map<String, Object>param = new HashMap<>();
		
		if (event_id != null && event_id != Integer.MIN_VALUE) { // Means we need to update event 
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


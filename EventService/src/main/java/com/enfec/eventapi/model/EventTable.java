package com.enfec.eventapi.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/************************************************
*
* Author: Heidi Huo
* Assignment: Event information
* Class: EventTable
*
************************************************/
@Data
@Component
@Getter
@Setter
public class EventTable {

    /**
     * Event ID
     */
	private int event_id;
	
	/**
	 * Event status code
	 */
	private int event_status_code;
	
	/**
	 * Event type code
	 */
	private int event_type_code;
	
	/**
	 * Commercial type, 
	 */
	private String commercial_type;

	/**
	 * Organizer ID
	 */
	private int organizer_id;

	/**
	 * Event name
	 */
	private String event_name;

	/**
	 * Start time of the event
	 */
	private String event_start_time;
	
	/**
	 * End time of the event
	 */
	private String event_end_time;
	
	/**
	 * Time zone
	 */
	private String timezone;

	/**
	 * Number of participants
	 */
	private Integer number_of_participants;
	
	/**
	 * Derived days duration
	 */
	private String derived_days_duration;
	
	/**
	 * General event cost
	 */
	private String event_cost;
	
	/**
	 * Discount rate
	 */
	private Double discount;
	
	/**
	 * Event comments
	 */
	private String comments;

	/**
	 * Venue ID
	 */
	private int venue_id;
	
	/**
	 * Venue name
	 */
	private String venue_name;
	
	/**
	 * Other details
	 */
	private String other_details;
	
	/**
	 * street name 1
	 */
	private String street1;
	
	/**
	 * street name 2
	 */
	private String street2;
	
	/**
	 * City of the event
	 */
	private String city;
	
	/**
	 * State of the event
	 */
	private String state;
	
	/**
	 * Zip code of the event
	 */
	private Integer zipcode;
	
	/**
	 * Latitude of the event's zip code
	 */
	private Double latitude;
	
	/**
	 * Longitude of the event's zip code
	 */
	private Double longitude;

	/**
	 * Event status description
	 */
	private String event_status_description;

	/**
	 * Event type description
	 */
	private String event_type_description;

	/**
	 * Organizer name
	 */
	private String organizer_name;

	/** 
	 * Event Photo URL
	 */
	private String event_photo_url; 
}

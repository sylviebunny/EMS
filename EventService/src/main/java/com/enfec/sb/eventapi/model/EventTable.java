package com.enfec.sb.eventapi.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Component
@Getter @Setter
public class EventTable {
	
	private int event_id; 
	private int event_status_code; 
	private int event_type_code; 
	private String commercial_type; 			// Profitable/Non-profitable/Free...
	
	private int organizer_id; 
	private String event_name; 
	
	private Timestamp event_start_time;
	private Timestamp event_end_time; 
	
	private Integer number_of_participants; 
	private String derived_days_duration; 
	private Double event_cost; 
	private Double discount; 
	private String comments; 
	
	// Venue and address information 
	private int venue_id;
	private String venue_name; 
	private String other_details; 
	private String street1; 
	private String street2; 
	private String city; 
	private String state; 
	private Integer zipcode; 
	
	// Event Status
	private String event_status_description; 
	
	// Event Type
	private String event_type_description; 
	
	// Organizer Info
	private String organizer_name; 
	
}

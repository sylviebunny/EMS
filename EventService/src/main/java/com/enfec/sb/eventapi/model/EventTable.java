package com.enfec.sb.eventapi.model;

import java.sql.Timestamp;
import java.util.Date;

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
	private String event_status_code; 
	private String event_type_code; 
	private String commercial_type; 			// Profitable/Non-profitable/Free...
	
	private int organizer_id; 
	private int venue_id;
	private String event_name; 
	
	private Timestamp event_start_time;
	private Timestamp event_end_time; 
	
	private Integer number_of_participants; 
	private String derived_days_duration; 
	private Double event_cost; 
	private Double discount; 
	private String comments; 
	
}

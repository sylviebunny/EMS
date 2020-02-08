package com.enfec.sb.eventapi.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.stereotype.Component;

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
	private boolean free_or_commercial_code; 			// 0: free; 1: commercial
	private int organizer_id; 
	private int venue_id;
	private String event_name; 
	private Timestamp event_start_date;
	private Timestamp event_end_date; 
	private int number_of_participants; 
	private String derived_days_duration; 
	private double event_cost; 
	private double discount; 
	private String comments; 
	
}

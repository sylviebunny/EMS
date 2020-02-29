package com.enfec.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Room information to map 'Rooms' and 'Space_Requests' tables in database
* Class: Room
* 
************************************************/
@Data
@Getter
@Setter
@Component
public class Room {
	
	private int room_id; 
	private int venue_id; 
	private String room_name; 
	private int room_capability;
	private double rate_for_day; 
	private String other_details;
	
	private int space_request_id; 
	private int event_id;
	private String booking_status; 
	private String occupancy; 
	private String commercial_or_free; 
	private Timestamp occupancy_date_from;
	private Timestamp occupancy_date_to;
}

package com.enfec.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Venue information to map 'Venue' and 'Venue_Address' tables in database
* Class: Venue
* 
************************************************/
@Data
@Getter
@Setter
@Component
public class Venue {
	
	private int venue_id;
	private String venue_name;
	private String other_details;
	
	private int venueAddress_id;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private int zipcode;
	private double latitude;
	private double longitude;
}

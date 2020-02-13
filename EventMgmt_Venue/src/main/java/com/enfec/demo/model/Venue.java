package com.enfec.demo.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Component
public class Venue {
	public int Venue_ID;
	public String Venue_Name;
	public String Other_Details;
	
	public int VenueAddress_ID;
	public String Street1;
	public String Street2;
	public String City;
	public String State;
	public int Zipcode;
}

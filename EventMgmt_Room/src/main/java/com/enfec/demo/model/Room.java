package com.enfec.demo.model;

import java.sql.Timestamp;

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
public class Room {
	public int Room_ID; 
	public int Venue_ID; 
	public String Room_Name; 
	public int Room_Capability;
	public double Rate_for_Day; 
	public String Other_Details;
	
	public int Space_Request_ID; 
	public int Event_ID;
	public String Booking_Status_Code; 
	public byte Occupancy; 
	public byte Commercial_or_Free; 
	public Timestamp Occupancy_Date_From;
	public Timestamp Occupancy_Date_To;
}

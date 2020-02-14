package com.enfec.sb.eventapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonFormat;


public class EventRowmapper implements RowMapper<EventTable> {


	
	
	@Override
	public EventTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		EventTable et = new EventTable();
		
		et.setEvent_id(rs.getInt("Event_ID"));
		et.setEvent_status_code(rs.getInt("Event_Status_Code"));
		et.setEvent_type_code(rs.getInt("Event_Type_Code"));
		et.setCommercial_type(rs.getString("Commercial_Type"));
		et.setOrganizer_id(rs.getInt("Organizer_ID"));
		et.setVenue_id(rs.getInt("Venue_ID"));
		et.setEvent_name(rs.getString("Event_Name"));
		
		et.setEvent_start_time(rs.getString("Event_Start_Time")); 
		et.setEvent_end_time(rs.getString("Event_End_Time"));
		et.setTimezone(rs.getString("Timezone"));
		
		et.setNumber_of_participants(rs.getInt("Number_of_Participants"));
		et.setDerived_days_duration(rs.getString("Derived_Days_Duration"));
		et.setEvent_cost(rs.getDouble("Event_Cost"));
		et.setDiscount(rs.getDouble("Discount"));
		et.setComments(rs.getString("Comments"));
		
		// Venue and Address Information
		et.setVenue_name(rs.getString("Venue_Name"));
		et.setOther_details(rs.getString("Other_Details"));
		et.setStreet1(rs.getString("Street1"));
		et.setStreet2(rs.getString("Street2"));
		et.setCity(rs.getString("City"));
		et.setState(rs.getString("State"));
		et.setZipcode(rs.getInt("Zipcode"));
		
		et.setEvent_type_description(rs.getString("Event_Type_Description"));
		et.setEvent_status_description(rs.getString("Event_status_description"));
		et.setOrganizer_name(rs.getString("Organizer_Name"));
		
		return et;
	}
	
	
	
}


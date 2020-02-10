package com.enfec.sb.refundapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;


public class RefundRowmapper implements RowMapper<RefundTable> {


	
	
	@Override
	public RefundTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		RefundTable et = new RefundTable();
		
		et.setEvent_id(rs.getInt("Event_ID"));
		et.setEvent_status_code(rs.getString("Event_Status_Code"));
		et.setEvent_type_code(rs.getString("Event_Type_Code"));
		et.setFree_or_commercial_code(rs.getBoolean("Free_or_Commercial_Code"));
		et.setOrganizer_id(rs.getInt("Organizer_ID"));
		et.setVenue_id(rs.getInt("Venue_ID"));
		et.setEvent_name(rs.getString("Event_Name"));
		et.setEvent_start_date(rs.getTimestamp("Event_Start_Date"));
		et.setEvent_end_date(rs.getTimestamp("Event_End_Date"));
		et.setNumber_of_participants(rs.getInt("Number_of_Participants"));
		et.setDerived_days_duration(rs.getString("Derived_Days_Duration"));
		et.setEvent_cost(rs.getDouble("Event_Cost"));
		et.setDiscount(rs.getDouble("Discount"));
		et.setComments(rs.getString("Comments"));
		
		return et;
	}
	
	
	
}


package com.enfec.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RoomRowmapper implements RowMapper<Room>{
	@Override
	public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
		Room room = new Room();
		room.setRoom_ID(rs.getInt("Room_ID"));
		room.setVenue_ID(rs.getInt("Venue_ID"));
		room.setRoom_Name((rs.getString("Room_Name")));
		room.setRoom_Capability(rs.getInt("Room_Capability"));
		room.setRate_for_Day(rs.getDouble("Rate_for_Day"));
		room.setOther_Details((rs.getString("Other_Details")));
		
		room.setSpace_Request_ID(rs.getInt("Space_Request_ID"));
		room.setEvent_ID(rs.getInt("Event_ID"));
		room.setBooking_Status_Code((rs.getString("Booking_Status_Code")));
		room.setOccupancy(rs.getByte("Occupancy"));
		room.setCommercial_or_Free((rs.getByte("Commercial_or_Free")));
		room.setOccupancy_Date_From(rs.getTimestamp("Occupancy_Date_From"));
		room.setOccupancy_Date_To(rs.getTimestamp("Occupancy_Date_To"));
		
		return room;
	}
}

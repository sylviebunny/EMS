package com.enfec.demo.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.enfec.demo.model.Room;

@Component
public class RoomRepositoryImpl implements RoomRepository{
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	
	@Override
	public int createRoom(Room room) {
		String CREATE_ROOM = "INSERT INTO Rooms(Venue_ID,Room_Name,Room_Capability,Rate_for_Day,Other_Details) VALUES(?,?,?,?,?)";
		String CREATE_ROOM1 = "INSERT INTO Space_Requests(Room_ID,Event_ID,Booking_Status_Code,Occupancy,Commercial_or_Free,Occupancy_Date_From,Occupancy_Date_To) VALUES(?,?,?,?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int count = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_ROOM, new String[]{"Room_ID"});
	                ps.setInt(1, room.getVenue_ID());
	                ps.setString(2, room.getRoom_Name());
	                ps.setInt(3, room.getRoom_Capability());
	                ps.setDouble(4, room.getRate_for_Day());
	                ps.setString(5, room.getOther_Details());
	                return ps;
	              }, keyHolder);
	    Number key = keyHolder.getKey();
	    //key is primary key
	    int count1 = jdbcTemplate.update(CREATE_ROOM1,key.longValue(),room.getEvent_ID(),room.getBooking_Status_Code(),room.getOccupancy(),room.getCommercial_or_Free(),room.getOccupancy_Date_From(),room.getOccupancy_Date_To());
	    return count;
	}
	
	
	@Override
	public Room getRoomInfo(int Room_ID) {
		String SELECT_ROOM = "select * from Rooms r join Space_Requests s on r.Room_ID = s.Room_ID where r.Room_ID = ?";
		Room room = jdbcTemplate.queryForObject(SELECT_ROOM, (rs,count) -> new Room(rs.getInt("Room_ID"),
				rs.getInt("Venue_ID"),
				rs.getString("Room_Name"),
				rs.getInt("Room_Capability"),
				rs.getDouble("Rate_for_Day"),
				rs.getString("Other_Details"),
				rs.getInt("Space_Request_ID"),
				rs.getInt("Event_ID"),
				rs.getString("Booking_Status_Code"),
				rs.getByte("Occupancy"),
				rs.getByte("Commercial_or_Free"),
				rs.getTimestamp("Occupancy_Date_From"),
				rs.getTimestamp("Occupancy_Date_To")),Room_ID);
		return room;
	}
	
	/*
	//Update Room with partial information
	
//		String UPDATE_SPACE_INFO_SUFFIX = " WHERE Room_ID = :Room_ID";
	@Override
	public int updateRoom(Room room) {
//		String UPDATE_ROOM = "UPDATE Rooms SET Venue_ID=?,Room_Name=?,Room_Capability=?,Rate_for_Day=?,Other_Details=? WHERE Room_ID =?";
//		String UPDATE_SPACE_REQUESTS = "UPDATE Space_Requests SET Organizer_Name=?,Email_Address=?,Other_details=? WHERE Room_ID =? AND Event_ID =?";
		String UPDATE_ROOM_INFO_PREFIX = "UPDATE Rooms SET "; 
		String UPDATE_ROOM_INFO_SUFFIX = " WHERE Room_ID = :Room_ID";
		int affectedRow;
		Map<String, Object> param = roomMap(room);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_ROOM_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("Room_ID"))
			{
				UPDATE_ROOM_INFO.append(key + "=:" + key + ",");
			}
		}
		// remove the last colon
		UPDATE_ROOM_INFO = UPDATE_ROOM_INFO.deleteCharAt(UPDATE_ROOM_INFO.length() - 1); 
		
		String UPDATE_ROOM = UPDATE_ROOM_INFO_PREFIX + UPDATE_ROOM_INFO + UPDATE_ROOM_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ROOM, pramSource);
		
		
		String UPDATE_SPACE_INFO_PREFIX = "UPDATE Space_Requests SET "; 
		int affectedRow1;
		Map<String, Object> param1 = spaceMap(room);
		
		SqlParameterSource pramSource1 = new MapSqlParameterSource(param1);
		StringBuilder UPDATE_SPACE_INFO = new StringBuilder();
		for (String key : param1.keySet()) {
			if (param1.get(key) != null && !key.equals("Room_ID"))
			{
				UPDATE_SPACE_INFO.append(key + "=:" + key + ",");
			}
		}
		// remove the last colon
		UPDATE_SPACE_INFO = UPDATE_SPACE_INFO.deleteCharAt(UPDATE_SPACE_INFO.length() - 1); 
		
		String UPDATE_SPACE = UPDATE_SPACE_INFO_PREFIX + UPDATE_ROOM_INFO + UPDATE_ROOM_INFO_SUFFIX;
		affectedRow1 = namedParameterJdbcTemplate.update(UPDATE_SPACE, pramSource1);
		return affectedRow;
	}
	*/
	
	@Override
	public int deleteRoom(int Room_ID) {
		String DELETE_ROOM = "DELETE FROM Rooms WHERE Room_ID = ?";
		String DELETEfromSPACE_REQUESTS = "DELETE FROM Space_Requests WHERE Room_ID = ?";
		String DELETEfromSEATS = "DELETE FROM Seats WHERE Room_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_ROOM, Room_ID);
		int affectedRow1 = jdbcTemplate.update(DELETEfromSPACE_REQUESTS, Room_ID);
		int affectedRow2 = jdbcTemplate.update(DELETEfromSEATS, Room_ID);
		
		return affectedRow;
	}
	/*
	//For update with partial part
		private Map<String, Object> roomMap(Room room) {
			// Mapping room's information query's variable to URL POST body
			Map<String, Object>param = new HashMap<>();
		
			if (room.getRoom_ID() != 0) {
				param.put("Room_ID", room.getRoom_ID());
			} else {
				throw new NullPointerException("Room_ID cannot be null");
			}
			param.put("Venue_ID", room.getVenue_ID() == 0 ? null:room.getVenue_ID());
			param.put("Room_Name", room.getRoom_Name() == null || room.getRoom_Name().isEmpty() ? null:room.getRoom_Name());
			param.put("Room_Capability", room.getRoom_Capability() == 0 ? null:room.getRoom_Capability());		
			param.put("Rate_for_Day", room.getRate_for_Day() == 0 ? null:room.getRate_for_Day());
			param.put("Other_Details", room.getOther_Details() == null || room.getOther_Details().isEmpty() ? null:room.getOther_Details());

			return param;
		}
		
		private Map<String, Object> spaceMap(Room room) {
			// Mapping space request's information query's variable to URL POST body
			Map<String, Object>param = new HashMap<>();
		
			if (room.getRoom_ID() != 0) {
				param.put("Room_ID", room.getRoom_ID());
			} else {
				throw new NullPointerException("Room_ID cannot be null");
			}
			
			param.put("Space_Request_ID", room.getSpace_Request_ID() == 0 ? null:room.getSpace_Request_ID());
			param.put("Event_ID", room.getEvent_ID() == 0 ? null:room.getEvent_ID()); 
			param.put("Booking_Status_Code", room.getBooking_Status_Code() == null || room.getBooking_Status_Code().isEmpty() ? null:room.getBooking_Status_Code());
			param.put("Occupancy", room.getOccupancy() == 0 ? null:room.getOccupancy());
			param.put("Commercial_or_Free", room.getCommercial_or_Free() == 0 ? null:room.getCommercial_or_Free());
			param.put("Occupancy_Date_From", room.getOccupancy_Date_From() == null ? null:room.getOccupancy_Date_From());
			param.put("Occupancy_Date_To", room.getOccupancy_Date_To() == null ? null:room.getOccupancy_Date_To());

			return param;
		}
		*/
}

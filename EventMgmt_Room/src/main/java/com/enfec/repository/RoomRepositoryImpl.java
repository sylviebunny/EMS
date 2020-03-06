package com.enfec.repository;


import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.model.Room;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Implement CRUD methods for room
* Class: RoomRepositoryImpl
*
************************************************/
@Component
@Transactional
public class RoomRepositoryImpl implements RoomRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(RoomRepositoryImpl.class);
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
     * Create room information
     * Map room table to MySql parameters and insert into 'Rooms' and 'Space_Requests' table in database
     * 
     * @param room: The information that needs to be created; Date format: "Mon, 10 Feb 2020 10:30:00 PST"
     * @return number of affected rows
     */
	@Override
	public int createRoom(Room room) {
		String CREATE_ROOM = "INSERT INTO Rooms(Venue_ID,Room_Name,Room_Capability,Rate_for_Day,"
				+ "Other_Details) VALUES(?,?,?,?,?)";
		String CREATE_ROOM1 = "INSERT INTO Space_Requests(Room_ID,Event_ID,Booking_Status,Occupancy,"
				+ "Commercial_or_Free,Occupancy_Date_From,Occupancy_Date_To) VALUES(?,?,?,?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int affectedRow = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_ROOM, new String[]{"Room_ID"});
	                ps.setInt(1, room.getVenue_id());
	                ps.setString(2, room.getRoom_name());
	                ps.setInt(3, room.getRoom_capability());
	                ps.setDouble(4, room.getRate_for_day());
	                ps.setString(5, room.getOther_details());
	                return ps;
	              }, keyHolder);
	    Number primaryKey = keyHolder.getKey();
	    jdbcTemplate.update(CREATE_ROOM1, primaryKey.longValue(),room.getEvent_id(),room.getBooking_status(),
	    		room.getOccupancy(),room.getCommercial_or_free(),room.getOccupancy_date_from(),room.getOccupancy_date_to());
	    return affectedRow;
	}
	
	/**
     * Get room information from 'Rooms' and 'Space_Requests' tables in database by room id
     * 
     * @param Room_ID
     * @return Room: specific room information that match the request
     */
	@Override
	public Room getRoomInfo(int Room_ID) {
		String SELECT_ROOM = "select * from Rooms r join Space_Requests s on r.Room_ID = s.Room_ID where r.Room_ID = ?";
		Room room;
		try {
			room = jdbcTemplate.queryForObject(SELECT_ROOM, new Object[] { Room_ID }, 
					new BeanPropertyRowMapper<Room>(Room.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return room;
	}
	
	/**
     * Get all rooms' information from 'Rooms' and 'Space_Requests' tables in database
     * @return List<Room>: all entries that match the request
     */
	@Override
	public List<Room> getAllRoomInfo() {
		String SELECT_ALL_ROOM = "select * from Rooms r join Space_Requests s on r.Room_ID = s.Room_ID";
		return jdbcTemplate.query(SELECT_ALL_ROOM, new BeanPropertyRowMapper<Room>(Room.class));
	}
	
	/**
     * Update room information
     * Map room table to MySql parameters, update 'Rooms' and 'Space_Requests' tables in database
     * 
     * @param room: The information that needs to be updated. 
     * @return number of affected rows
     */
	@Override
	public int updateRoom(Room room) {
		String UPDATE_ROOM_INFO_PREFIX = "UPDATE Rooms SET "; 
		String UPDATE_ROOM_INFO_SUFFIX = " WHERE Room_ID = :room_id";
		
		Map<String, Object> param = roomMap(room);		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating 'Rooms' table Info: {}", pramSource);
		
		StringBuilder UPDATE_ROOM_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("room_id"))  {
				UPDATE_ROOM_INFO.append(key + "=:" + key + ",");
			}
		}
		UPDATE_ROOM_INFO = UPDATE_ROOM_INFO.deleteCharAt(UPDATE_ROOM_INFO.length() - 1); 	
		String UPDATE_ROOM = UPDATE_ROOM_INFO_PREFIX + UPDATE_ROOM_INFO + UPDATE_ROOM_INFO_SUFFIX;
		int affectedRow = namedParameterJdbcTemplate.update(UPDATE_ROOM, pramSource);
		updateSpaceRequests(room);
		return affectedRow;
	}
	
	/**
     * Update room's Space Requests information
     * Map room table to MySql parameters, update 'Space_Requests' table in database
     * 
     * @param room: The information that needs to be updated.
     */
	private void updateSpaceRequests(Room room) {
		String UPDATE_SPACE_INFO_PREFIX = "UPDATE Space_Requests SET ";
		String UPDATE_SPACE_INFO_SUFFIX = " WHERE Room_ID = :room_id";
		Map<String, Object> param1 = spaceMap(room);
		SqlParameterSource pramSource1 = new MapSqlParameterSource(param1);
		logger.info("Updating 'Space_Requests' table Info: {}", pramSource1);
		
		StringBuilder UPDATE_SPACE_INFO = new StringBuilder();
		for (String key : param1.keySet()) {
			if (param1.get(key) != null && !key.equals("room_id")) {
				UPDATE_SPACE_INFO.append(key + "=:" + key + ",");
			}
		}
		UPDATE_SPACE_INFO = UPDATE_SPACE_INFO.deleteCharAt(UPDATE_SPACE_INFO.length() - 1);
		String UPDATE_SPACE = UPDATE_SPACE_INFO_PREFIX + UPDATE_SPACE_INFO + UPDATE_SPACE_INFO_SUFFIX;
		namedParameterJdbcTemplate.update(UPDATE_SPACE, pramSource1);
	}
	
	/**
     * Delete room information from 'Room', 'Space_Requests', 'Seats' tables in database by room id
     * 
     * @param Room_ID
     * @return number of affected rows
     */
	@Override
	public int deleteRoom(int Room_ID) {
		String DELETE_ROOM = "DELETE FROM Rooms WHERE Room_ID = ?";
		String DELETEfromSPACE_REQUESTS = "DELETE FROM Space_Requests WHERE Room_ID = ?";
		String DELETEfromSEATS = "DELETE FROM Seats WHERE Room_ID = ?";
		
		int affectedRow = jdbcTemplate.update(DELETE_ROOM, Room_ID);
		jdbcTemplate.update(DELETEfromSPACE_REQUESTS, Room_ID);
		jdbcTemplate.update(DELETEfromSEATS, Room_ID);
		return affectedRow;
	}
	
	
    /**
     * For update with 'Room' table in database
     * Mapping room information between URL body information and database variable attributes
     * 
     * @param room: room information corresponding to 'Rooms' table used for update
     * @return Map<String, Object>: contains variable and it's corresponding information 
     */
	private Map<String, Object> roomMap(Room room) {
		Map<String, Object> param = new HashMap<>();
		
		if(room.getRoom_id() != 0) {
			param.put("room_id", room.getRoom_id());
		}
		param.put("venue_id", room.getVenue_id() != 0 ? room.getVenue_id() : null);
		param.put("room_name", room.getRoom_name() == null || room.getRoom_name().isEmpty() ? null : room.getRoom_name());
		param.put("room_capability", room.getRoom_capability() != 0 ? room.getRoom_capability() : null);		
		param.put("rate_for_day", room.getRate_for_day() != 0 ? room.getRate_for_day() : null);
		param.put("other_details", room.getOther_details() == null || room.getOther_details().isEmpty() ? null 
				: room.getOther_details());
		return param;
	}
	
    /**
     * For update with 'Space_Requests' table in database
     * Mapping room information between URL body information and database variable attributes
     * 
     * @param room: room information corresponding to 'Space_Requests' table used for update
     * @return Map<String, Object>: contains variable and it's corresponding information 
     */
	private Map<String, Object> spaceMap(Room room) {
		Map<String, Object>param = new HashMap<>();
		
		if(room.getRoom_id() != 0) {
			param.put("room_id", room.getRoom_id());
		} 	
		param.put("event_id", room.getEvent_id() != 0 ? room.getEvent_id() : null); 
		param.put("booking_status", room.getBooking_status() == null || room.getBooking_status().isEmpty() ? null 
				: room.getBooking_status());
		param.put("occupancy", room.getOccupancy()  == null || room.getOccupancy().isEmpty() ? null 
				: room.getOccupancy());
		param.put("commercial_or_free", room.getCommercial_or_free() == null || room.getCommercial_or_free().isEmpty() ? 
				null : room.getCommercial_or_free());
		param.put("occupancy_date_from", room.getOccupancy_date_from() == null ? null : room.getOccupancy_date_from());
		param.put("occupancy_date_to", room.getOccupancy_date_to() == null ? null : room.getOccupancy_date_to());
		return param;
	}
}

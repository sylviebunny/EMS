package com.enfec.repository;

import com.enfec.model.Room;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Interface for room api
* Interface: RoomRepository
*
************************************************/
public interface RoomRepository {

	/**
	 * Create a room
	 * 
	 * @param room Room information
	 * @return affected row
	 */
	public int createRoom(Room room);

	/**
	 * Get a room's information
	 * 
	 * @param Room_ID
	 * @return Object
	 */
	public Object getRoomInfo(int Room_ID);

	/**
	 * Update a room
	 * 
	 * @param room Room information
	 * @return affected row
	 */
	public int updateRoom(Room room);

	/**
	 * Delete a room
	 * 
	 * @param Room_ID
	 * @return affected row
	 */
	public int deleteRoom(int Room_ID);
	
	/**
	 * Get all rooms' information
	 * @return Object
	 */
	public Object getAllRoomInfo();
}

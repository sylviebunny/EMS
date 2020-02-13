package com.enfec.demo.repository;

import com.enfec.demo.model.Room;

public interface RoomRepository {
	
	public int createRoom(Room room);
	public Room getRoomInfo(int Room_ID);
	public int updateRoom(Room room);	
	public int deleteRoom(int Room_ID);
}

package com.enfec.demo.repository;

import com.enfec.demo.model.Seat;

public interface SeatRepository {
	
	public int createSeat(Seat seat);
	public Seat getSeatInfo(int Seat_ID);
	public int updateSeat(Seat seat);	
//	public int deleteSeat(int Seat_ID);
}

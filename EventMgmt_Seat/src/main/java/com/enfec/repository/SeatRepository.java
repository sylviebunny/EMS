package com.enfec.repository;

import com.enfec.model.Seat;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Interface for seat api
* Interface: SeatRepository
*
************************************************/
public interface SeatRepository {
	
	/**
     * Create a seat
     * @param seat Seat information
     * @return affected row
     */
	public int createSeat(Seat seat);
	
	/**
	 * Get a seat's information
	 * @param Seat_ID
	 * @return Object
	 */
	public Object getSeatInfo(int Seat_ID);
	
	/**
     * Update a seat
     * @param seat Seat information
     * @return affected row
     */
	public int updateSeat(Seat seat);	
	
	/**
	 * Delete an organizer
	 * @param Seat_ID
	 * @return affected row
	 */
	public int deleteSeat(int Seat_ID);
	
	/**
	 * Get available seats' information in a room
	 * @param Room_ID
	 * @return Object
	 */
	public Object getAvailableSeatInfo(int Room_ID);
	
	/**
	 * Get all seats' information in a room
	 * @param Room_ID
	 * @return Object
	 */
	public Object getAllSeatInfo(int Room_ID);
	
	/**
     * Update seat availability to unavailable 
     * 
     * @param seat Seat information
     * @return affected rows
     */
	public int updateAvailability(Seat seat);
	
	/**
     * Update seat availability to available 
     * 
     * @param seat Seat information
     * @return affected rows
     */
	public int updateToAvailabile(Seat seat);
}

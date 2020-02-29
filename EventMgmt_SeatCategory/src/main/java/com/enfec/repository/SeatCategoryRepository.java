package com.enfec.repository;

import com.enfec.model.SeatCategory;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Interface for seat category api
* Interface: SeatCategoryRepository
*
************************************************/
public interface SeatCategoryRepository {
	
	/**
     * Create a seat catogory
     * @param sc Seat catogory information
     * @return affected row
     */
	public int createSeatCategory(SeatCategory sc);
	
	/**
	 * Get a seat catogory's information
	 * @param Category_ID
	 * @return Object
	 */
	public Object getSeatCategoryInfo(int Category_ID);
	
	/**
     * Update a seat catogory
     * @param sc Seat catogory information
     * @return affected row
     */
	public int updateSeatCategory(SeatCategory sc);	
	
	/**
	 * Delete a seat catogory
	 * @param Category_ID
	 * @return affected row
	 */
	public int deleteSeatCategory(int Category_ID);
}

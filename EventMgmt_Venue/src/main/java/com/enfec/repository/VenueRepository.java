package com.enfec.repository;

import com.enfec.model.Venue;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Interface for venue api
* Interface: VenueRepository
*
************************************************/
public interface VenueRepository {
	
	/**
     * Create a venue
     * @param venue Venue information
     * @return affected row
     */
	public int createVenue(Venue venue);
	
	/**
	 * Get a venue's information
	 * @param Venue_ID
	 * @return Object
	 */
	public Object getVenueInfo(int Venue_ID);
	
	/**
     * Update a venue
     * @param venue Venue information
     * @return affected row
     */
	public int updateVenue(Venue venue);	
	
	/**
	 * Delete a venue
	 * @param Venue_ID
	 * @return affected row
	 */
	public int deleteVenue(int Venue_ID);
	
	/**
	 * Get all venues' information
	 * @return Object
	 */
	public Object getAllVenueInfo();
}

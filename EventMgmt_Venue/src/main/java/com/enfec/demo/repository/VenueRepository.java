package com.enfec.demo.repository;

import com.enfec.demo.model.Venue;

public interface VenueRepository {
	public int createVenue(Venue venue);
	public Venue getVenueInfo(int Venue_ID);
//	public int updateVenue(Venue venue);	
//	public int deleteVenue(int Venue_ID);
}

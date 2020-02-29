package com.enfec.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.model.Venue;
import com.enfec.repository.VenueRepositoryImpl;
import com.google.gson.Gson;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Venue Controller
* Class: VenueController
*
************************************************/
@RestController
public class VenueController {
	
	private static final Logger logger = LoggerFactory.getLogger(VenueController.class);
	
	@Autowired
	VenueRepositoryImpl VenueRepositoryImpl;

	//For "Venues" and "Venue_Address" table in database
	//Create venue
	/**
	 * Create a venue and put its information into database
	 * 
	 * @param venue. Contains information in "Venues" and "Venue_Address" table in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/venue/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createVenue(@RequestBody(required = true) Venue venue) {
		try {
			int affectedRow = VenueRepositoryImpl.createVenue(venue);

			if (affectedRow == 0) {
				logger.info("Venue not created");
				return new ResponseEntity<>(
						"{\"message\" : \"Venue not created\"}", HttpStatus.OK);
			} else {
				logger.info("Venue created");
				return new ResponseEntity<>(
						"{\"message\" : \"Venue created\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in creating venue info: {}", exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in creating venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Get venue basic and address information from database by venue id
	 * 
	 * @param Venue_ID
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/venue/search/{Venue_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getVenueList(@PathVariable("Venue_ID") int Venue_ID) {
		try {		
			Venue venue = VenueRepositoryImpl.getVenueInfo(Venue_ID);
			if (venue == null) {
				logger.info("No venue found for: {}", Venue_ID);
				return new ResponseEntity<>(
						"{\"message\" : \"No venue found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((VenueRepositoryImpl
								.getVenueInfo(Venue_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting venue info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	/**
	 * Get all venues' basic and address information from database
	 * 
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/venue/searchall", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getAllVenueList() {
		try {		
			List<Venue> venueList = VenueRepositoryImpl.getAllVenueInfo();
			if (venueList.isEmpty()) {
				logger.info("No venue found");
				return new ResponseEntity<>(
						"{\"message\" : \"No venue found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((VenueRepositoryImpl
								.getAllVenueInfo())), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting venue info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	/**
	 * Update venue information, can update partial info
	 * 
	 * @param venue. Venue_id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/venue/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateVenue(@RequestBody(required = true) Venue venue) {
		try {	
			int affectedRow = VenueRepositoryImpl.updateVenue(venue);

			if (affectedRow == 0) {
				logger.info("No venue found for: {}", venue.getVenue_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Input venue_id not found\"}", HttpStatus.OK);
			} else {
				logger.info("Venue updated for venue_id: {} ", venue.getVenue_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Venue updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating venue info: {}", exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete a venue and its address from database by venue id
	 * 
	 * @param id: Venue_ID
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value="/venue/delete/{Venue_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteVenue(@PathVariable("Venue_ID") int id) {
		try {
			int affectedRow = VenueRepositoryImpl.deleteVenue(id);
			if(affectedRow > 0 )  {
				logger.info("Venue deleted for: {}", id);
				return new ResponseEntity<>(
						"{\"message\" : \"Venue deleted\"}", HttpStatus.OK);
			} else {
				logger.info("No venue found for: {} ", id);
				return new ResponseEntity<>(
						"{\"message\" : \"Venue not found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in deleting venue info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}

package com.enfec.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.demo.model.Venue;
import com.enfec.demo.repository.VenueRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class VenueController {
	@Autowired
	VenueRepositoryImpl VenueRepositoryImpl;

	
	@RequestMapping(value = "/venue/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createVenue(@RequestBody(required = true) Venue venue) {
		try {
			int affectedRow = VenueRepositoryImpl.createVenue(venue);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Venue not created\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Venue created\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in creating venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/venue/search/{Venue_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getVenueList(@PathVariable("Venue_ID") int Venue_ID) {
		try {		
			Venue venue = VenueRepositoryImpl.getVenueInfo(Venue_ID);
			if (venue == null) {
				return new ResponseEntity<>(
						"{\"message\" : \"No venue found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((VenueRepositoryImpl
								.getVenueInfo(Venue_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	//The foreign key information must exist first in the db
	@RequestMapping(value = "/venue/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateVenue(@RequestBody(required = true) Venue venue) {
		try {	
			int affectedRow = VenueRepositoryImpl.updateVenue(venue);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Input venue_id not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Venue updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value="/venue/delete/{Venue_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteVenue(@PathVariable("Venue_ID") int id) {
		try {
			int affectedRow = VenueRepositoryImpl.deleteVenue(id);
			if(affectedRow > 0 )  {
				return new ResponseEntity<>(
						"{\"message\" : \"Venue deleted\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Venue not found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting venue info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}

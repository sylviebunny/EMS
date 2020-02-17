package com.enfec.sb.eventapi.controller;

import java.rmi.NotBoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.zip.DataFormatException;

import com.enfec.sb.eventapi.model.EventTable;
import com.enfec.sb.eventapi.repository.EventRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class EventController {

	@Autowired
	EventRepositoryImpl eventRepositoryImpl;
	
	/* 
	 * ---------------------------------- Event GET ----------------------------------
	 * 
	 */
	// Search event by Event_ID
	// This method should be ONLY used by admin
	// URI template: ../event/search?event_id=80001
	@RequestMapping(value = "/event/search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventList (
				@RequestParam(name = "event_id", required = true) Integer event_id
			) {

			List<EventTable> resultEvent = eventRepositoryImpl.getEventByID(event_id); 
			
			if (resultEvent.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson(resultEvent), HttpStatus.OK);
			}
	}
	
	// Search events by anything, like city/state/zipcode/event_name/event_type
	// This ignores scenarios that it will show all events matches all IDs. 
	@RequestMapping(value = "/event/search/{anything}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventListByFilterBar (
			@PathVariable(required = false) String anything) {


			// Get all events information from database and get all related information 
			// Put them into List<EventTable>
			List<EventTable> getAllEvent = eventRepositoryImpl.getAllEvents(); 
			try {
				List<Map> resultEvents = eventRepositoryImpl.getFilteredEvents(getAllEvent, anything); 
				
				if (resultEvents.isEmpty()) {
					return new ResponseEntity<>(
							"{\"message\" : \"No event found\"}", HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new Gson().toJson(resultEvents), HttpStatus.OK);
				}
			} catch (NotBoundException nbe) {
				return new ResponseEntity<>(
						"{\"message\" : \"Not a valid zipcode\"}", HttpStatus.OK);
			}
	}
	
	// Get events from start_date to end_date
	@RequestMapping(value = "event/search/by_date", method = RequestMethod.GET, produces = "application/json;charset=UTF-8") 
	public ResponseEntity<String> getEventsThroughDate (
			@RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") Date start_date, 
			@RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") Date end_date) {
		try {
			
			
			Timestamp st = new Timestamp(start_date.getTime());
			Timestamp et = new Timestamp(end_date.getTime()); 
			// Check if enter the right date range
			if (st == null || et == null || et.before(st)) {
				throw new DataFormatException();
			}
			
			List<EventTable> allEvent = eventRepositoryImpl.getAllEvents(); 
			
			List<Map> result_events = eventRepositoryImpl.getFilteredEvents(allEvent, st, et); 
			
			if (result_events == null || result_events.size() == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"No event within this period\"}", HttpStatus.OK); 
			} else {
				return new ResponseEntity<>(
						new Gson().toJson(result_events), HttpStatus.OK); 
			}
		} catch (DataFormatException ex) {
			return new ResponseEntity<>(
					"{\"message\" : \"Invalid input date\"}", HttpStatus.BAD_REQUEST); 
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Unknown error, please contact admin\"}", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	/* 
	 * ---------------------------------- Event POST ----------------------------------
	 * 
	 */
	// Create event
	@RequestMapping(value = "/event/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerEvent(
			@RequestBody(required = true) EventTable eventTable) {
		try {
			int affectedRow = eventRepositoryImpl.createEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Event not registerd\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Event successfully registered\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dve) {
			return new ResponseEntity<> (
					"{\"message\" : \"Lack of required event information or invalid input\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<> (
					"{\"message\" : \"Unknown Error, need contact admin\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* 
	 * ---------------------------------- Event UPDATE ----------------------------------
	 * 
	 */
	// Update event
	@RequestMapping(value = "/event/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateEvent(
			@RequestBody(required = true) EventTable eventTable) {
		try {
			int affectedRow = eventRepositoryImpl.updateEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Event not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Event successfully updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dve) {
			return new ResponseEntity<> (
					"{\"message\" : \"Lack of required event information or invalid input\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<> (
					"{\"message\" : \"Unknown Error, need contact admin\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* 
	 * ---------------------------------- Event DELETE ----------------------------------
	 * 
	 */
	// Delete event
	@RequestMapping(value = "/event/delete/{Event_ID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8") 
	public ResponseEntity<String> deleteEvent(@PathVariable int Event_ID){
		int affectedRow = eventRepositoryImpl.deleteEvent(Event_ID); 
		
		if (affectedRow == Integer.MIN_VALUE) {
			// Didn't find this event by event_id 
			return new ResponseEntity<> (
					"{\"message\" : \"Event not found\"}", HttpStatus.OK);  
		} else {
			return new ResponseEntity<> (
					"{\"message\" : \"Event successfully deleted\"}", HttpStatus.OK); 
		}
	}
}

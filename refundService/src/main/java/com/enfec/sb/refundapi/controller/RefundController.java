package com.enfec.sb.refundapi.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.sb.refundapi.model.RefundTable;
import com.enfec.sb.refundapi.repository.RefundRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class RefundController {

	@Autowired
	RefundRepositoryImpl eventRepositoryImpl;
	
	// Search event by options
	// Organizers or customers can search an event by using filters through name/type/commercial/organizer/venue
	@RequestMapping(value = "/event/search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventList (
				@RequestParam(name = "event_id", required = false) Integer event_id, 
				@RequestParam(name = "event_name", required = false) String event_name, 
				@RequestParam(name = "event_type_code", required = false) String type_code, 
				@RequestParam(name = "free_or_commercial_code", required = false) Boolean free_or_commercial, 
				@RequestParam(name = "organizer_id", required = false) Integer organizer_id, 
				@RequestParam(name = "venue_id", required = false) Integer venue_id
			) {
		
			// Precheck if each parameter is valid 
			if ((event_id != null && event_id <= 0) || 
				(organizer_id != null && organizer_id <= 0) || 
				(venue_id != null && venue_id <= 0)) {
				return new ResponseEntity<>(
						"{\"message\" : \"Invalid id\"}", HttpStatus.BAD_REQUEST); 
			}
			
			List<RefundTable> eventList = eventRepositoryImpl.getEventInfo(event_id, event_name, type_code, free_or_commercial, organizer_id, venue_id);
			
			if (eventList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((eventRepositoryImpl
								.getEventInfo(event_id, event_name, type_code, free_or_commercial, organizer_id, venue_id))), HttpStatus.OK);
			}
	}
	
	// Create event
	@RequestMapping(value = "/event/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerEvent(
			@RequestBody(required = true) RefundTable eventTable) {
			int affectedRow = eventRepositoryImpl.createEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Event not registerd\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Event successfully registered\"}", HttpStatus.OK);
			}
	}
	
	// Update event
	@RequestMapping(value = "/event/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateEvent(
			@RequestBody(required = true) RefundTable eventTable) {
			int affectedRow = eventRepositoryImpl.updateEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Event not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Event successfully updated\"}", HttpStatus.OK);
			}
	}
	
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

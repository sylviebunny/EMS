package com.enfec.sb.eventapi.controller;

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

import com.enfec.sb.eventapi.model.EventTable;
import com.enfec.sb.eventapi.repository.EventRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class EventController {

	@Autowired
	EventRepositoryImpl eventRepositoryImpl;
	
	// Search event by event ID
	@RequestMapping(value = "/event/search/event_id/{Event_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventListByID(@PathVariable int Event_ID) {
			List<EventTable> eventList = eventRepositoryImpl
					.getEventInfoByID(Event_ID);
			if (eventList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((eventRepositoryImpl
								.getEventInfoByID(Event_ID))), HttpStatus.OK);
			}
	}
	
	// Search event by event name 
	@RequestMapping(value = "/event/search/event_name/{Event_Name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventListByID(@PathVariable String Event_Name) {
			List<EventTable> eventList = eventRepositoryImpl
					.getEventInfoByName(Event_Name);
			if (eventList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((eventRepositoryImpl
								.getEventInfoByName(Event_Name))), HttpStatus.OK);
			}
	}
	
	// Create event
	@RequestMapping(value = "/event/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerEvent(
			@RequestBody(required = true) EventTable eventTable) {
			int affectedRow = eventRepositoryImpl
					.createEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Event not registerd\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Event Registered\"}", HttpStatus.OK);
			}
	}
	
	// Update event
	@RequestMapping(value = "/event/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateEvent(
			@RequestBody(required = true) EventTable eventTable) {
			int affectedRow = eventRepositoryImpl
					.updateEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Event not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Event updated\"}", HttpStatus.OK);
			}
	}
	
}

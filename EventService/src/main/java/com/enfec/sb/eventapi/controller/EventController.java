package com.enfec.sb.eventapi.controller;

import com.enfec.sb.eventapi.model.EventTable;
import com.enfec.sb.eventapi.repository.EventRepositoryImpl;
import com.google.gson.Gson;
import java.rmi.NotBoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class EventController {

	@Autowired
	EventRepositoryImpl eventRepositoryImpl;

	/**
	 * Get API for events with given event ID
	 * @param event_id specific event ID
	 * @return response entity
	 */
	@RequestMapping(value = "/event/search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventList(@RequestParam(name = "event_id", required = true) Integer event_id) {
		try {
			List<EventTable> resultEvent = eventRepositoryImpl.getEventByID(event_id);

			if (resultEvent.isEmpty()) {
				return new ResponseEntity<>("{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Gson().toJson(resultEvent), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>("{\"message\" : \"Unknown error, please contact developer\"}", HttpStatus.OK);
		}
	}

	/**
	 * Get API for events whose city, zipcode, event name, event type contains specified content 
	 * @param word - key word to be searched by
	 * @return response entity
	 */
	@RequestMapping(value = "/event/search/{word}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventListByFilterBar(@PathVariable(required = false) String word) {
		List<EventTable> getAllEvent = eventRepositoryImpl.getAllEvents();
		try {
			List<Map> resultEvents = eventRepositoryImpl.getFilteredEventsByRefinedZipcode(getAllEvent, word);

			if (resultEvents.isEmpty()) {
				return new ResponseEntity<>("{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Gson().toJson(resultEvents), HttpStatus.OK);
			}
		} catch (NotBoundException nbe) {
			return new ResponseEntity<>("{\"message\" : \"Not a valid zipcode\"}", HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>("{\"message\" : \"Unknown error, please contact with developer\"}",
					HttpStatus.OK);
		}
	}

	/**
	 * Get API for events with specified zip code, event type and start/end dates
	 * @param start_date start date of searched time range, exclusive
	 * @param end_date end date of searched time range, exclusive
	 * @param zipcode specified zip code
	 * @param event_type specified event type
	 * @return response entity
	 */
	@RequestMapping(value = "event/search/by_date", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventsByDateZipCodeAndEventType(
			@RequestParam(required = true) @DateTimeFormat(pattern = "MM/dd/yyyy") Date start_date,
			@RequestParam(required = true) @DateTimeFormat(pattern = "MM/dd/yyyy") Date end_date,
			@RequestParam(required = false) String zipcode, 
			@RequestParam(required = false) String event_type) {
		try {

			Timestamp st = new Timestamp(start_date.getTime());
			Timestamp et = new Timestamp(end_date.getTime());
			if (st == null || et == null || et.before(st)) {
				throw new DataFormatException();
			}

			List<EventTable> allEvent = eventRepositoryImpl.getAllEvents();

			List<Map> result_events_within_date = eventRepositoryImpl.getFilteredEvents(allEvent, st, et);
			
			List<Map> result_events_by_zipcode = null;
			if (zipcode != null && zipcode.length() != 0) {
				result_events_by_zipcode = eventRepositoryImpl.getEventByZipcode(result_events_within_date,
						Integer.parseInt(zipcode));
			} else {
				result_events_by_zipcode = result_events_within_date;
			}
			
			List<Map> result_events = eventRepositoryImpl.getEventByEventType(result_events_by_zipcode, event_type);  // Get events with specific type

			if (result_events == null || result_events.size() == 0) {
				return new ResponseEntity<>("{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Gson().toJson(result_events), HttpStatus.OK);
			}
		} catch (DataFormatException ex) {
			return new ResponseEntity<>("{\"message\" : \"Invalid input date\"}", HttpStatus.BAD_REQUEST);
		} catch (CannotGetJdbcConnectionException ce) {
			return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>("{\"message\" : \"Unknown error, please contact developer\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Post API to create an event
	 * @param eventTable parameter table of the event to be created
	 * @return response entity
	 */
	@RequestMapping(value = "/event/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerEvent(@RequestBody(required = true) EventTable eventTable) {
		try {
			int affectedRow = eventRepositoryImpl.createEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>("{\"message\" : \"Event not registerd\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("{\"message\" : \"Event successfully registered\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dve) {
			return new ResponseEntity<>("{\"message\" : \"Lack of required event information or invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<>("{\"message\" : \"Unknown Error, need contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Put API to update an event
	 * @param eventTable parameter table of the event to be updated with
	 * @return response entity
	 */
	@RequestMapping(value = "/event/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateEvent(@RequestBody(required = true) EventTable eventTable) {
		try {
			int affectedRow = eventRepositoryImpl.updateEvent(eventTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>("{\"message\" : \"Event not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("{\"message\" : \"Event successfully updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dve) {
			return new ResponseEntity<>("{\"message\" : \"Lack of required event information or invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<>("{\"message\" : \"Unknown Error, need contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete API for to delete an event with specified event ID
	 * @param Event_ID specified event ID
	 * @return response entity
	 */
	@RequestMapping(value = "/event/delete/{Event_ID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteEvent(@PathVariable int Event_ID) {
		int affectedRow = eventRepositoryImpl.deleteEvent(Event_ID);

		if (affectedRow == Integer.MIN_VALUE) {
			// Didn't find this event by event_id
			return new ResponseEntity<>("{\"message\" : \"Event not found\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"message\" : \"Event successfully deleted\"}", HttpStatus.OK);
		}
	}
}

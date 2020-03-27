package com.enfec.eventapi.controller;

import java.rmi.NotBoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.enfec.eventapi.model.EventTable;
import com.enfec.eventapi.repository.AmazonClient;
import com.enfec.eventapi.repository.EventRepositoryImpl;
import com.google.gson.Gson;

/************************************************
*
* Author: Heidi Huo
* Assignment: Event Controller
* Class: EventController
*
************************************************/
@CrossOrigin
@RestController
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class); 
    
    @Autowired
	EventRepositoryImpl eventRepositoryImpl;
    
    @Autowired
    private AmazonClient amazonClient;

	/**
	 * Get API for events with given event ID
	 * @param event_id specific event ID
	 * @return response entity
	 */
	@RequestMapping(value = "/event/search", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getEventList(@RequestParam(name = "event_id", required = false) Integer event_id) {
	    if (event_id == null) {
	        List<EventTable> getAllEvent = eventRepositoryImpl.getAllEvents();
	        List<Map> allEvents = eventRepositoryImpl.getFilteredEventsWithDateRange(getAllEvent, null, null); 
	        List<Map> afterToday = eventRepositoryImpl.getEventsAfterToday(allEvents); 
	        logger.info("No input word or filter, print out all events");
	        return new ResponseEntity<>(new Gson().toJson(afterToday),
	                HttpStatus.OK) ; 
	    }
		try {
			List<EventTable> resultEvent = eventRepositoryImpl.getEventByID(event_id);

			if (resultEvent.isEmpty()) {
			    logger.info("No event found, event_id: {}", event_id);
				return new ResponseEntity<>("{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
			    logger.info("Event found by event id, event_id: {}", event_id); 
				return new ResponseEntity<>(new Gson().toJson(resultEvent), HttpStatus.OK);
			}
		} catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database");
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
		    logger.error("Exception in searching event: {}", ex.getMessage());
			return new ResponseEntity<>("{\"message\" : \"Unknown error, please contact developer\"}", HttpStatus.INTERNAL_SERVER_ERROR);
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
			List<Map> afterFilterEvents = eventRepositoryImpl.getFilteredEventsByWordFilter(getAllEvent, word);
			List<Map> resultEvents = eventRepositoryImpl.getEventsAfterToday(afterFilterEvents); 
			
			if (resultEvents.isEmpty()) {
			    logger.info("No event found, word: {}", word);
				return new ResponseEntity<>("{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
			    logger.info("Events found, word: {}", word); 
				return new ResponseEntity<>(new Gson().toJson(resultEvents), HttpStatus.OK);
			}
		} catch (NotBoundException nbe) {
		    logger.warn("Not a valid zipcode, {}", word); 
			return new ResponseEntity<>("{\"message\" : \"Not a valid zipcode\"}", HttpStatus.OK);
		} catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database");
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
		    logger.error("Input word: {}", word);
		    logger.error("Exception in getting events: {}", ex.getMessage()); 
			return new ResponseEntity<>("{\"message\" : \"Unknown error, please contact with developer\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
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

			List<Map> result_events_within_date = eventRepositoryImpl.getFilteredEventsWithDateRange(allEvent, st, et);
			
			List<Map> result_events_by_zipcode = null;
			if (zipcode != null && zipcode.length() != 0) {
				result_events_by_zipcode = eventRepositoryImpl.getEventByZipcode(result_events_within_date,
						Integer.parseInt(zipcode));
			} else {
				result_events_by_zipcode = result_events_within_date;
			}
			
			List<Map> afterFilterEvents = eventRepositoryImpl.getEventByEventType(result_events_by_zipcode, event_type);  // Get events with specific type

			List<Map> result_events = eventRepositoryImpl.getEventsAfterToday(afterFilterEvents); 
			
			if (result_events == null || result_events.size() == 0) {
			    logger.info("Based on given criteria, no event found");
				return new ResponseEntity<>("{\"message\" : \"No event found\"}", HttpStatus.OK);
			} else {
			    logger.info("Events found");
			    logger.info("start date: {}", start_date);
                logger.info("end date: {}", end_date);
                logger.info("zipcode: {}", zipcode); 
                logger.info("event type: {}", event_type);
				return new ResponseEntity<>(new Gson().toJson(result_events), HttpStatus.OK);
			}
		} catch (DataFormatException ex) {
		    logger.error("Invalid input date");
		    logger.error("start date: {}", start_date);
            logger.error("end date: {}", end_date);
			return new ResponseEntity<>("{\"message\" : \"Invalid input date\"}", HttpStatus.BAD_REQUEST);
		} catch (CannotGetJdbcConnectionException ce) {
		    logger.error("Fail to connect database: {}",ce.getMessage());
			return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
		    logger.error("Exception in searching events: {}", e.getMessage());
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
			    logger.info("No event created");
				return new ResponseEntity<>("{\"message\" : \"No event created\"}", HttpStatus.OK);
			} else {
			    logger.info("Event successfully registered");
				return new ResponseEntity<>("{\"message\" : \"Event successfully registered\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dve) {
		    logger.error("Lack of required event information or invalid input");
            logger.error("Organizer id: {}", eventTable.getOrganizer_id());
            logger.error("Venue id: {}", eventTable.getVenue_id());
            logger.error("Event status code: {}", eventTable.getEvent_status_code());
            logger.error("Event type code: {}", eventTable.getEvent_type_code());
			return new ResponseEntity<>("{\"message\" : \"Lack of required event information or invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            logger.error("Exception in creating event: {}", ex.getMessage());
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
			    logger.info("Event not found, event_id: {}", eventTable.getEvent_id());
				return new ResponseEntity<>("{\"message\" : \"Event not found\"}", HttpStatus.OK);
			} else {
			    logger.info("Event successfully updated, event_id: {}", eventTable.getEvent_id());
				return new ResponseEntity<>("{\"message\" : \"Event successfully updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dve) {
		    logger.error("Lack of required event information or invalid input");
            logger.error("Organizer id: {}", eventTable.getOrganizer_id());
            logger.error("Venue id: {}", eventTable.getVenue_id());
            logger.error("Event status code: {}", eventTable.getEvent_status_code());
            logger.error("Event type code: {}", eventTable.getEvent_type_code());
			return new ResponseEntity<>("{\"message\" : \"Lack of required event information or invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            logger.error("Exception in updating event, event_id: {}", eventTable.getEvent_id());
            logger.error("Error message: {}", ex.getMessage());
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
	    try {
    		int affectedRow = eventRepositoryImpl.deleteEvent(Event_ID);
    
    		if (affectedRow == Integer.MIN_VALUE) {
    		    logger.info("No event found based on event id, event_id: {}", Event_ID);
    			return new ResponseEntity<>("{\"message\" : \"Event not found\"}", HttpStatus.OK);
    		} else {
    		    logger.info("Event successfully deleted");
    			return new ResponseEntity<>("{\"message\" : \"Event successfully deleted\"}", HttpStatus.OK);
    		}
	    } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            logger.error("Exception in updating event, event_id: {}", Event_ID);
            logger.error("Error message: {}", ex.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown Error, need contact admin\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
	/**
	 * 
	 * @param multipartFile Upload Image to cloud storage
	 * @return URL of the image
	 */
    @RequestMapping(value = "/event/uploadImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) {
        try {
            String newURL = this.amazonClient.uploadFile(multipartFile);

            logger.info("Image URL in S3 is: " + newURL); 
            return new ResponseEntity<>("{\"Image URL\" : \"" + newURL + "\"}", HttpStatus.OK);
        } catch (IllegalStateException ilex) {
          logger.error("Error messageL {}", ilex.getMessage());
          return new ResponseEntity<>("{\"message\" : \"Upload image should be under 2MB\"}", HttpStatus.INTERNAL_SERVER_ERROR); 
        } catch (Exception ex) {
            logger.error("Error message: {}", ex.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown Error, need contact admin\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

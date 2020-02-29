package com.enfec.controller;

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

import com.enfec.model.Room;
import com.enfec.repository.RoomRepositoryImpl;
import com.google.gson.Gson;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Room Controller
* Class: RoomController
*
************************************************/
@RestController
public class RoomController {
	
	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	RoomRepositoryImpl RoomRepositoryImpl;

	/**
	 * Create or book an room and put its information into database
	 * 
	 * @param room. Contains information in "Room" and "Space_Requests" table in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/room/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createRoom(@RequestBody(required = true) Room room) {
		try {
			int affectedRow = RoomRepositoryImpl.createRoom(room);
			if (affectedRow == 0) {
				logger.info("Room not created");
				return new ResponseEntity<>("{\"message\" : \"Room not created\"}", HttpStatus.OK);
			} else {
				logger.info("Room created");
				return new ResponseEntity<>("{\"message\" : \"Room created\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Event_id or venue_id not found, or Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Event_id or venue_id not found, or Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in creating room info: {}", exception.getMessage());
			return new ResponseEntity<>("{\"message\" : \"Exception in creating room info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get room information from database by room id
	 * 
	 * @param Room_ID
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/room/search/{Room_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getRoomList(@PathVariable("Room_ID") int Room_ID) {
		try {
			Room room = RoomRepositoryImpl.getRoomInfo(Room_ID);
			if (room == null) {
				logger.info("No room found for: {}", Room_ID);
				return new ResponseEntity<>("{\"message\" : \"No room found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Gson().toJson((RoomRepositoryImpl.getRoomInfo(Room_ID))),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting room info: {}", e.getMessage());
			return new ResponseEntity<>("{\"message\" : \"Exception in getting room info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update room information, can update partial info
	 * 
	 * @param room. Room_id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/room/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateRoom(@RequestBody(required = true) Room room) {
		try {
			int affectedRow = RoomRepositoryImpl.updateRoom(room);
			if (affectedRow == 0) {
				logger.info("No room found for: {}", room.getRoom_id());
				return new ResponseEntity<>("{\"message\" : \"Input room_id not found\"}", HttpStatus.OK);
			} else {
				logger.info("Room updated for room_id: {} ", room.getRoom_id());
				return new ResponseEntity<>("{\"message\" : \"Room updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// The foreign key information must exist first in the db
			logger.error("Event_id or venue_id not found, or Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Event_id or venue_id not found, or Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating room info: {}", exception.getMessage());
			return new ResponseEntity<>("{\"message\" : \"Exception in updating room info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete a room from database by room id
	 * 
	 * @param Room_ID
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/room/delete/{Room_ID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteRoom(@PathVariable("Room_ID") int id) {
		try {
			int affectedRow = RoomRepositoryImpl.deleteRoom(id);
			if (affectedRow > 0) {
				logger.info("Room deleted for: {}", id);
				return new ResponseEntity<>("{\"message\" : \"Room deleted\"}", HttpStatus.OK);
			} else {
				logger.info("No room found for: {} ", id);
				return new ResponseEntity<>("{\"message\" : \"Room not found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in deleting room info: {}", e.getMessage());
			return new ResponseEntity<>("{\"message\" : \"Exception in deleting room info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

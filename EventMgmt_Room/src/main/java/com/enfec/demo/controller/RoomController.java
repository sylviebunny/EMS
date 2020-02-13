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

import com.enfec.demo.model.Room;
import com.enfec.demo.repository.RoomRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class RoomController {
	@Autowired
	RoomRepositoryImpl RoomRepositoryImpl;

	
	@RequestMapping(value = "/room/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createRoom(@RequestBody(required = true) Room room) {
		try {	
			int affectedRow = RoomRepositoryImpl.createRoom(room);
			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Room not booked\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Room booked\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Bad Request: invalid info\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in creating room info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/room/search/{Room_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getRoomList(@PathVariable("Room_ID") int Room_ID) {
		try {	
			Room room = RoomRepositoryImpl.getRoomInfo(Room_ID);
			if (room == null) {
				return new ResponseEntity<>(
						"{\"message\" : \"No room found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((RoomRepositoryImpl
								.getRoomInfo(Room_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting room info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	//The foreign key information must exist first in the db
	@RequestMapping(value = "/room/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateRoom(@RequestBody(required = true) Room room) {
		try {	
			int affectedRow = RoomRepositoryImpl.updateRoom(room);
			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Room not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Room updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Bad Request: invalid info\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating room info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/room/delete/{Room_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteRoom(@PathVariable("Room_ID") int id) {
		try {
			int affectedRow = RoomRepositoryImpl.deleteRoom(id);
			if(affectedRow > 0 )  {
				return new ResponseEntity<>(
						"{\"message\" : \"Room deleted\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Room is not able to delete\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting room info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}

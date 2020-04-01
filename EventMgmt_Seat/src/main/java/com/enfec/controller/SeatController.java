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

import com.enfec.model.Seat;
import com.enfec.repository.SeatRepositoryImpl;
import com.google.gson.Gson;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Seat Controller
* Class: SeatController
*
************************************************/
@RestController
public class SeatController {
	
	private static final Logger logger = LoggerFactory.getLogger(SeatController.class);
	
	@Autowired
	SeatRepositoryImpl SeatRepositoryImpl;

	
	/**
	 * Create a seat and put its information into database
	 * 
	 * @param seat. Contains information in "Seats" table in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/seat/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createSeat(@RequestBody(required = true) Seat seat) {
		try {	
			int affectedRow = SeatRepositoryImpl.createSeat(seat);
			if (affectedRow == 0) {
				logger.info("Seat not created");
				return new ResponseEntity<>(
						"{\"message\" : \"Seat not created\"}", HttpStatus.OK);
			} else {
				logger.info("Seat created");
				return new ResponseEntity<>(
						"{\"message\" : \"Seat created\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Room_id or category_id not found or Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Room_id or category_id not found or Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in creating seat info: {}", exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in creating seat info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Get seat information from database by seat id
	 * 
	 * @param Seat_ID
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/seat/search/{Seat_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getSeatList(@PathVariable("Seat_ID") int Seat_ID) {
		try {
			Seat seat = SeatRepositoryImpl.getSeatInfo(Seat_ID);
			if (seat == null) {
				logger.info("No seat found for: {}", Seat_ID);
				return new ResponseEntity<>(
						"{\"message\" : \"No seat found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatRepositoryImpl
								.getSeatInfo(Seat_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting seat info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting seat info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	/**
	 * Update seat information, can update partial info
	 * 
	 * @param seat. Seat_id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/seat/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateSeat(@RequestBody(required = true) Seat seat) {
		try {	
			int affectedRow = SeatRepositoryImpl.updateSeat(seat);
			if (affectedRow == 0) {
				logger.info("No seat found for: {}", seat.getSeat_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Input seat_id not found\"}", HttpStatus.OK);
			} else {
				logger.info("Seat updated for seat_id: {} ", seat.getSeat_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Seat updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			//The foreign key information must exist first in the db
			logger.error("Room_id or category_id not found or Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Room_id or category_id not found or Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating seat info: {}", exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating seat info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update seat availability to unavailable 
	 * 
	 * @param seat. Seat_id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/seat/setseat", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateAvailability(@RequestBody(required = true) Seat seat) {
		try {	
			int affectedRow = SeatRepositoryImpl.updateAvailability(seat);
			if (affectedRow == 0) {
				logger.info("No seat found for: {}", seat.getSeat_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Input seat_id not found\"}", HttpStatus.OK);
			} else {
				logger.info("Seat updated for seat_id: {} ", seat.getSeat_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Seat updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Room_id or category_id not found or Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating seat info: {}", exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating seat availability, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Delete a seat from database by seat id
	 * 
	 * @param Seat_ID
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value="/seat/delete/{Seat_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteSeat(@PathVariable("Seat_ID") int id) {
		try {
			int affectedRow = SeatRepositoryImpl.deleteSeat(id);
			if(affectedRow > 0 )  {
				logger.info("Seat deleted for: {}", id);
				return new ResponseEntity<>(
						"{\"message\" : \"Seat deleted\"}", HttpStatus.OK);
			} else {
				logger.info("No seat found for: {} ", id);
				return new ResponseEntity<>(
						"{\"message\" : \"Seat not found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in deleting seat info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting seat info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	/**
	 * Get available seats information from database by room id
	 * 
	 * @param Room_ID
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/seat/getavailable/{Room_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getAvailableSeatList(@PathVariable("Room_ID") int Room_ID) {
		try {
			List<Seat> seatList = SeatRepositoryImpl.getAvailableSeatInfo(Room_ID);
			if (seatList.isEmpty()) {
				logger.info("No seat found for: {}", Room_ID);
				return new ResponseEntity<>(
						"{\"message\" : \"Room not found or No available seat\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatRepositoryImpl
								.getAvailableSeatInfo(Room_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting available seats info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting available seats info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	/**
	 * Get all seats information in a specific room from database by room id
	 * 
	 * @param Room_ID
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/seat/getall/{Room_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getAllSeatList(@PathVariable("Room_ID") int Room_ID) {
		try {
			List<Seat> seatList = SeatRepositoryImpl.getAllSeatInfo(Room_ID);
			if (seatList.isEmpty()) {
				logger.info("No seat found for: {}", Room_ID);
				return new ResponseEntity<>(
						"{\"message\" : \"Room not found or No available seat\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatRepositoryImpl
								.getAllSeatInfo(Room_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting all seats info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting all seats info in the room, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}

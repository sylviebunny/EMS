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

import com.enfec.demo.model.Seat;
import com.enfec.demo.repository.SeatRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class SeatController {
	@Autowired
	SeatRepositoryImpl SeatRepositoryImpl;

	@RequestMapping(value = "/seat/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createSeat(@RequestBody(required = true) Seat seat) {
		try {	
			int affectedRow = SeatRepositoryImpl.createSeat(seat);
			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat not created\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat created\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Bad Request: invalid info\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in creating seat info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/seat/search/{Seat_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getSeatList(@PathVariable("Seat_ID") int Seat_ID) {
		try {
			Seat seat = SeatRepositoryImpl.getSeatInfo(Seat_ID);
			if (seat == null) {
				return new ResponseEntity<>(
						"{\"message\" : \"No seat found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatRepositoryImpl
								.getSeatInfo(Seat_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting seat info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	//The foreign key information must exist first in the db
	@RequestMapping(value = "/seat/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateSeat(@RequestBody(required = true) Seat seat) {
		try {	
			int affectedRow = SeatRepositoryImpl.updateSeat(seat);
			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat updated\"}", HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Bad Request: invalid info\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating seat info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value="/seat/delete/{Seat_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteSeat(@PathVariable("Seat_ID") int id) {
		try {
			int affectedRow = SeatRepositoryImpl.deleteSeat(id);
			if(affectedRow > 0 )  {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat deleted\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat is not able to delete\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting seat info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
	
	//Search available seats by room_id
	@RequestMapping(value = "/seat/getavailable/{Room_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getAvailableSeatList(@PathVariable("Room_ID") int Room_ID) {
		try {
			List<Seat> seatList = SeatRepositoryImpl.getAvailableSeatInfo(Room_ID);
			if (seatList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No available seat found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatRepositoryImpl
								.getAvailableSeatInfo(Room_ID))), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting available seats info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}

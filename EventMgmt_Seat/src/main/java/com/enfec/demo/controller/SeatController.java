package com.enfec.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
			int affectedRow = SeatRepositoryImpl.createSeat(seat);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat not created\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat created\"}", HttpStatus.OK);
			}
	}
	
	@RequestMapping(value = "/seat/search/{Seat_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getSeatList(@PathVariable("Seat_ID") int Seat_ID) {
			Seat seat = SeatRepositoryImpl.getSeatInfo(Seat_ID);
			
			if (seat == null) {
				return new ResponseEntity<>(
						"{\"message\" : \"No seat found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatRepositoryImpl
								.getSeatInfo(Seat_ID))), HttpStatus.OK);
			}
	}

	//The foreign key information must exist first in the db
	@RequestMapping(value = "/seat/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateSeat(@RequestBody(required = true) Seat seat) {
			int affectedRow = SeatRepositoryImpl.updateSeat(seat);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat updated\"}", HttpStatus.OK);
			}
	}

	
	@RequestMapping(value="/seat/delete/{Seat_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteSeat(@PathVariable("Seat_ID") int id) {
		int affectedRow = SeatRepositoryImpl.deleteSeat(id);
		if(affectedRow > 0 )  {
			return new ResponseEntity<>(
					"{\"message\" : \"Seat deleted\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					"{\"message\" : \"Seat is not able to delete\"}", HttpStatus.OK);
		}
	}
}

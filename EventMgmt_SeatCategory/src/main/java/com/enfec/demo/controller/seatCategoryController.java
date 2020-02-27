package com.enfec.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.enfec.demo.model.SeatCategory;
import com.enfec.demo.repository.SeatCategoryRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class seatCategoryController {
	@Autowired
	SeatCategoryRepositoryImpl SeatCategoryRepositoryImpl;

	
	@RequestMapping(value = "/seatcategory/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createSeatCategory(@RequestBody(required = true) SeatCategory sc) {
			
			try {
				int affectedRow = SeatCategoryRepositoryImpl.createSeatCategory(sc);
				if (affectedRow == 0) {
					return new ResponseEntity<>(
							"{\"message\" : \"Seat category not created\"}", HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							"{\"message\" : \"Seat category created\"}", HttpStatus.OK);
				}
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
						HttpStatus.BAD_REQUEST);
			} catch (Exception exception) {
				return new ResponseEntity<>(
						"{\"message\" : \"Exception in creating seat category info, please contact admin\"}",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@RequestMapping(value = "/seatcategory/search/{Category_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getSeatCategoryList(@PathVariable("Category_ID") int Category_ID) {
		try {	
			SeatCategory sc = SeatCategoryRepositoryImpl.getSeatCategoryInfo(Category_ID);
			if (sc == null) {
				return new ResponseEntity<>(
						"{\"message\" : \"No seat category found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatCategoryRepositoryImpl
								.getSeatCategoryInfo(Category_ID))), HttpStatus.OK);
			} 
		} catch (Exception e) {
				return new ResponseEntity<>(
						"{\"message\" : \"Exception in getting seat category info, please contact admin\"}",
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//The foreign key information must exist first in the db
	@RequestMapping(value = "/seatcategory/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateSeatCategory(@RequestBody(required = true) SeatCategory sc) {
		try {
			int affectedRow = SeatCategoryRepositoryImpl.updateSeatCategory(sc);
			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category updated\"}", HttpStatus.OK);
			} 
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating seat category info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value="/seatcategory/delete/{Category_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteSeatCategory(@PathVariable("Category_ID") int id) {
		try {	
			int affectedRow = SeatCategoryRepositoryImpl.deleteSeatCategory(id);
			if(affectedRow > 0 )  {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category deleted\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category not found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting seat category info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

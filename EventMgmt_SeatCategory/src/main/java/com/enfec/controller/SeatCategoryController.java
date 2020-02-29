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

import com.enfec.model.SeatCategory;
import com.enfec.repository.SeatCategoryRepositoryImpl;
import com.google.gson.Gson;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Seat category Controller
* Class: SeatCategoryController
*
************************************************/
@RestController
public class SeatCategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(SeatCategoryController.class);
	
	@Autowired
	SeatCategoryRepositoryImpl SeatCategoryRepositoryImpl;

	
	/**
	 * Create a seat category and put its information into database
	 * 
	 * @param sc: SeatCategory, contains information in "Seat_Category" table in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/seatcategory/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createSeatCategory(@RequestBody(required = true) SeatCategory sc) {
		try {
			int affectedRow = SeatCategoryRepositoryImpl.createSeatCategory(sc);
			if (affectedRow == 0) {
				logger.info("Seat category not created");
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category not created\"}", HttpStatus.OK);
			} else {
				logger.info("Seat category created");
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category created\"}", HttpStatus.OK);
				}
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				logger.error("Invalid input");
				return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
						HttpStatus.BAD_REQUEST);
			} catch (Exception exception) {
				logger.error("Exception in creating seat category info: {}", exception.getMessage());
				return new ResponseEntity<>(
						"{\"message\" : \"Exception in creating seat category info, please contact admin\"}",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	/**
	 * Get seat category information from database by seat id
	 * 
	 * @param Category_ID
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/seatcategory/search/{Category_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getSeatCategoryList(@PathVariable("Category_ID") int Category_ID) {
		try {	
			SeatCategory sc = SeatCategoryRepositoryImpl.getSeatCategoryInfo(Category_ID);
			if (sc == null) {
				logger.info("No seat category found for: {}", Category_ID);
				return new ResponseEntity<>(
						"{\"message\" : \"No seat category found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((SeatCategoryRepositoryImpl
								.getSeatCategoryInfo(Category_ID))), HttpStatus.OK);
			} 
		} catch (Exception e) {
			logger.error("Exception in getting seat category info: {}", e.getMessage());
				return new ResponseEntity<>(
						"{\"message\" : \"Exception in getting seat category info, please contact admin\"}",
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update seat category information, can update partial info
	 * 
	 * @param sc: SeatCategory. Category_id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/seatcategory/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateSeatCategory(@RequestBody(required = true) SeatCategory sc) {
		try {
			int affectedRow = SeatCategoryRepositoryImpl.updateSeatCategory(sc);
			if (affectedRow == 0) {
				logger.info("No seat category found for: {}", sc.getCategory_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category not found\"}", HttpStatus.OK);
			} else {
				logger.info("Seat category updated for category_id: {} ", sc.getCategory_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category updated\"}", HttpStatus.OK);
			} 
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid input");
			return new ResponseEntity<>("{\"message\" : \"Invalid input\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating seat category info: {}", exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in updating seat category info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete a seat category from database by seat category id
	 * 
	 * @param id: Category_ID
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value="/seatcategory/delete/{Category_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteSeatCategory(@PathVariable("Category_ID") int id) {
		try {	
			int affectedRow = SeatCategoryRepositoryImpl.deleteSeatCategory(id);
			if(affectedRow > 0 )  {
				logger.info("Seat category deleted for: {}", id);
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category deleted\"}", HttpStatus.OK);
			} else {
				logger.info("No seat category found for: {} ", id);
				return new ResponseEntity<>(
						"{\"message\" : \"Seat category not found\"}", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in deleting seat category info: {}", e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in deleting seat category info, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

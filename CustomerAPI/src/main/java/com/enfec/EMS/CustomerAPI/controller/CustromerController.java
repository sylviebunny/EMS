package com.enfec.EMS.CustomerAPI.controller;


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

import com.enfec.EMS.CustomerAPI.model.CustomerTable;
import com.enfec.EMS.CustomerAPI.repository.CustomerRepositoryImpl;
import com.google.gson.Gson;





@RestController
public class CustromerController {
	private static final Logger logger = LoggerFactory.getLogger(CustromerController.class);
	
	@Autowired
	CustomerRepositoryImpl customerRepositoryImpl;
	
	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String>getCustomerList(@PathVariable String id) { 
			List<CustomerTable> customerList = customerRepositoryImpl.getCustomer(id);
			if (customerList.isEmpty()) {
				logger.info("No organizer found for: {} ", id);
				return new ResponseEntity<>(
						"{\"message\" : \"No organizer found\"}", HttpStatus.OK);
			
			}
			return new ResponseEntity<>(
					new Gson().toJson((customerRepositoryImpl.getCustomer(id))), HttpStatus.OK);
		}
	
	@RequestMapping(value = "/Customer/Register", method = RequestMethod.POST, produces = "applications/json; charset=UTF-8")
	public ResponseEntity<String>customerRegister(
			@RequestBody(required = true) CustomerTable customerTable){
		try {
			int affectedRow = customerRepositoryImpl.registerCustomer(customerTable);
			if(affectedRow == 0) {
				logger.info("Customer not registered customer_name: {}", customerTable.getName());
				return new ResponseEntity<String>(
						"{\"message\" : \"Customer not registered\"}",
						HttpStatus.OK);
			}else {
				logger.info("Custoer registered customer_name: {}", customerTable.getName());
				return new ResponseEntity<String>("{\"message\": \"Customer registered\"}", HttpStatus.OK);
			}
		
		}catch(DataIntegrityViolationException dataIntegrityViolationException){
			logger.error("Invalid Customer id:{}", customerTable.getId());
			return new ResponseEntity<String>(
					"{\"message\": \"Invalid Customer id\"}", 
					HttpStatus.BAD_REQUEST);
		}catch(Exception exception){
			logger.error("Exceprtion in regestering Customer:{}", exception.getMessage());
			return new ResponseEntity<String>(
					"{\"message\": \"Exception in regestering Customer info\"}", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/Customer/Update", method = RequestMethod.PUT, produces = "applications/json; charset=UTF-8")
	public ResponseEntity<String>CustomerUpdate(
			@RequestBody(required = true) CustomerTable customerTable){
		try {
			int affectedRow = customerRepositoryImpl.updateCustomer(customerTable);
			if(affectedRow == 0) {
				logger.info("Customer not updated customer_name: {}", customerTable.getName());
				return new ResponseEntity<String>(
						"{\"message\" : \"Customer not updated\"}",
						HttpStatus.OK);
			}else {
				logger.info("Custoer updated customer_name: {}", customerTable.getName());
				return new ResponseEntity<String>(
						"{\"message\": \"Customer updated\"}", HttpStatus.OK);
			}
		
		}catch(DataIntegrityViolationException dataIntegrityViolationException){
			logger.error("Invalid Customer id:{}", customerTable.getId());
			return new ResponseEntity<String>(
					"{\"message\": \"Invalid Customer id\"}", HttpStatus.BAD_REQUEST);
		}catch(Exception exception){
			logger.error("Exceprtion in updating Customer:{}", exception.getMessage());
			return new ResponseEntity<String>(
					"{\"message\": \"Exception in updating Customer info\"}", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/Customer/Delete/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteCustomer(
			@PathVariable String id) {

			int affectedRow = customerRepositoryImpl.deleteCustomer(id);
			if (affectedRow==0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Customer not found\"}", HttpStatus.OK);
			}else {
			return new ResponseEntity<>(
					"{\"message\" : \"Customer deleted\"}", HttpStatus.OK);
			}

	}
	
}
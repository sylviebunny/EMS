package com.enfec.EMS.CustomerAPI.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
}
package com.enfec.EMS.CustomerOrderAPI.controller;


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

import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderTable;
import com.enfec.EMS.CustomerOrderAPI.model.TicketTable;
import com.enfec.EMS.CustomerOrderAPI.repository.CustomerOrderRepositoryImpl;
import com.google.gson.Gson;





@RestController
public class CustromerOrderController {
	private static final Logger logger = LoggerFactory.getLogger(CustromerOrderController.class);
	
	@Autowired
	CustomerOrderRepositoryImpl customerOrderRepositoryImpl;
	
	
	//Customer Order Functions
	//Get customer order
	@RequestMapping(value = "/CustomerOrder/{customerOrderID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String>getCustomerOrderList(@PathVariable String customerOrderID) { 
			List<CustomerOrderTable> customerList = customerOrderRepositoryImpl.getCustomerOrder(customerOrderID);
			if (customerList.isEmpty()) {
				logger.info("No Organizer Order found for: {} ", customerOrderID);
				return new ResponseEntity<>(
						"{\"message\" : \"No Organizer Order found\"}", HttpStatus.OK);
			
			}
			return new ResponseEntity<>(
					new Gson().toJson((customerOrderRepositoryImpl.getCustomerOrder(customerOrderID))), HttpStatus.OK);
		}
	
	
	//Create customer order
	@RequestMapping(value = "/CustomerOrder/Create", method = RequestMethod.POST, produces = "applications/json; charset=UTF-8")
	public ResponseEntity<String>createCustomerOrder(
			@RequestBody(required = true) CustomerOrderTable customerOrderTable){
		try {
			int affectedRow = customerOrderRepositoryImpl.createCustomerOrder(customerOrderTable);
			if(affectedRow == 0) {
				logger.info("Customer order not created customerID: {}", customerOrderTable.getCustomerID());
				return new ResponseEntity<String>(
						"{\"message\" : \"Customer order not created\"}",
						HttpStatus.OK);
			}else {
				logger.info("Custoer order created customerID: {}", customerOrderTable.getCustomerID());
				return new ResponseEntity<String>("{\"message\": \"Customer order created\"}", HttpStatus.OK);
			}
		
		}catch(DataIntegrityViolationException dataIntegrityViolationException){
			logger.error("Invalid Customer Order id:{}", customerOrderTable.getCustomerOrderID());
			return new ResponseEntity<String>(
					"{\"message\": \"Invalid Customer Order id\"}", 
					HttpStatus.BAD_REQUEST);
		}catch(Exception exception){
			logger.error("Exceprtion in creating Customer order:{}", exception.getMessage());
			return new ResponseEntity<String>(
					"{\"message\": \"Exception in creating Customer order info\"}", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//Delete Customer Order
	@RequestMapping(value = "/CustomerOrder/Delete/{customerOrderID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteCusotmerOrder(
			@PathVariable String customerOrderID) {

			int affectedRow = customerOrderRepositoryImpl.deleteCustomerOrder(customerOrderID);
			if (affectedRow==0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Customer Order not found\"}", HttpStatus.OK);
			}else {
			return new ResponseEntity<>(
					"{\"message\" : \"Customer Order deleted\"}", HttpStatus.OK);
			}
			

	}
	

	
	//Ticket Functions
	//Get ticket
	@RequestMapping(value = "/Ticket/{ticketID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String>getTicketList(@PathVariable String ticketID) { 
			List<TicketTable> ticketList = customerOrderRepositoryImpl.getTicket(ticketID);
			if (ticketList.isEmpty()) {
				logger.info("No Ticket found for: {} ", ticketID);
				return new ResponseEntity<>(
						"{\"message\" : \"No Ticket found\"}", HttpStatus.OK);
			
			}
			return new ResponseEntity<>(
					new Gson().toJson((customerOrderRepositoryImpl.getTicket(ticketID))), HttpStatus.OK);
		}
	
	
	
	//Create ticket
		@RequestMapping(value = "/Ticket/Create", method = RequestMethod.POST, produces = "applications/json; charset=UTF-8")
		public ResponseEntity<String>createTicket(
				@RequestBody(required = true) TicketTable ticketTable){
			try {
				int affectedRow = customerOrderRepositoryImpl.createTicket(ticketTable);
				if(affectedRow == 0) {
					logger.info("Ticket not created customerOrderID: {}", ticketTable.getCustomerOrderID());
					return new ResponseEntity<String>(
							"{\"message\" : \"Ticket not created\"}",
							HttpStatus.OK);
				}else {
					logger.info("Ticket created customerOrderID: {}", ticketTable.getCustomerOrderID());
					return new ResponseEntity<String>("{\"message\": \"Ticket created\"}", HttpStatus.OK);
				}
			
			}catch(DataIntegrityViolationException dataIntegrityViolationException){
				logger.error("Invalid ticket id:{}", ticketTable.getTicketID());
				return new ResponseEntity<String>(
						"{\"message\": \"Invalid ticket id\"}", 
						HttpStatus.BAD_REQUEST);
			}catch(Exception exception){
				logger.error("Exceprtion in creating Ticket:{}", exception.getMessage());
				return new ResponseEntity<String>(
						"{\"message\": \"Exception in creating Ticket info\"}", 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		//Update ticket
		@RequestMapping(value = "/Ticket/Update", method = RequestMethod.PUT, produces = "applications/json; charset=UTF-8")
		public ResponseEntity<String>updateTicket(
				@RequestBody(required = true) TicketTable ticketTable){
			try {
				int affectedRow = customerOrderRepositoryImpl.updateTicket(ticketTable);
				if(affectedRow == 0) {
					logger.info("Ticket not updated customerOrderID: {}", ticketTable.getCustomerOrderID());
					return new ResponseEntity<String>(
							"{\"message\" : \"Ticket not updated\"}",
							HttpStatus.OK);
				}else {
					logger.info("Custoer updated customerOrderID: {}", ticketTable.getCustomerOrderID());
					return new ResponseEntity<String>(
							"{\"message\": \"Ticket updated\"}", HttpStatus.OK);
				}
			
			}catch(DataIntegrityViolationException dataIntegrityViolationException){
				logger.error("Invalid Ticket id:{}", ticketTable.getTicketID());
				return new ResponseEntity<String>(
						"{\"message\": \"Invalid Ticket id\"}", HttpStatus.BAD_REQUEST);
			}catch(Exception exception){
				logger.error("Exceprtion in updating Ticket:{}", exception.getMessage());
				return new ResponseEntity<String>(
						"{\"message\": \"Exception in updating Ticket info\"}", 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/Ticket/Delete/{ticketID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
		public ResponseEntity<String> deleteTicket(
				@PathVariable String ticketID) {

				int affectedRow = customerOrderRepositoryImpl.deleteTicket(ticketID);
				if (affectedRow==0) {
					return new ResponseEntity<>(
							"{\"message\" : \"Ticket not found\"}", HttpStatus.OK);
				}else {
				return new ResponseEntity<>(
						"{\"message\" : \"Ticket deleted\"}", HttpStatus.OK);
				}
				

		}
	
	
}
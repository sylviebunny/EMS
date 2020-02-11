package com.enfec.sb.refundapi.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.sb.refundapi.model.COrderRefundTable;
import com.enfec.sb.refundapi.model.OOrderRefundTable;
import com.enfec.sb.refundapi.repository.RefundRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class RefundController {

	@Autowired
	RefundRepositoryImpl refundRepositoryImpl;
	
	// Search organizer's refund by refund_id
	@RequestMapping(value = "/organizer_refund/search/{organizer_refund_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizerRefundByRefundID(@PathVariable int organizer_refund_id) {
			
			List<OOrderRefundTable> organizerRefundList = refundRepositoryImpl.getOrganizerRefundByRefundID(organizer_refund_id);
			
			if (organizerRefundList == null || organizerRefundList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No refund found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((refundRepositoryImpl
								.getOrganizerRefundByRefundID(organizer_refund_id))), HttpStatus.OK);
			}
	}
	
	// Search organizer's refund by oorder_id 
	@RequestMapping(value = "/organizer_refund/search/organizer_order_id/{oorder_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizerRefundByOorder (@PathVariable int oorder_id) {
			
			List<OOrderRefundTable> organizerRefundList = refundRepositoryImpl.getOrganizerRefundByOorderID(oorder_id);
			
			if (organizerRefundList == null || organizerRefundList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No refund found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((refundRepositoryImpl
								.getOrganizerRefundByOorderID(oorder_id))), HttpStatus.OK);
			}
	}
	
	// Create organizer refund by entering oorder_ID and description
	@RequestMapping(value = "/organizer_refund/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createOrganizerRefund (
			@RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
			int affectedRow = refundRepositoryImpl.createOrganizerRefund(organizerRefundTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund not created\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund successfully registered\"}", HttpStatus.OK);
			}
	}
	
	// Update organizer refund by specific refund_Id, entering status and description
	@RequestMapping(value = "/organizer_refund/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateOrganizerRefund (
			@RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
			int affectedRow = refundRepositoryImpl.updateOrganizerRefund(organizerRefundTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund not updated\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund successfully updated\"}", HttpStatus.OK);
			}
	}
	
	// Delete organizer refund by refund_id
	@RequestMapping(value = "/organizer_refund/delete/{organizer_refund_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8") 
	public ResponseEntity<String> deleteOrganizerRefund(@PathVariable int organizer_refund_id){
		int affectedRow = refundRepositoryImpl.deleteOrganizerRefund(organizer_refund_id); 
		
		if (affectedRow == Integer.MIN_VALUE) {
			// Didn't find this event by event_id 
			return new ResponseEntity<> (
					"{\"message\" : \"Organizer refund not found\"}", HttpStatus.OK);  
		} else {
			return new ResponseEntity<> (
					"{\"message\" : \"Organizer refund successfully deleted\"}", HttpStatus.OK); 
		}
	}
	
	/* -----------------------------------------------------------
	 *  	Below are customer related controller
	 * -----------------------------------------------------------
	 */
	
	// Search customer's refund by crefund_id
	@RequestMapping(value = "/customer_refund/search/{customer_refund_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getCustomerRefundByRefundID(@PathVariable int customer_refund_id) {
			
			List<COrderRefundTable> customerRefundList = refundRepositoryImpl.getCustomerRefundByCRefundID(customer_refund_id);
			
			if (customerRefundList == null || customerRefundList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No customer refund found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((refundRepositoryImpl
								.getCustomerRefundByCRefundID(customer_refund_id))), HttpStatus.OK);
			}
	}
	
	// Search customer's refund by corder_id 
	@RequestMapping(value = "/customer_refund/search/customer_order_id/{corder_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getCustomerRefundByCorderID (@PathVariable int corder_id) {
			
			List<COrderRefundTable> customerRefundList = refundRepositoryImpl.getCustomerRefundByCorderID(corder_id);
			
			if (customerRefundList == null || customerRefundList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No customer refund found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((refundRepositoryImpl
								.getCustomerRefundByCorderID(corder_id))), HttpStatus.OK);
			}
	}
	
	// Create customer refund by entering corder_ID and crefund_description
	@RequestMapping(value = "/customer_refund/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createCustomerRefund (
			@RequestBody(required = true) COrderRefundTable customerRefund) {
			int affectedRow = refundRepositoryImpl.createCustomerRefund(customerRefund);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Customer refund not created\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Customer refund successfully registered\"}", HttpStatus.OK);
			}
	}
	
	// Update customer refund by specific crefund_Id, entering status and description
	@RequestMapping(value = "/customer_refund/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateCustomerRefund (
			@RequestBody(required = true) COrderRefundTable customerRefund) {
			int affectedRow = refundRepositoryImpl.updateCustomerRefund(customerRefund);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund not updated\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund successfully updated\"}", HttpStatus.OK);
			}
	}
	
	// Delete customer refund by crefund_id
	@RequestMapping(value = "/customer_refund/delete/{customer_refund_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8") 
	public ResponseEntity<String> deleteCustomerRefund(@PathVariable int customer_refund_id){
		int affectedRow = refundRepositoryImpl.deleteCustomerRefund(customer_refund_id); 
		
		if (affectedRow == Integer.MIN_VALUE) {
			// Didn't find this event by event_id 
			return new ResponseEntity<> (
					"{\"message\" : \"Organizer refund not found\"}", HttpStatus.OK);  
		} else {
			return new ResponseEntity<> (
					"{\"message\" : \"Organizer refund successfully deleted\"}", HttpStatus.OK); 
		}
	}
	
}

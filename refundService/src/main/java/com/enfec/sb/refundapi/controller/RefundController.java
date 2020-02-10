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

import com.enfec.sb.refundapi.model.OOrderRefundTable;
import com.enfec.sb.refundapi.repository.RefundRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class RefundController {

	@Autowired
	RefundRepositoryImpl organizerRefundRepositoryImpl;
	
	// Search organizer's refund by refund_id
	@RequestMapping(value = "/organizer_refund/search/{organizer_refund_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizerRefund(@PathVariable int organizer_refund_id) {
			
			List<OOrderRefundTable> eventList = organizerRefundRepositoryImpl.getOrganizerRefund(organizer_refund_id);
			
			if (eventList == null || eventList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No refund found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((organizerRefundRepositoryImpl
								.getOrganizerRefund(organizer_refund_id))), HttpStatus.OK);
			}
	}
	
	// Create organizer refund
	@RequestMapping(value = "/organizer_refund/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createOrganizerRefund (
			@RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
			int affectedRow = organizerRefundRepositoryImpl.createOrganizerRefund(organizerRefundTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund not created\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer refund successfully registered\"}", HttpStatus.OK);
			}
	}
	
	// Delete event
	@RequestMapping(value = "/organizer_refund/delete/{Refund_ID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8") 
	public ResponseEntity<String> deleteEvent(@PathVariable int Refund_ID){
		int affectedRow = organizerRefundRepositoryImpl.deleteOrganizerRefund(Refund_ID); 
		
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

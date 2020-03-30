package com.enfec.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.model.ChargeRequest;
import com.enfec.model.RefundRequest;
import com.enfec.service.StripeClient;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Charge and Refund API using Stripe
* Class: PaymentController
*
************************************************/
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

	/**
	 * Create a charge with a card, update database with stripe information
	 * 
	 * @param chargeRequest: ChargeRequest, contains requested token,amount for payment, order id and user type
	 * @return ResponseEntity with message and data
	 */
    @RequestMapping(value = "/charge", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> chargeCard(@RequestBody ChargeRequest chargeRequest) throws StripeException {
    	try {
	        Charge charge = this.stripeClient.chargeCreditCard(chargeRequest);
	        String chargeId = charge.getId();
	        Long amount = charge.getAmount();
	        String status = charge.getStatus();
	        
	//        String type = chargeRequest.getUser_type();
	        int orderID = chargeRequest.getOrder_id();
	        
	        String dbResult = null;
	        int count = this.stripeClient.updateCusOrder(status, chargeId, orderID);
	        if (count > 0) {
	        	dbResult = "\"Updated\"";
	        } else {
	        	dbResult = "\"Something wrong...Database cannot update\"";
	        }
	        /*
	        else if (type.equals("organizer")) {
	        	int count = this.stripeClient.updateOrgOrder(status, chargeId, orderID);
	        	if (count > 0) {
	        		dbResult = "Updated.";
	        	} else {
	        		dbResult = "Something wrong...Database cannot update.";
	        	}
	        }
	        */
	        return new ResponseEntity<>("{\"message\": \"Your card has been charged successfully\", \n" 
					+ "\"charge_id\": \"" + chargeId + "\",\n"
						+ "\"amount in USD\": " + amount/100.0 + ",\n"
							+ "\"database result\": " + dbResult + "\n}", HttpStatus.OK);
    	} catch (InvalidRequestException exception) {
			return new ResponseEntity<>("{\"message\" : \"You cannot use a Stripe token more than once\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			return new ResponseEntity<>("{\"message\" : \"Exception in charging a card, please contact admin\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
	/**
	 * Make a refund associated with the charge id, update database with stripe information
	 * 
	 * @param refundRequest: RefundRequest, contains charge id, order id and user type
	 * @return ResponseEntity with message and data
	 */
    @RequestMapping(value = "/refund", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getRefund(@RequestBody RefundRequest refundRequest) throws StripeException {
    	String chargeID = refundRequest.getCharge_id();
    	try {
	        Refund refund = this.stripeClient.createRefund(refundRequest);
	        String refundId = refund.getId();
	        Long amount = refund.getAmount();
	        String status = refund.getStatus();
	        
	//        String type = refundRequest.getUser_type();
	        int orderID = refundRequest.getOrder_id();
	        
	        String dbResult = null;
	        int count = this.stripeClient.updateCusRefund(status, refundId, orderID);
	        if (count > 0) {
	        	dbResult = "\"Updated\"";
	        } else {
	        	dbResult = "\"Something wrong...Database cannot update\"";
	        }
	        /*
	        } else if (type.equals("organizer")) {
	        	int count = this.stripeClient.updateOrgRefund(status, refundId, orderID);
	        	if (count > 0) {
	        		dbResult = "Updated.";
	        	} else {
	        		dbResult = "Something wrong...Database cannot update.";
	        	}
	        }*/
	        return new ResponseEntity<>("{\"message\": \"Congratulations, your charge has been refunded\", \n" 
					+ "\"refund_id\": \"" + refundId + "\",\n"
						+ "\"amount in USD\": " + amount/100.0 + ",\n"
							+ "\"database result\": " + dbResult + "\n}", HttpStatus.OK);
	    } catch (Exception exception) {
			return new ResponseEntity<>("{\"message\" : \"This charge has already been refunded\", \n"
					+ "\"charge_id\": \"" + chargeID + "\"\n}", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
    }
    
    /*
    @PostMapping("/charge1")
    public ResponseEntity<String> chargeCard1(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        String number = request.getHeader("amount");
        Double amount = Double.valueOf(number);
//        String email = request.getHeader("email");
        Charge charge = this.stripeClient.chargeCreditCard1(token, amount);
        String chargeId = charge.getId();
        return new ResponseEntity<>("{\"message\" : \"Your card has been charged sucessfully\", \n" 
				+ "\"chargeID\" :" + chargeId + "\n}", HttpStatus.OK);
    }
    
    @PostMapping("/refund1")
    public ResponseEntity<String> getRefund1(HttpServletRequest request) throws Exception {
        String chargeId = request.getHeader("chargeId");
        Refund refund = this.stripeClient.createRefund1(chargeId);
        String refundId = refund.getId();
        Long amount = refund.getAmount();   
        return new ResponseEntity<>("{\"message\" : \"Congratulations, your charge has been refunded\", \n" 
				+ "\"refundID\" :" + refundId + "\n"
						+ "\"amount in USD\" :" + amount/100.0 + "\n}", HttpStatus.OK);
    }*/
    
    /**
	 * Create a customer in a Stripe account
	 * 
	 * @param request: HttpServletRequest, contains requested token and customer email
	 * @return ResponseEntity with message and data
	 */
    /*
    @PostMapping("/createcustomer")
    public ResponseEntity<String> createCustomer(HttpServletRequest request) throws Exception {
        String email = request.getHeader("email");
        Customer cus = this.stripeClient.createCustomer(email);
        String cusId = cus.getId();
        return new ResponseEntity<>("{\"message\" : \"A new customer has been created successfully\",\n" 
				+ "\"refundID\" :" + cusId + "\n"
						+ "\"Email\" :" + cus.getEmail() + "\n}", HttpStatus.OK);
    }*/
    
	/**
	 * Create a charge for a customer using Stripe API
	 * 
	 * @param request: HttpServletRequest, contains customer ID and amount for payment
	 * @return ResponseEntity with message and data
	 */
    /*
    @PostMapping("/chargecustomer")
    public ResponseEntity<String> chargeCustomer(HttpServletRequest request) throws Exception {
        String customerId = request.getHeader("customerId");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        Charge charge = this.stripeClient.chargeCustomerCard(customerId, amount);
        String chargeId = charge.getId();
        return new ResponseEntity<>("{\"message\" : \"Your card associated with the account has been charged sucessfully\",\n" 
				+ "\"chargeID\" :" + chargeId + "\n"
						+ "\"customerID\" :" + customerId + "\n}", HttpStatus.OK);
    }*/
}

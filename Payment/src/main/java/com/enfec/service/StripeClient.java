package com.enfec.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.model.ChargeRequest;
import com.enfec.model.RefundRequest;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Implement payment methods with Stripe
* Class: StripeClient
*
************************************************/
@Component
@Transactional
public class StripeClient {
	
    @Autowired
    StripeClient() {
        Stripe.apiKey = "xxxxxxxxxxx";
    }

	@Autowired
	private JdbcTemplate jt;
    
	/**
     * Charge credit card with requested token and amount for payment
     * Map charge parameters and create a charge information in Stripe 
     * 
     * @param token Requested token sent by front-end
     * @param amount to be charged
     * @param email to be charged for
     * 
     * @return Charge object
     */
    public Charge chargeCreditCard(ChargeRequest chargeRequest) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {

        Map<String, Object> chargeParams = new HashMap<String, Object>();
	    chargeParams.put("amount", (int)(chargeRequest.getAmount() * 100));
	    chargeParams.put("currency", "USD");
	    chargeParams.put("source", chargeRequest.getToken());
	    Charge charge = Charge.create(chargeParams);
        return charge;
    }
    
    /**
     * Update 'Customer_Orders' table in database with stripe payment information
     * 
     * @param status
     * @param chargeId
     * @param orderID
     * @return number of affected rows
     */
    public int updateCusOrder(String status, String chargeId, int orderID) {
    	String sql = "UPDATE Customer_Orders SET Order_Status=?,Stripe_Status=?,Stripe_Charge_ID=? WHERE COrder_ID =?";
    	int count = jt.update(sql, "Paid", status, chargeId, orderID);
    	return count;
    }
    
    /**
     * Update 'Organizer_Orders' table in database with stripe payment information
     * 
     * @param status
     * @param chargeId
     * @param orderID
     * @return number of affected rows
     */
    public int updateOrgOrder(String status, String chargeId, int orderID) {
    	String sql = "UPDATE Organizer_Orders SET Order_Status=?,Stripe_Status=?,Stripe_Charge_ID=? WHERE OOrder_ID =?";
    	int count = jt.update(sql, "Paid", status, chargeId, orderID);
    	return count;
    }
    
	/**
     * Refund to a card by charge id 
     * Map refund parameters and create a refund in Stripe 
     * 
     * @param chargeId to be processed
     * @return Refund object
     */
    public Refund createRefund(RefundRequest refundRequest) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
    	Map<String, Object> params = new HashMap<>();
    	params.put("charge", refundRequest.getCharge_id());
    	Refund refund = Refund.create(params);
    	return refund;
    }
    
    /**
     * Update 'Customer_Refund' table in database with stripe refund information
     * 
     * @param status
     * @param refundId
     * @param orderID
     * @return number of affected rows
     */
    public int updateCusRefund(String status, String refundId, int orderID) {
    	String sql = "UPDATE Customer_Refund SET CRefund_Status=?,Stripe_Status=?,Stripe_Refund_ID=? WHERE COrder_ID =?";
    	int count = jt.update(sql, "Refunded", status, refundId, orderID);
    	return count;
    }
    
    /**
     * Update 'Refund' table in database with stripe refund information
     * 
     * @param status
     * @param refundId
     * @param orderID
     * @return number of affected rows
     */
    public int updateOrgRefund(String status, String refundId, int orderID) {
    	String sql = "UPDATE Refund SET Refund_Status=?,Stripe_Status=?,Stripe_Refund_ID=? WHERE OOrder_ID =?";
    	int count = jt.update(sql, "Refunded", status, refundId, orderID);
    	return count;
    }
    
    /*
    public Charge chargeCreditCard1(String token, double amount) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
//    	String chargeId = null;
//        try {
        	Map<String, Object> chargeParams = new HashMap<String, Object>();
	        chargeParams.put("amount", (int)(amount * 100));
	        chargeParams.put("currency", "USD");
	        chargeParams.put("source", token);
//	        chargeParams.put("description", "Charge for " + email);
	        Charge charge = Charge.create(chargeParams);
//	        chargeId = charge.getId();
//        } catch(StripeException e) {
//            throw new RuntimeException("Unable to process the charge", e);
//        }
        return charge;
    }
    
    public Refund createRefund1(String chargeId) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
    	Map<String, Object> params = new HashMap<>();
    	params.put("charge", chargeId);
    	Refund refund = Refund.create(params);
    	return refund;
    }
    */
    
	/**
     * Create customer if want to save user’s credit card on Stripe side
     * To save customerId to user’s model in order not to loose it
     * 
     * @param token Requested token sent by front-end
     * @param email How much to be charged
     * @return Charge object
     */
    /*
    public Customer createCustomer(String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
//        customerParams.put("source", token);
        return Customer.create(customerParams);
    }*/

	/**
     * Charge existing customer with customerId and amount for payment
     * Map charge parameters and create a charge information in Stripe 
     * 
     * @param customerId which already exsits
     * @param amount How much to be charged
     * @return Charge object
     */
    /*
    public Charge chargeCustomerCard(String customerId, double amount) throws Exception {
        String sourceCard = Customer.retrieve(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }*/
}

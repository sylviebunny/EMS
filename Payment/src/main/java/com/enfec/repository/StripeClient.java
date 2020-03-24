package com.enfec.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@Component
public class StripeClient {
	
    @Autowired
    StripeClient() {
        Stripe.apiKey = "sk_test_xxxxxxxxxxxx";
    }

	/**
     * Charge credit card with requested token and amount for payment
     * Map charge parameters and create a charge information in Stripe 
     * 
     * @param token Requested token sent by front-end
     * @param amount How much to be charged
     * @return Charge object
     */
    public Charge chargeCreditCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
    
	/**
     * Create customer if want to save user’s credit card on Stripe side
     * To save customerId to user’s model in order not to loose it
     * 
     * @param token Requested token sent by front-end
     * @param email How much to be charged
     * @return Charge object
     */
    public Customer createCustomer(String token, String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
        customerParams.put("source", token);
        return Customer.create(customerParams);
    }
    
	/**
     * Charge existing customer with customerId and amount for payment
     * Map charge parameters and create a charge information in Stripe 
     * 
     * @param customerId which already exsits
     * @param amount How much to be charged
     * @return Charge object
     */
    public Charge chargeCustomerCard(String customerId, int amount) throws Exception {
        String sourceCard = Customer.retrieve(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
}

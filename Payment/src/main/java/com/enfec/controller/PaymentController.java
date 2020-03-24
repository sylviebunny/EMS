package com.enfec.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.service.StripeClient;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

	/**
	 * Create a charge using Stripe API
	 * 
	 * @param request: HttpServletRequest, contains requested token and amount for payment
	 * @return Charge object in Stripe
	 */
    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
    	
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        String email = request.getHeader("email");
        return this.stripeClient.chargeCreditCard(token, amount, email);
    }
    
    /**
	 * Create a customer in a Stripe account
	 * 
	 * @param request: HttpServletRequest, contains requested token and customer email
	 * @return Customer object in Stripe
	 */
    @PostMapping("/createcustomer")
    public Customer createCustomer(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        String email = request.getHeader("email");
        return this.stripeClient.createCustomer(token, email);
    }
    
	/**
	 * Create a charge for a customer using Stripe API
	 * 
	 * @param request: HttpServletRequest, contains customer ID and amount for payment
	 * @return Charge object in Stripe
	 */
    @PostMapping("/chargecustomer")
    public Charge chargeCustomer(HttpServletRequest request) throws Exception {
        String customerId = request.getHeader("customerId");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeClient.chargeCustomerCard(customerId, amount);
    }

}

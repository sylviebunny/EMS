package com.enfec.EMS.CustomerAPI.repository;

import java.sql.Timestamp;
import com.enfec.EMS.CustomerAPI.model.CustomerTable;

/************************************************
* Author: Chad Chai
* Assignment: Interface for customer API
* Interface: CustomerRepository
************************************************/

public interface CustomerRepository {
	
	/**
	 * Get the customer's basic information
	 * @param id: customer id number
	 * @return Object
	 */
	public Object getCustomer(String id);
	
	
	/**
     * Get all Customer basic information from database 
     * @return List<CustomerTable>: all entries that match the request
     */
	public Object getAllCustomer();
	
	
	/**
     * Create a customer
     * @param customerTable: Customer basic information
     * @return affected row
     */
	public int registerCustomer(CustomerTable customerTable);
	
	
	/**
     * Update a customer
     * @param customerTable: Customer basic information
     * @return affected row
     */
	public int updateCustomer(CustomerTable customerTable);
	
	
	/**
	 * Delete a customer
	 * @param id: customer id number
	 * @return affected row
	 */
	public int deleteCustomer(String id);
	
	
	/**
     * Customer login: determine if email and password match in database
     * @param cEmail: customer email which is used to login
	 * @param cPwd: Customer input password
     * @return whether the cEmail match with the cPwd in database
     */
	public boolean isMatching(String cEmail, String cPwd);
	
	
	/**
     * Login role type check: determine if email belongs to customer or organizer
     * @param email: email which is used to login
     * @return "Customer" or "Organizer"
     */
	public String roleType(String email);
	
	
	/**
     * Customer register: determine if the email exist in database
     * @param customerEmail: customer email which is used to register as a new customer
     * @return whether the customerEmail exist in database or not.
     */
	public boolean hasRegistered(String customerEmail);
	
	
	/**
     * Customer register: send register confirmation email to customer
     * @param to: the email address of the customer
     * @param subject: the subject of the confirmation email
     * @param body: the detail of confirmation email
     * @param CToken: the OTP for confirmation email
     * @return null
     */
	public void sendGreetMail(String to, String subject, String body, String CToken);

	
	/**
     * Customer forget password: determine if email is in customer table
     * @param customerEmail: customer email which is used to get back password
     * @return whether the email address is in the customer table or not
     */
	public boolean isValidCustomer(String customerEmail);
	
	
	/**
     * Customer forget password: determine if email is in customer token table
     * @param customerEmail: customer email which is used to get back password
     * @return whether the email address is in the customer token table or not
     */
	public boolean hasForgetenPWD(String customerEmail);
	
	
	/**
     * Save Token info: save the customer token info to customer token table
     * @param cEmail: the email address of the customer
     * @param cToken: the random generated OTP 
     * @param cExpiryDate: the expire time of the cToken
     * @return affected row
     */
	public int saveTokenInfo(String cEmail, String cToken, Timestamp cExpiryDate);
	
	
	/**
     * Customer reset password: send reset password link to customer email
     * @param to: the email address of the customer
     * @param subject: the subject of the reset password email
     * @param body: the detail of reset password email
     * @param CToken: the OTP for reset password
     * @return null
     */
	public void sendPwdMail(String to, String subject, String body, String CToken);
	
	
	/**
     * Generate a random string as token
     */
	public String generateToken();
	
	
	/**
     * Verify token: determine if the token is correct and not expire
     * @param CToken: customer token 
     * @return whether the customer token is in the token table and expire or not
     */
	public boolean validToken(String CToken);
	
	
	/**
     * Verify token: determine if the token is checked or not
     * @param CToken: customer token 
     * @return whether the customer token is checked or not
     */
	public boolean hasChecked(String CToken);
	
	
	/**
     * Update customer password: save the new password to customer table
     * @param cEmail: the email address of the customer
     * @param newpwd: the random generated OTP 
     * @return affected row
     */
	public int updatePassword(String cEmail, String newpwd);
	
	
	/**
	 * Get the customer's email information using customer token
	 * @param CToken: the OTP for customer to reset password
	 * @return Object
	 */
	public Object findEmailByToken(String CToken);
	
	
	/**
     * Update customer token table if customer token is expired
     * @param cEmail: the email address of the customer
     * @param cToken: the random generated OTP 
     * @param cExpiryDate: the expire time of the cToken
     * @return affected row
     */
	public int updateToken(String cEmail, String cToken, Timestamp expireDate);
	
	
	/**
     * Update customer token status if this token has been checked
     * @param cToken: the random generated OTP 
     * @return affected row
     */
	public int updateTokenStatus(String cToken);
	
	/**
     * Update customer email: change email address
     * @param cID: the customer ID
     * @param newEmail: the new email address
     * @return affected row
     */
	public int updateEmail(int cID, String newEmail);
	
	/**
	 * Get the customer's ID information using customer Email
	 * @param CEmail: the Customer Email Address
	 * @return Object
	 */
	public Object findIDByEmail(String CEmail);
}

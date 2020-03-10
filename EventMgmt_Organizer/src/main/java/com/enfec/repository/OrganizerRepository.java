package com.enfec.repository;

import java.sql.Timestamp;

import com.enfec.model.Address;
import com.enfec.model.OrganizerContactTable;
import com.enfec.model.OrganizerTable;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Interface for organizer api
* Interface: OrganizerRepository
*
************************************************/
public interface OrganizerRepository {

	/**
	 * Create an organizer
	 * 
	 * @param organizerTable Organizer basic information
	 * @return affected row
	 */
	public int createOrganizer(OrganizerTable organizerTable);

	/**
	 * Get an organizer's basic information
	 * 
	 * @param Organizer_ID
	 * @return Object
	 */
	public Object getOrganizerInfo(int Organizer_ID);

	/**
	 * Get all organizer's basic information
	 * 
	 * @return Object
	 */
	public Object getAllOrganizerInfo();
	
	/**
	 * Update an organizer
	 * 
	 * @param organizerTable Organizer basic information
	 * @return affected row
	 */
	public int updateOrganizer(OrganizerTable organizerTable);

	/**
	 * Delete an organizer
	 * 
	 * @param Organizer_ID
	 * @return affected row
	 */
	public int deleteOrganizer(int Organizer_ID);

	/**
	 * Create an organizer address
	 * 
	 * @param address Organizer address information
	 * @return affected row
	 */
	public int createAddress(Address address);

	/**
	 * Get an organizer's address
	 * 
	 * @param Organizer_ID
	 * @return Object
	 */
	public Object getAddressInfo(int Organizer_ID);

	/**
	 * Update an organizer address
	 * 
	 * @param address Organizer address information
	 * @return affected row
	 */
	public int updateAddress(Address address);

	/**
	 * Create an organizer contact
	 * 
	 * @param organizerContactTable Organizer contact information
	 * @return affected row
	 */
	public int createOrganizerContact(OrganizerContactTable organizerContactTable);

	/**
	 * Get an organizer's contact
	 * 
	 * @param Organizer_ID
	 * @return Object
	 */
	public Object getOrganizerContactInfo(int organizer_id);

	/**
	 * Update an organizer contact
	 * 
	 * @param organizerContactTable Organizer contact information
	 * @return affected row
	 */
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable);

	/**
	 * Organizer login: determine if email and password match in database
	 * 
	 * @param OEmail: Organizer email which is used to login
	 * @param oPwd: Organizer input password
	 * @return whether oEmail and oPwd match or not
	 */
	public boolean isMatching(String OEmail, String oPwd);
	
	/**
	 * Organizer register: determine if the email exist in database
	 * 
	 * @param organizerEmail: organizer email which is used to register as a new organizer
	 * @return whether the organizerEmail exist in database or not.
	 */
	public boolean hasRegistered(String organizerEmail);

	/**
     * Organizer register: send register confirmation email to organizer
     * 
     * @param to: the email address of the organizer
     * @param subject: the subject of the confirmation email
     * @param body: the detail of confirmation email
     * @param oToken: the OTP for confirmation email
     * @return null
     */
	public void sendGreetMail(String to, String subject, String body, String oToken);

	/**
     * Organizer forget password: determine if email is in organizer table
     * 
     * @param organizerEmail: organizer email which is used to get back password
     * @return whether the email address is in the organizer table or not
     */
	public boolean isValidOrganizer(String organizerEmail);

	/**
     * Organizer forget password: determine if email is in organizer token table
     * 
     * @param organizerEmail: organizer email which is used to get back password
     * @return whether the email address is in the organizer token table or not
     */
	public boolean hasForgetenPWD(String organizerEmail);

	/**
     * Save Token info: save the organizer token info to organizer token table
     * 
     * @param oEmail: the email address of the organizer
     * @param oToken: the random generated OTP 
     * @param oExpiryDate: the expire time of the cToken
     * @return affected row
     */
	public int saveTokenInfo(String oEmail, String oToken, Timestamp oExpiryDate);

	/**
     * Organizer reset password: send reset password link to organizer email
     * 
     * @param to: the email address of the organizer
     * @param subject: the subject of the reset password email
     * @param body: the detail of reset password email
     * @param oToken: the OTP for reset password
     * @return null
     */
	public void sendPwdMail(String to, String subject, String body, String oToken);

	/**
     * Generate a random string as token
     */
	public String generateToken();

	/**
     * Verify token: determine if the token is correct and not expire
     * 
     * @param oToken: customer token 
     * @return whether the organizer token is in the token table and expire or not
     */
	public boolean validToken(String oToken);

	/**
     * Update organizer password: save the new password to organizer table
     * 
     * @param oEmail: the email address of the organizer
     * @param newpwd: the random generated OTP 
     * @return affected row
     */
	public int updatePassword(String oEmail, String newpwd);

	/**
	 * Get the organizer's email information using organizer token
	 * 
	 * @param oToken: the OTP for organizer to reset password
	 * @return Object
	 */
	public Object findEmailByToken(String oToken);

	/**
     * Update organizer token table if organizer token is expired
     * 
     * @param oEmail: the email address of the organizer
     * @param oToken: the random generated OTP 
     * @param expiryDate: the expire time of the oToken
     * @return affected row
     */
	public int updateToken(String oEmail, String oToken, Timestamp expireDate);
}

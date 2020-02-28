package com.enfec.repository;

import java.sql.Timestamp;

import com.enfec.model.Address;
import com.enfec.model.OrganizerContactTable;
import com.enfec.model.OrganizerTable;


/**
 * This is an interface for organizer api
 * @author sylvia zhao
 */
public interface OrganizerRepository {
	
	/**
     * Create an organizer
     * @param organizerTable Organizer basic information
     * @return affected row
     */

	public int createOrganizer(OrganizerTable organizerTable);	
	
	/**
	 * Get an organizer's basic information
	 * @param Organizer_ID
	 * @return Object
	 */
	public Object getOrganizerInfo(int Organizer_ID);
	
	/**
     * Update an organizer
     * @param organizerTable Organizer basic information
     * @return affected row
     */
	public int updateOrganizer(OrganizerTable organizerTable);	
	
	/**
	 * Delete an organizer
	 * @param Organizer_ID
	 * @return affected row
	 */
	public int deleteOrganizer(int Organizer_ID);

	/**
     * Create an organizer address
     * @param address Organizer address information
     * @return affected row
     */
	public int createAddress(Address address);
	
	/**
	 * Get an organizer's address
	 * @param Organizer_ID
	 * @return Object
	 */
	public Object getAddressInfo(int Organizer_ID);
	
	/**
     * Update an organizer address
     * @param address Organizer address information
     * @return affected row
     */
	public int updateAddress(Address address);

	/**
     * Create an organizer contact
     * @param organizerContactTable Organizer contact information
     * @return affected row
     */
	public int createOrganizerContact(OrganizerContactTable organizerContactTable); 
	
	/**
	 * Get an organizer's contact
	 * @param Organizer_ID
	 * @return Object
	 */
	public Object getOrganizerContactInfo(int organizer_id); 
	
	/**
     * Update an organizer contact
     * @param organizerContactTable Organizer contact information
     * @return affected row
     */
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable); 
	
	/**
     * Organizer login: determine if email and password match in database
     * @param OEmail: Organizer email which is used to login
	 * @param oPwd: Organizer input password
     * @return whether oEmail and oPwd match
     */
	public boolean isMatching(String OEmail, String oPwd);
	
	public boolean hasRegistered(String organizerEmail);
	public void sendGreetMail(String to, String subject, String body, String oToken);
	//Organizer forget password section
	public boolean isValidOrganizer(String organizerEmail);
	public boolean hasForgetenPWD(String organizerEmail);
	public int saveTokenInfo( String oEmail, String oToken, Timestamp oExpiryDate);
	public void sendPwdMail(String to, String subject, String body, String oToken);
	public String generateToken();
	public boolean validToken(String oToken);
	public int updatePassword(String oEmail, String newpwd);
	public Object findEmailByToken(String oToken);
	public int updateToken(String oEmail, String oToken, Timestamp expireDate);
}

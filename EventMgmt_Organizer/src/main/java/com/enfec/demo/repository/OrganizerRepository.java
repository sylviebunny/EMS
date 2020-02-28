package com.enfec.demo.repository;

import java.sql.Timestamp;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.OrganizerContactTable;
import com.enfec.demo.model.OrganizerTable;

public interface OrganizerRepository {
	
	public int createOrganizer(OrganizerTable organizerTable);	
	public Object getOrganizerInfo(int Organizer_ID);
	public int updateOrganizer(OrganizerTable organizerTable);	
	public int deleteOrganizer(int Organizer_ID);

	public int createAddress(Address address);
	public Object getAddressInfo(int Organizer_ID);
	public int updateAddress(Address address);

	public int createOrganizerContact(OrganizerContactTable organizerContactTable); 
	public Object getOrganizerContactInfo(int organizer_id); 
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable); 
	
	public boolean isMatching(String OEmail, String oPwd);
	
	//ADD NEW
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

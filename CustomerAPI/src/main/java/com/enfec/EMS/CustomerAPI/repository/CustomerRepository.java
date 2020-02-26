package com.enfec.EMS.CustomerAPI.repository;

import java.sql.Timestamp;

import com.enfec.EMS.CustomerAPI.model.CustomerTable;

public interface CustomerRepository {
	public Object getCustomer(String id);
	public int registerCustomer(CustomerTable customerTable);
	public int updateCustomer(CustomerTable customerTable);
	public int deleteCustomer(String id);
	public boolean isMatching(String cEmail, String cPwd);
	public boolean hasRegistered(String customerEmail);
	public void sendGreetMail(String to, String subject, String body);
	
	//Customer forget password section
	
	public boolean isValidCustomer(String customerEmail);
	public boolean hasForgetenPWD(String customerEmail);
	public int saveTokenInfo( String cEmail, String cToken, Timestamp cExpiryDate);
	public void sendPwdMail(String to, String subject, String body, String CToken);
	public String generateToken();
	public boolean validToken(String CToken);
	public int updatePassword(String cEmail, String newpwd);
	public Object findEmailByToken(String CToken);
	public int updateToken(String cEmail, String cToken, Timestamp expireDate);
}

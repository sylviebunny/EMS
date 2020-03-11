package com.enfec.EMS.CustomerAPI.repository;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.EMS.CustomerAPI.model.CustomerRowmapper;
import com.enfec.EMS.CustomerAPI.model.CustomerTable;
import com.enfec.EMS.CustomerAPI.model.CustomerTokenTable;
import com.enfec.EMS.CustomerAPI.model.CustomerTokenRowmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/************************************************
* Author: Chad Chai
* Assignment: Implement CRUD methods for Customer, Customer login, password handling
* Class: CustomerRepositoryImpl
************************************************/

@Component
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {
	private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoryImpl.class);
	
	
	/**
     * All the Sql statements to use in MySql database
     */
	final String SELECT_CUSTOMER = "SELECT Customer_ID, User_Name, Email_Address, CPassword, Phone FROM Customers WHERE Customer_ID =?";
	final String REGISTER_CUSTOMER = "INSERT INTO Customers(User_Name, Email_Address, CPassword, Phone) VALUES"
			+ "(:name, :email, :psw, :phone)";
	final String UPDATE_CUSTOMER_INFO_PREFIX = "UPDATE Customers SET ";
	final String UPDATE_CUSTOMER_INFO_SUFFIX = " WHERE Customer_ID =:id";
	
	final String DELETE_CUSTOMER = "DELETE FROM Customers WHERE Customer_ID =?";
	final String SELECT_PWD = "SELECT Customer_ID, User_Name, Email_Address, CPassword, Phone FROM Customers WHERE Email_Address =?";

	final String VALID_CUSTOMER = "SELECT Customer_ID, User_Name, Email_Address, CPassword, Phone FROM Customers WHERE Email_Address=?";
	final String CREATE_TOKEN = "INSERT INTO Customer_Token(CEmail, CToken, CTExpire) VALUE (:customerEmail, :customerToken, :customerExpiryDate)";
	final String FIND_EMAIL_BY_TOKEN = "SELECT * FROM Customer_Token WHERE CToken=?";
	final String VALID_TOKEN = "SELECT * FROM Customer_Token WHERE CToken=?";
	final String UPDATE_PASSWORD = "UPDATE Customers SET CPassword =:psw WHERE Email_Address =:email";
	final String HAS_FORGET_PWD = "SELECT * FROM Customer_Token WHERE CEmail=?";
	final String UPDATE_TOKEN = "UPDATE Customer_Token SET CToken =:customerToken, CTExpire =:customerExpiryDate WHERE CEmail =:customerEmail";
	final String UPDATE_TOKEN_STATUS = "UPDATE Customer_Token SET Checked =:hasChecked WHERE CToken =:customerToken";

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	/**
	 * For create and update with 'Customers' table in database 
	 * Mapping customer information between URL body information and database variable attributes
	 * @param customerTable: customer's information used for create or update
	 * @return Map<String, Object>: contains variable and it's corresponding information
	 */
	public Map<String, Object> CustomerMap(CustomerTable customerTable) {
		Map<String, Object> cstmMap = new HashMap<>();
		cstmMap.put("id", customerTable.getId());
		cstmMap.put("name", customerTable.getName() == null || customerTable.getName().isEmpty() ? null : customerTable.getName());
		cstmMap.put("email", customerTable.getEmail() == null || customerTable.getEmail().isEmpty() ? null : customerTable.getEmail());
		cstmMap.put("psw", customerTable.getPsw() == null || customerTable.getPsw().isEmpty() ? null
				: Base64.getEncoder().encode((customerTable.getPsw().getBytes())));
		cstmMap.put("phone", customerTable.getPhone() == null || customerTable.getPhone().isEmpty() ? null : customerTable.getPhone());

		return cstmMap;
	}


	/**
	 * For create and update with 'Customer_Token' table in database 
	 * Mapping customer token information between URL body information and database variable attributes
	 * @param customerTokenTable: customer token's information used for create or update
	 * @return Map<String, Object>: contains variable and it's corresponding information
	 */
	private Map<String, Object> CustomerTokenMap(CustomerTokenTable customerTokenTable) {
		Map<String, Object> cfgpwdMap = new HashMap<>();
		cfgpwdMap.put("customerTokenID", customerTokenTable.getCustomerTokenID());
		cfgpwdMap.put("customerEmail", customerTokenTable.getCustomerEmail());
		cfgpwdMap.put("customerToken", customerTokenTable.getCustomerToken());
		cfgpwdMap.put("customerExpiryDate", customerTokenTable.getCustomerExpiryDate());
		cfgpwdMap.put("hasChecked", customerTokenTable.getHasChecked());

		return cfgpwdMap;
	}
	
	
	/**
     * Get Customer basic information from database by customer id
     * @param id
     * @return List<CustomerTable>: all entries that match the request
     */
	@Override
	public List<CustomerTable> getCustomer(String id) {
		return jdbcTemplate.query(SELECT_CUSTOMER, new Object[] { id }, new CustomerRowmapper());
	}
	
	
	/**
     * Create customer basic information
     * Map customerTable table to MySql parameters, and insert into database
     * @param customerTable: The information that needs to be created
     * @return number of affected rows
     */
	@Override
	public int registerCustomer(CustomerTable customerTable) {
		int affectedRow;
		Map<String, Object> cstmMap = CustomerMap(customerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(cstmMap);
		logger.info("Create customer info: {}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_CUSTOMER, parameterSource);
		return affectedRow;

	}
	
	
	/**
     * Update customer basic information
     * Map customer table to MySql parameters, and update database
     * @param customerTable: The information that needs to be updated. 
     * @return number of affected rows
     */
	@Override
	public int updateCustomer(CustomerTable customerTable) {
		int affectedRow;
		Map<String, Object> cstmMap = CustomerMap(customerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(cstmMap);
		StringBuilder UPDATE_CUSTOMER_INFO = new StringBuilder();
		if(cstmMap.get("name") != null) {
				UPDATE_CUSTOMER_INFO.append("User_Name" + "=:" + "name" + ",");
			}
		if(cstmMap.get("email") != null) {
			UPDATE_CUSTOMER_INFO.append("Email_Address" + "=:" + "email" + ",");
		}
		if(cstmMap.get("psw") != null) {
			UPDATE_CUSTOMER_INFO.append("CPassword" + "=:" + "psw" + ",");
		}
		if(cstmMap.get("phone") != null) {
			UPDATE_CUSTOMER_INFO.append("Phone" + "=:" + "phone" + ",");
		}
		
		UPDATE_CUSTOMER_INFO = UPDATE_CUSTOMER_INFO.deleteCharAt(UPDATE_CUSTOMER_INFO.length() - 1);//To delete the , at the end of the string
		String UPDATE_CUSTOMER = UPDATE_CUSTOMER_INFO_PREFIX + UPDATE_CUSTOMER_INFO + UPDATE_CUSTOMER_INFO_SUFFIX;
		logger.info("Update customer info:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_CUSTOMER, parameterSource);

		return affectedRow;

	}
	
	
	/**
     * Delete the customer information from database by customer id
     * @param id
     * @return number of affected rows
     */
	@Override
	public int deleteCustomer(String id) {
		int affectedRow = jdbcTemplate.update(DELETE_CUSTOMER, id);
		return affectedRow;

	}
	
	
	/**
     * Customer login: determine if email and password match in database
     * @param cEmail: Customer email which is used to login
	 * @param cPwd: Customer input password
     * @return whether cEmail and cPwd match or not
     */
	@Override
	public boolean isMatching(String cEmail, String cPwd) {
		List<CustomerTable> cusPwd = jdbcTemplate.query(SELECT_PWD, new Object[] { cEmail }, new CustomerRowmapper());
		if (cusPwd.isEmpty()) {
			return false;
		}
		String eCpwd = Base64.getEncoder().encodeToString(cPwd.getBytes());
		if (cusPwd.get(0).getPsw().equals(eCpwd)) {
			return true;
		} else {
			return false;
		}

	}
	
	
	/**
     * Customer register: determine if the email exist in database
     * @param customerEmail: customer email which is used to register as a new customer
     * @return whether the customerEmail exist in database or not.
     */
	@Override
	public boolean hasRegistered(String customerEmail) {
		List<CustomerTable> hasRegis = jdbcTemplate.query(VALID_CUSTOMER, new Object[] { customerEmail },
				new CustomerRowmapper());
		if (hasRegis.isEmpty() || hasRegis.get(0).getEmail().isEmpty()) {
			logger.info("not register before: {}", customerEmail);
			return false;
		} else {
			logger.info("registed already");
			return true;
		}

	}
	
	
	/**
     * Customer register: send register confirmation email to customer
     * @param to: the email address of the customer
     * @param subject: the subject of the confirmation email
     * @param body: the detail of confirmation email
     * @param CToken: the OTP for confirmation email
     * @return null
     */
	@Override
	public void sendGreetMail(String to, String subject, String body, String CToken) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setText(body, true);// true indicate html
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
     * Customer forget password: determine if email is in customer table
     * @param customerEmail: customer email which is used to get back password
     * @return whether the email address is in the customer table or not
     */
	@Override
	public boolean isValidCustomer(String customerEmail) {
		List<CustomerTable> cusEmail = jdbcTemplate.query(VALID_CUSTOMER, new Object[] { customerEmail },
				new CustomerRowmapper());
		if (cusEmail.isEmpty() || cusEmail.get(0).getEmail().isEmpty()) {
			logger.info("not valid customer email: {}", customerEmail);
			return false;
		} else {
			logger.info("Valid customer email");
			return true;
		}

	}
	
	
	/**
     * Customer forget password: determine if email is in customer token table
     * @param customerEmail: customer email which is used to get back password
     * @return whether the email address is in the customer token table or not
     */
	@Override
	public boolean hasForgetenPWD(String customerEmail) {
		List<CustomerTokenTable> fgcusEmail = jdbcTemplate.query(HAS_FORGET_PWD, new Object[] { customerEmail },
				new CustomerTokenRowmapper());
		if (fgcusEmail.isEmpty() || fgcusEmail.get(0).getCustomerEmail().isEmpty()) {
			logger.info("has not forgetten before");
			return false;
		} else {
			logger.info("has forgetten before");
			return true;
		}

	}

	
	/**
     * Create customer basic information
     * Map customerTokenTable to MySql parameters, and insert into database
     * @param customerEmail: the email address of the customer
     * @param customerToken: the random generated OTP 
     * @param customerExpiryDate: the expire time of the cToken
     * @return number of affected rows
     */
	@Override
	public int saveTokenInfo(String customerEmail, String customerToken, Timestamp customerExpiryDate) {
		int affectedRow;

		CustomerTokenTable ct = new CustomerTokenTable();
		ct.setCustomerEmail(customerEmail);
		ct.setCustomerToken(customerToken);
		ct.setCustomerExpiryDate(customerExpiryDate);
		Map<String, Object> resMap = CustomerTokenMap(ct);
		SqlParameterSource parameterSource = new MapSqlParameterSource(resMap);
		logger.info("Create Reset Token info: {}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(CREATE_TOKEN, parameterSource);
		return affectedRow;
	}

	@Autowired
	private JavaMailSender mailSender;
	
	
	/**
     * Customer reset password: send reset password link to customer email
     * @param to: the email address of the customer
     * @param subject: the subject of the reset password email
     * @param body: the detail of reset password email
     * @param CToken: the OTP for reset password
     * @return null
     */
	@Override
	public void sendPwdMail(String to, String subject, String body, String CToken) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setText(body, true);// true indicate html
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	/**
     * Generate a random string as token
     */
	@Override
	public String generateToken() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder token = new StringBuilder(20);
		for (int i = 0; i < 20; i++) {
			int sequence = (int) (AlphaNumericString.length() * Math.random());
			token.append(AlphaNumericString.charAt(sequence));
		}

		return token.toString();

	}
	
	
	/**
     * Verify token: determine if the token is correct and not expire
     * @param CToken: customer token 
     * @return whether the customer token is in the token table and expired or not
     */
	@Override
	public boolean validToken(String CToken) {
		List<CustomerTokenTable> cusToken = jdbcTemplate.query(VALID_TOKEN, new Object[] { CToken },
				new CustomerTokenRowmapper());
		if (cusToken.isEmpty() || cusToken.get(0).getCustomerExpiryDate().equals(null)) {
			return false;
		} else {
			Timestamp ct = new Timestamp(System.currentTimeMillis() - CustomerTokenTable.getTokenExpiration());
			if (cusToken.get(0).getCustomerExpiryDate().before(ct)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	
	/**
     * Verify token: determine if the token is checked
     * @param CToken: customer token 
     * @return whether the customer token is checked or not
     */
	@Override
	public boolean hasChecked(String CToken) {
		List<CustomerTokenTable> cusToken = jdbcTemplate.query(VALID_TOKEN, new Object[] { CToken },
				new CustomerTokenRowmapper());
		if (cusToken.isEmpty() || cusToken.get(0).getHasChecked()==0) {
			return false;
		} else {
			return true;
		}
	}
	

	
	/**
     * Get customer basic information from database by customer token
     * @param CToken
     * @return List<CustomerTable>: all entries that match the request
     */
	@Override
	public List<CustomerTokenTable> findEmailByToken(String CToken) {
		return jdbcTemplate.query(FIND_EMAIL_BY_TOKEN, new Object[] { CToken }, new CustomerTokenRowmapper());
	}
	
	
	/**
     * Update customer password: save the new password to customer table
     * Map customer table to MySql parameters, and update database
     * @param cEmail: the email address of the customer
     * @param newpwd: the random generated OTP 
     * @return affected row
     */
	@Override
	public int updatePassword(String cEmail, String newpwd) {
		int affectedRow;
		CustomerTable customerTable = new CustomerTable();

		customerTable.setEmail(cEmail);
		logger.info(cEmail);
		customerTable.setPsw(newpwd);
		// customerTable.setPsw(Base64.getEncoder().encodeToString(newpwd.getBytes()));

		logger.info(newpwd);
		Map<String, Object> updatePwdMap = CustomerMap(customerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updatePwdMap);
		logger.info("Update customer info:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_PASSWORD, parameterSource);

		return affectedRow;

	}

	
	/**
     * Update customer token table if customer token is expired
     * Map customer token table to MySql parameters, and update database
     * @param cEmail: the email address of the customer
     * @param cToken: the random generated OTP 
     * @param expiryDate: the expire time of the cToken
     * @return affected row
     */
	@Override
	public int updateToken(String cEmail, String cToken, Timestamp expireDate) {
		int affectedRow;
		CustomerTokenTable ctt = new CustomerTokenTable();

		ctt.setCustomerEmail(cEmail);
		logger.info(cEmail);
		ctt.setCustomerToken(cToken);
		logger.info(cToken);
		ctt.setCustomerExpiryDate(expireDate);
		logger.info(expireDate.toString());

		Map<String, Object> updateTokenMap = CustomerTokenMap(ctt);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updateTokenMap);
		logger.info("Update customer token info:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_TOKEN, parameterSource);

		return affectedRow;
	}
	
	
	/**
	 * Update customer basic information, can update partial info
	 * @param CustomerTable. Customer id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@Override
	public int updateTokenStatus(String cToken) {
		int affectedRow;
		CustomerTokenTable ctt = new CustomerTokenTable();
		ctt.setCustomerToken(cToken);
		ctt.setHasChecked(1);
		logger.info("Customer token status change to: {}", ctt.getHasChecked());

		Map<String, Object> updateTokenMap = CustomerTokenMap(ctt);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updateTokenMap);
		logger.info("Update customer token status to :{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_TOKEN_STATUS, parameterSource);

		return affectedRow;
	}

}

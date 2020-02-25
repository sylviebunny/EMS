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

@Component
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {
	private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoryImpl.class);
	
	final String SELECT_CUSTOMER = "SELECT Customer_ID, User_Name, Email_Address, CPassword, Phone FROM Customers WHERE Customer_ID =?";
	final String REGISTER_CUSTOMER = "INSERT INTO Customers(User_Name, Email_Address, CPassword, Phone) VALUES"
			+ "(:name, :email, :psw, :phone)";
	final String UPDATE_CUSTOMER = "UPDATE Customers SET User_name =:name, Email_Address =:email, CPassword =:psw, Phone =:phone WHERE Customer_ID =:id";
	final String DELETE_CUSTOMER = "DELETE FROM Customers WHERE Customer_ID =?";
	final String SELECT_PWD = "SELECT Customer_ID, User_Name, Email_Address, CPassword, Phone FROM Customers WHERE Email_Address =?";
	
	
	//Customer Token SQL
	final String VALID_CUSTOMER = "SELECT Customer_ID, User_Name, Email_Address, CPassword, Phone FROM Customers WHERE Email_Address=?";
	final String CREATE_TOKEN = "INSERT INTO Customer_Token(CEmail, CToken, CTExpire) VALUE (:customerEmail, :customerToken, :customerExpiryDate)";
	final String FIND_EMAIL_BY_TOKEN = "SELECT * FROM Customer_Token WHERE CToken=?";
	final String VALID_TOKEN = "SELECT * FROM Customer_Token WHERE CToken=?";
	final String UPDATE_PASSWORD = "UPDATE Customers SET CPassword =:psw WHERE Email_Address =:email";
	final String HAS_FORGET_PWD = "SELECT * FROM Customer_Token WHERE CEmail=?";
	final String UPDATE_TOKEN = "UPDATE Customer_Token SET CToken =:customerToken, CTExpire =:customerExpiryDate WHERE CEmail =:customerEmail";

	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Map<String, Object>CustomerMap(CustomerTable customerTable){
		Map<String, Object>cstmMap = new HashMap<>();
		/*  //This section is to judge if the ID is null;
		if(customerTable.getId() != 0) {
			cstmMap.put("id", customerTable.getId());
		}else {
			logger.error("Customer ID is missing");
			throw new NullPointerException("Customer ID can not be null");
		}
		*/
		cstmMap.put("id", customerTable.getId());
		cstmMap.put("name", customerTable.getName() == null ? null: customerTable.getName());
		cstmMap.put("email", customerTable.getEmail());
		cstmMap.put("psw", customerTable.getPsw() == null ? null:Base64.getEncoder().encode((customerTable.getPsw().getBytes())));
		cstmMap.put("phone", customerTable.getPhone());
		
		return cstmMap;
	}
	
	
	//Customer Token Map
	private Map<String, Object>CustomerTokenMap(CustomerTokenTable customerTokenTable){
		Map<String, Object>cfgpwdMap = new HashMap<>();
		cfgpwdMap.put("customerTokenID", customerTokenTable.getCustomerTokenID());
		cfgpwdMap.put("customerEmail", customerTokenTable.getCustomerEmail());
		cfgpwdMap.put("customerToken", customerTokenTable.getCustomerToken());
		cfgpwdMap.put("customerExpiryDate", customerTokenTable.getCustomerExpiryDate());

		return cfgpwdMap;
	}
	
	

	@Override
	public List<CustomerTable>getCustomer(String id){
		return jdbcTemplate.query(SELECT_CUSTOMER, new Object[] {id}, new CustomerRowmapper());
	}
	
	@Override
	public int registerCustomer(CustomerTable customerTable) {
		int affectedRow;
		Map<String, Object>cstmMap = CustomerMap(customerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(cstmMap);
		logger.info("Create customer info: {}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(REGISTER_CUSTOMER, parameterSource);
		return affectedRow;
		
	}
	
	
	
	@Override
	public int updateCustomer(CustomerTable customerTable) {
		int affectedRow;
		Map<String, Object>cstmMap = CustomerMap(customerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(cstmMap);
		logger.info("Update customer info:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_CUSTOMER, parameterSource);
		
		return affectedRow;
		
	}
	
	
	@Override
	public int deleteCustomer(String id) {
		int affectedRow = jdbcTemplate.update(DELETE_CUSTOMER, id);
		return affectedRow;

	}
	
	
	@Override
	public boolean isMatching(String cEmail, String cPwd){
		List<CustomerTable> cusPwd = jdbcTemplate.query(SELECT_PWD, new Object[] {cEmail}, new CustomerRowmapper());
		if(cusPwd.isEmpty()) {
			return false;
		}
		String eCpwd = Base64.getEncoder().encodeToString(cPwd.getBytes());
		if(cusPwd.get(0).getPsw().equals(eCpwd)) {
			return true;
		}else {
			return false;
		}
		
	}
		
	
	/*
	 * Customer Forget Password section
	 * 
	 * */
	
	@Override
	public boolean isValidCustomer(String customerEmail){
		List<CustomerTable> cusEmail = jdbcTemplate.query(VALID_CUSTOMER, new Object[] {customerEmail}, new CustomerRowmapper());
		if(cusEmail.isEmpty() || cusEmail.get(0).getEmail().isEmpty()) {
			logger.info("not valid customer email: {}", customerEmail);
			return false;
		}else {
			logger.info("Valid customer email");
			return true;
		}
		
	}
	
	
	@Override
	public boolean hasForgetenPWD(String customerEmail){
		List<CustomerTokenTable> fgcusEmail = jdbcTemplate.query(HAS_FORGET_PWD, new Object[] {customerEmail}, new CustomerTokenRowmapper());
		if(fgcusEmail.isEmpty() || fgcusEmail.get(0).getCustomerEmail().isEmpty()) {
			logger.info("has not forgetten before");
			return false;
		}else {
			logger.info("has forgetten before");
			return true;
		}
		
	}
	
	@Override
	public int saveTokenInfo(String customerEmail, String customerToken, Timestamp customerExpiryDate) {
		int affectedRow;
		
		CustomerTokenTable ct = new CustomerTokenTable(); 
		ct.setCustomerEmail(customerEmail);
		ct.setCustomerToken(customerToken);
		ct.setCustomerExpiryDate(customerExpiryDate);
		Map<String, Object>resMap = CustomerTokenMap(ct);
		SqlParameterSource parameterSource = new MapSqlParameterSource(resMap);
		logger.info("Create Reset Token info: {}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(CREATE_TOKEN, parameterSource);
		return affectedRow;
	}
	
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public void sendMail(String to, String subject, String body, String CToken) {
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
	
	@Override
	public String generateToken() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz" + "!@#$%^&*()_+";
		StringBuilder token = new StringBuilder(20);
		for (int i = 0; i < 20; i++) {
			 int sequence = (int)(AlphaNumericString.length() * Math.random()); 
			 token.append(AlphaNumericString.charAt(sequence));
			}

		return token.toString();
		
		}
	
	@Override
	public boolean validToken(String CToken) {
		List<CustomerTokenTable> cusToken = jdbcTemplate.query(VALID_TOKEN, new Object[] {CToken}, new CustomerTokenRowmapper());
		if(cusToken.isEmpty() || cusToken.get(0).getCustomerExpiryDate().equals(null)) {
			return false;
		}else {
			Timestamp ct = new Timestamp(System.currentTimeMillis()-CustomerTokenTable.getTokenExpiration());
			if(cusToken.get(0).getCustomerExpiryDate().before(ct)) {
				return false;
			}else {
				return true;
			}
		}
	}
	
	@Override
	public List<CustomerTokenTable> findEmailByToken(String CToken) {
		return jdbcTemplate.query(FIND_EMAIL_BY_TOKEN, new Object[] {CToken}, new CustomerTokenRowmapper());
	}
	
	
	@Override
	public int updatePassword(String cEmail, String newpwd) {
		int affectedRow;
		CustomerTable customerTable = new CustomerTable();
		
		customerTable.setEmail(cEmail);
		logger.info(cEmail);
		customerTable.setPsw(newpwd);
		//customerTable.setPsw(Base64.getEncoder().encodeToString(newpwd.getBytes()));

		logger.info(newpwd);
		Map<String, Object>updatePwdMap = CustomerMap(customerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updatePwdMap);
		logger.info("Update customer info:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_PASSWORD, parameterSource);
		
		return affectedRow;
		
	}
	
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

		Map<String, Object>updateTokenMap = CustomerTokenMap(ctt);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updateTokenMap);
		logger.info("Update customer token info:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_TOKEN, parameterSource);
		
		return affectedRow;
	}



}

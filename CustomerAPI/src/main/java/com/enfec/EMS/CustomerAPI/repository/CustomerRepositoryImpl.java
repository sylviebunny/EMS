package com.enfec.EMS.CustomerAPI.repository;


import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.EMS.CustomerAPI.model.CustomerRowmapper;
import com.enfec.EMS.CustomerAPI.model.CustomerTable;

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
		
	
	
	

}

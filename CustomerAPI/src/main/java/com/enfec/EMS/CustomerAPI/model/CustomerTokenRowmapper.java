package com.enfec.EMS.CustomerAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/************************************************
* Author: Chad Chai
* Assignment: Customer Token Rowmapper for 'Customer_Token' table
* Class: CustomerTokenRowmapper
************************************************/

public class CustomerTokenRowmapper implements RowMapper<CustomerTokenTable> {
	/**
     * Map each MySql column's content to customer token table
     * 
     * @param rs: ResultSet
     * @param rowNum
     * @throws SQLException when column doesn't exist in database 
     * @return Customer Token Table
     */
	
	@Override
	public CustomerTokenTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		CustomerTokenTable cTokenTable = new CustomerTokenTable();
		cTokenTable.setCustomerTokenID(rs.getInt("CT_ID"));
		cTokenTable.setCustomerEmail(rs.getString("CEmail"));
		cTokenTable.setCustomerToken(rs.getString("CToken"));
		cTokenTable.setCustomerExpiryDate(rs.getTimestamp("CTExpire"));

		return cTokenTable;
	}

}

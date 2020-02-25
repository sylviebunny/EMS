package com.enfec.EMS.CustomerAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class CustomerTokenRowmapper implements RowMapper<CustomerTokenTable>{
	@Override
	public CustomerTokenTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		CustomerTokenTable cTokenTable = new CustomerTokenTable();
		cTokenTable.setCustomerTokenID(rs.getInt("CT_ID"));
		cTokenTable.setCustomerEmail(rs.getString("CEmail"));
		cTokenTable.setCustomerToken(rs.getString("CToken"));
		cTokenTable.setCustomerExpiryDate(rs.getTimestamp("CTExpire"));
		
		return cTokenTable;
	}

}

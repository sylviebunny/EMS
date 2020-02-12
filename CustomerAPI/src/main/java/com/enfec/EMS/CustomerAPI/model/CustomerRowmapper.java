package com.enfec.EMS.CustomerAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class CustomerRowmapper implements RowMapper<CustomerTable>  {
	@Override
	public CustomerTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		CustomerTable customerTable = new CustomerTable();
		customerTable.setId(rs.getInt("Customer_ID"));
		customerTable.setName(rs.getString("User_Name"));
		customerTable.setEmail(rs.getString("Email_Address"));
		customerTable.setPsw(rs.getString("CPassword") == null ? null: rs.getString("CPassword"));
		customerTable.setPhone(rs.getString("Phone"));
		return customerTable;
	}

}

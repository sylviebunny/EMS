package com.enfec.EMS.CustomerOrderAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class CustomerOrderRowmapper implements RowMapper<CustomerOrderTable>  {
	@Override
	public CustomerOrderTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		CustomerOrderTable customerOederTable = new CustomerOrderTable();
		customerOederTable.setCustomerOrderID(rs.getInt("COrder_ID"));
		customerOederTable.setCustomerID(rs.getInt("Customer_ID"));
		customerOederTable.setOrderTime(rs.getTimestamp("OrderCreateTime"));
		
		return customerOederTable;
	}
	

}

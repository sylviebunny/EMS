package com.enfec.EMS.CustomerOrderAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/************************************************
* Author: Chad Chai
* Assignment: Customer Order Rowmapper for 'Customer_Orders' table
* Class: CustomerOrderRowmapper
************************************************/
public class CustomerOrderRowmapper implements RowMapper<CustomerOrderTable>  {
	/**
     * Map each MySql column's content to customer order table
     * @param rs: ResultSet
     * @param rowNum
     * @throws SQLException when column doesn't exist in database 
     * @return CustomerOrderTable
     */
	@Override
	public CustomerOrderTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		CustomerOrderTable customerOederTable = new CustomerOrderTable();
		customerOederTable.setCustomerOrderID(rs.getInt("COrder_ID"));
		customerOederTable.setCustomerID(rs.getInt("Customer_ID"));
		customerOederTable.setOrderTime(rs.getTimestamp("OrderCreateTime"));
		customerOederTable.setOrderStatus(rs.getString("Order_Status"));
		customerOederTable.setStripeStatus(rs.getString("Stripe_Status"));
		customerOederTable.setStripeChargeID(rs.getString("Stripe_Charge_ID"));
		return customerOederTable;
	}
	

}

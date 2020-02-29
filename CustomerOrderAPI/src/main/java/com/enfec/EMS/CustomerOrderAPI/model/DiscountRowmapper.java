package com.enfec.EMS.CustomerOrderAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/************************************************
* Author: Chad Chai
* Assignment: Discount Rowmapper for 'Discounts' table
* Class: DiscountRowmapper
************************************************/
public class DiscountRowmapper implements RowMapper<DiscountTable>  {
	
	/**
     * Map each MySql column's content to discount table
     * @param rs: ResultSet
     * @param rowNum
     * @throws SQLException when column doesn't exist in database 
     * @return DiscountTable
     */
	@Override
	public DiscountTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		DiscountTable discountTable = new DiscountTable();
		discountTable.setDiscountID(rs.getInt("Discount_ID"));
		discountTable.setDiscountName(rs.getString("Discount_Name"));
		discountTable.setPercentageOff(rs.getDouble("Percentage_Off"));
		
		return discountTable;
	}
	

}

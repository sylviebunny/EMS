package com.enfec.EMS.CustomerOrderAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class DiscountRowmapper implements RowMapper<DiscountTable>  {
	@Override
	public DiscountTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		DiscountTable discountTable = new DiscountTable();
		discountTable.setDiscountID(rs.getInt("Discount_ID"));
		discountTable.setDiscountName(rs.getString("Discount_Name"));
		discountTable.setPercentageOff(rs.getDouble("Percentage_Off"));
		
		return discountTable;
	}
	

}

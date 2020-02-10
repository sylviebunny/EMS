package com.enfec.sb.refundapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;


public class OOrderRefundRowmapper implements RowMapper<OOrderRefundTable> {


	
	
	@Override
	public OOrderRefundTable mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OOrderRefundTable oot = new OOrderRefundTable();
		  
		oot.setRefund_id(rs.getInt("Refund_ID"));
		oot.setOorder_id(rs.getInt("OOrder_ID"));
		oot.setDescription(rs.getString("Description"));
		oot.setRefund_created_time(rs.getTimestamp("Refund_Created_Time"));
		oot.setRefund_status(rs.getString("Refund_Status"));
		
		return oot;
	}
	
	
	
}


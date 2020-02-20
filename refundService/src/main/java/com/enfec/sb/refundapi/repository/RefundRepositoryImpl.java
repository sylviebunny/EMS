package com.enfec.sb.refundapi.repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.sb.refundapi.model.COrderRefundRowmapper;
import com.enfec.sb.refundapi.model.COrderRefundTable;
import com.enfec.sb.refundapi.model.OOrderRefundRowmapper;
import com.enfec.sb.refundapi.model.OOrderRefundTable;

@Component
@Transactional
public class RefundRepositoryImpl implements RefundRepository {
	private static final Logger logger = LoggerFactory.getLogger(RefundRepositoryImpl.class);

	private static final String DELETE_ORGANIZER_REFUND = "DELETE FROM Refund WHERE Refund_ID =?";

	private static final String SELECT_ORGANIZER_REFUND = "SELECT * FROM Refund WHERE Refund_ID =?";

	private static final String CREATE_ORGANIZER_REFUND = "INSERT INTO Refund (OOrder_ID, Description, Refund_Updated_Time, Refund_Status)"
			+ "VALUES(:oorder_id, :description, :refund_updated_time, :refund_status)";

	private static final String UPDATE_ORGANIZER_REFUND = "UPDATE evntmgmt_usa.Refund SET Refund.Refund_Status = :refund_status, "
			+ "Refund.Description = :description, Refund_Updated_Time = :refund_updated_time WHERE Refund.Refund_ID = :refund_id;";

	private static final String SELECT_ORGANIZER_REFUND_BY_OORDER_ID = "SELECT * FROM Refund WHERE OOrder_ID =?";

	private static final String CREATE_CUSTOMER_REFUND = "INSERT INTO Customer_Refund (COrder_ID, CRefund_Description, CRefund_Updated_Time, CRefund_Status)"
			+ "VALUES(:corder_id, :crefund_description, :crefund_updated_time, :crefund_status)";

	private static final String DELETE_CUSTOMER_REFUND = "DELETE FROM Customer_Refund WHERE CRefund_ID =?";

	private static final String SELECT_CUSTOMER_REFUND_BY_CREFUND_ID = "SELECT * FROM Customer_Refund WHERE CRefund_ID =?";

	private static final String SELECT_CUSTOMER_REFUND_BY_CORDER_ID = "SELECT * FROM Customer_Refund WHERE COrder_ID =?";

	private static final String UPDATE_CUSTOMER_REFUND = "UPDATE Customer_Refund SET CRefund_Status = :crefund_status, "
			+ "CRefund_Description = :crefund_description, CRefund_Updated_Time = :crefund_updated_time WHERE CRefund_ID = :crefund_id;";
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<OOrderRefundTable> getOrganizerRefundByRefundID(int refund_id) {
		// get organizer's refund information by refund_id
		return jdbcTemplate.query(SELECT_ORGANIZER_REFUND, new Object[] {refund_id}, new OOrderRefundRowmapper());
	}
	
	@Override
	public List<OOrderRefundTable> getOrganizerRefundByOorderID(int oorder_id) {
		// get organizer's refund information by oorder_id
		return jdbcTemplate.query(SELECT_ORGANIZER_REFUND_BY_OORDER_ID, new Object[] {oorder_id}, new OOrderRefundRowmapper());
	}
	
	@Override
	public int createOrganizerRefund (OOrderRefundTable organizerRefundTable) {
		// Create an organizer refund
		int affectedRow;
		Map<String, Object> param = getOrganizerRefundMap(organizerRefundTable, Integer.MIN_VALUE);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(CREATE_ORGANIZER_REFUND, pramSource);
		
		return affectedRow; 
	}
	
	@Override
	public int updateOrganizerRefund(OOrderRefundTable organizerRefundTable) {
		/*
		 *  Update an organizer refund by assigning specific organizer_refund_id and 
		 *  providing status and description
		 */
		int affectedRow;
		Map<String, Object> param = getOrganizerRefundMap(organizerRefundTable, organizerRefundTable.getRefund_id());
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_REFUND, pramSource);
		
		return affectedRow; 
		
	}
	
	@Override
	public int deleteOrganizerRefund(int refund_id) {
		// Delete an organizer refund by refund_id
		List<OOrderRefundTable> et = getOrganizerRefundByRefundID(refund_id); 
		
		if (et == null) {
			// Didn't find this organizer refund; 
			return Integer.MIN_VALUE; 
		} else {
			int affectedRow = jdbcTemplate.update(DELETE_ORGANIZER_REFUND, refund_id);
			return affectedRow; 
		}
	}

	private Map<String, Object> getOrganizerRefundMap(OOrderRefundTable organizerRefund, Integer refund_id) {
		// Mapping organizer refund's information variables from JSON body to entity variables 
		Map<String, Object>param = new HashMap<>();
		
		if (refund_id != null && refund_id != Integer.MIN_VALUE) {
			// Means we need to update an organizer refund
			param.put("refund_id", refund_id); 
		}
		
		// oorder_id cannot be null, must give oorder_id
		param.put("oorder_id", organizerRefund.getOorder_id());
		
		param.put("description", organizerRefund.getDescription() == null || organizerRefund.getDescription().length() == 0 ? 
				null : organizerRefund.getDescription()); 
		
		// Default time is current time based on machine 
		Date date = new Date(); 
		param.put("refund_updated_time", organizerRefund.getRefund_updated_time() == null ?
				new Timestamp(date.getTime()) : organizerRefund.getRefund_updated_time()); 
		
		// Default status is created 
		param.put("refund_status", organizerRefund.getRefund_status() == null ? 
				new String("Created") : organizerRefund.getRefund_status());
		
		return param;
	}
/*
 * ---------------------------------------------------------------------------------------------
 * 				Below are APIs for customer orders
 * ---------------------------------------------------------------------------------------------
 */
	@Override
	public int createCustomerRefund(COrderRefundTable customerRefundTable) {
		// Create a customer refund
		int affectedRow;
		Map<String, Object> param = getCustomerRefundMap(customerRefundTable, Integer.MIN_VALUE);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(CREATE_CUSTOMER_REFUND, pramSource);
		
		return affectedRow; 
	}

	private Map<String, Object> getCustomerRefundMap(COrderRefundTable customerRefund, Integer crefund_id) {
		// Get customer order refund info mapping
		Map<String, Object> param = new HashMap<>();
		
		if (crefund_id != null && crefund_id != Integer.MIN_VALUE) {
			// Means we need to update an customer refund
			param.put("crefund_id", crefund_id); 
		}
		
		// oorder_id cannot be null, Corder_id must be given
		param.put("corder_id", customerRefund.getCorder_id());
		
		param.put("crefund_description", customerRefund.getCrefund_description() == null || customerRefund.getCrefund_description().length() == 0 ? 
				null : customerRefund.getCrefund_description()); 
		
		// Default time is current time based on machine 
		Date date = new Date(); 
		param.put("crefund_updated_time", customerRefund.getCrefund_updated_time() == null ?
				new Timestamp(date.getTime()) : customerRefund.getCrefund_updated_time()); 
		
		// Default status is created 
		param.put("crefund_status", customerRefund.getCrefund_status() == null ? 
				new String("Created") : customerRefund.getCrefund_status());
		
		return param;
	}

	@Override
	public int deleteCustomerRefund(int crefund_id) {
		// Delete a customer refund by crefund_id
		List<COrderRefundTable> et = getCustomerRefundByCRefundID(crefund_id); 
		
		if (et == null) {
			// Didn't find this customer refund; 
			return Integer.MIN_VALUE; 
		} else {
			int affectedRow = jdbcTemplate.update(DELETE_CUSTOMER_REFUND, crefund_id);
			return affectedRow; 
		}
	}

	@Override
	public List<COrderRefundTable> getCustomerRefundByCRefundID(int crefund_id) {
		// Get customer's refund information by crefund_id
		return jdbcTemplate.query(SELECT_CUSTOMER_REFUND_BY_CREFUND_ID, new Object[] {crefund_id}, new COrderRefundRowmapper());
	}

	@Override
	public List<COrderRefundTable> getCustomerRefundByCorderID(int corder_id) {
		// Get customer's refund information by corder_id
		return jdbcTemplate.query(SELECT_CUSTOMER_REFUND_BY_CORDER_ID, new Object[] {corder_id}, new COrderRefundRowmapper());
	}

	@Override
	public int updateCustomerRefund(COrderRefundTable corganizerRefundTable) {
		/*
		 *  Update a customer refund by assigning specific customer_refund_id and 
		 *  providing status and description
		 */
		int affectedRow;
		Map<String, Object> param = getCustomerRefundMap(corganizerRefundTable, corganizerRefundTable.getCrefund_id());
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_CUSTOMER_REFUND, pramSource);
		
		return affectedRow; 
	}
	
}


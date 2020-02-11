package com.enfec.sb.refundapi.repository;

import java.util.List;

import com.enfec.sb.refundapi.model.COrderRefundTable;
import com.enfec.sb.refundapi.model.OOrderRefundTable;

public interface RefundRepository {
	
	// Organizer refund's APIs
	public int createOrganizerRefund(OOrderRefundTable organizerTable);
	public int deleteOrganizerRefund(int refund_id);
	public Object getOrganizerRefundByRefundID(int refund_id);
	public Object getOrganizerRefundByOorderID(int oorder_id);
	public int updateOrganizerRefund(OOrderRefundTable organizerRefundTable);
	
	// Customer refund's APIs
	public int createCustomerRefund(COrderRefundTable organizerTable);
	public int deleteCustomerRefund(int refund_id);
	public Object getCustomerRefundByCRefundID(int refund_id);
	public Object getCustomerRefundByCorderID(int oorder_id);
	public int updateCustomerRefund(COrderRefundTable organizerRefundTable);
	
	
}

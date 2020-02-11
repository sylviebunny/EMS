package com.enfec.sb.refundapi.repository;

import java.util.List;

import com.enfec.sb.refundapi.model.OOrderRefundTable;

public interface RefundRepository {
	
	public int createOrganizerRefund(OOrderRefundTable organizerTable);
	public int deleteOrganizerRefund(int refund_id);
	public Object getOrganizerRefundByRefundID(int refund_id);
	public Object getOrganizerRefundByOorderID(int oorder_id);
	public int updateOrganizerRefund(OOrderRefundTable organizerRefundTable);


	
}

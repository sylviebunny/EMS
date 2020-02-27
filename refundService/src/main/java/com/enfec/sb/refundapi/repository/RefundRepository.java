package com.enfec.sb.refundapi.repository;

import com.enfec.sb.refundapi.model.COrderRefundTable;
import com.enfec.sb.refundapi.model.OOrderRefundTable;

public interface RefundRepository {

    /**
     * Creates an organizer refund
     * @param organizerRefundTable - organizer order information
     * @return affected row
     */
    public int createOrganizerRefund(OOrderRefundTable organizerTable);

    /**
     * Deletes an organizer refund
     * @param refund_id - organizer refund id
     * @return affected row
     */
    public int deleteOrganizerRefund(int refund_id);

    /**
     * Gets an organizer's refund information by organizer refund id
     * @param refund_id - organizer refund_id
     * @return Object
     */
    public Object getOrganizerRefundByRefundID(int refund_id);

    /**
     * Gets an organizer's refund information by organizer order id
     * @param oorder_id - organizer order id
     * @return Object
     */
    public Object getOrganizerRefundByOorderID(int oorder_id);

    /**
     * Updates an organizer's refund information 
     * @param organizerRefundTable - organizer refund information 
     * @return affected row
     */
    public int updateOrganizerRefund(OOrderRefundTable organizerRefundTable);

    /**
     * Creates a customer refund
     * @param customerRefundTable - customer refund information 
     * @return affected row
     */
    public int createCustomerRefund(COrderRefundTable customerRefundTable);

    /**
     * Deletes a customer refund
     * @param customer_refund_id - customer refund id
     * @return affected row
     */
    public int deleteCustomerRefund(int refund_id);

    /**
     * Gets customer refund by customer refund id
     * @param customer_refund_id - customer refund id
     * @return Object
     */
    public Object getCustomerRefundByCRefundID(int refund_id);

    /**
     * Gets customer refund by customer order id
     * @param corder_id - customer order id
     * @return Object
     */
    public Object getCustomerRefundByCorderID(int corder_id);

    /**
     * Updates customer refund
     * @param customerRefundTable - customer refund information 
     * @return affected row
     */
    public int updateCustomerRefund(COrderRefundTable customerRefundTable);


}

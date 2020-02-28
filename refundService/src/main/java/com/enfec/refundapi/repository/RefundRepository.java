package com.enfec.refundapi.repository;

import com.enfec.refundapi.model.COrderRefundTable;
import com.enfec.refundapi.model.OOrderRefundTable;

/************************************************
*
* Author: Heidi Huo
* Assignment: Refund repository interface
* Interface: RefundRepository
*
************************************************/
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
     * @return all entries that match organizer refund id
     */
    public Object getOrganizerRefundByRefundID(int refund_id);

    /**
     * Gets an organizer's refund information by organizer order id
     * @param oorder_id - organizer order id
     * @return all entries that match organizer order id
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
     * @return all entries that match customer refund id
     */
    public Object getCustomerRefundByCRefundID(int refund_id);

    /**
     * Gets customer refund by customer order id
     * @param corder_id - customer order id
     * @return all entries that match customer order id
     */
    public Object getCustomerRefundByCorderID(int corder_id);

    /**
     * Updates customer refund
     * @param customerRefundTable - customer refund information 
     * @return affected row
     */
    public int updateCustomerRefund(COrderRefundTable customerRefundTable);


}

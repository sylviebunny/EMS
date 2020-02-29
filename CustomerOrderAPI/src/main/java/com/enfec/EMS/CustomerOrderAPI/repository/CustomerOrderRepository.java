package com.enfec.EMS.CustomerOrderAPI.repository;

import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderTable;
import com.enfec.EMS.CustomerOrderAPI.model.DiscountTable;
import com.enfec.EMS.CustomerOrderAPI.model.TicketTable;

/************************************************
* Author: Chad Chai
* Assignment: Interface for customer order API
* Interface: CustomerOrderRepository
************************************************/
public interface CustomerOrderRepository {
	/**
	 * Get the customer order basic information by customer order id
	 * @param customerOrderId: customer order id number
	 * @return Object
	 */
	public Object getCustomerOrder(String customerOrderID);
	
	
	/**
     * Create a customer order
     * @param customerOrderTable: Customer order basic information
     * @return affected row
     */
	public int createCustomerOrder(CustomerOrderTable customerOrderTable);
	
	
	/**
	 * Delete a customer order
	 * @param customerOrderID: customer order id number
	 * @return affected row
	 */
	public int deleteCustomerOrder(String customerOrderID);
	
	
	/**
	 * Get the ticket basic information by ticket id
	 * @param ticketID: ticket id number
	 * @return Object
	 */
	public Object getTicket(String ticketID);
	
	
	/**
	 * Get the ticket basic information by customer order id
	 * @param customerOrderID: customer order id number
	 * @return Object
	 */
	public Object getTicketByOrder(String customerOrderID);
	
	
	/**
     * Create a ticket
     * @param ticketTable: ticket basic information
     * @return affected row
     */
	public int createTicket(TicketTable ticketTable);
	
	
	/**
     * Update a ticket
     * @param ticketTable: ticket basic information
     * @return affected row
     */
	public int updateTicket(TicketTable ticketTable);
	
	
	/**
	 * Delete a ticket
	 * @param ticketID: ticket id number
	 * @return affected row
	 */
	public int deleteTicket(String ticketID);
	
	
	/**
	 * Get the original price information by seat id
	 * @param seatID: seat id number
	 * @return Object
	 */
	public Object getPrice(String seatID);
	
	
	/**
	 * Get the discount percentage information by seat id
	 * @param discountID: discount id number
	 * @return Object
	 */
	public Object getPercentageOff(String discountID);
	

	/**
	 * Get the discount information by discount id
	 * @param discountID: discount id number
	 * @return Object
	 */
	public Object getDiscount(String discountID);
	
	
	/**
     * Create a discount
     * @param discountTable: discount basic information
     * @return affected row
     */
	public int createDiscount(DiscountTable discountTable);
	
	
	/**
     * Update a discount
     * @param discountTable: discount basic information
     * @return affected row
     */
	public int updateDiscount(DiscountTable discountTable);
	
	
	/**
	 * Delete a discount
	 * @param dicountID: discount id number
	 * @return affected row
	 */
	public int deleteDiscount(String discountID);
	
}

package com.enfec.EMS.CustomerOrderAPI.repository;

import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderTable;
import com.enfec.EMS.CustomerOrderAPI.model.TicketTable;

public interface CustomerOrderRepository {
	//Customer order interface
	public Object getCustomerOrder(String customerOrderID);
	public int createCustomerOrder(CustomerOrderTable customerOrderTable);
	public int deleteCustomerOrder(String customerOrderID);
	
	
	//Ticket interface
	public Object getTicket(String ticketID);
	public Object getTicketByOrder(String customerOrderID);
	public int createTicket(TicketTable ticketTable);
	public int updateTicket(TicketTable ticketTable);
	public int deleteTicket(String ticketID);
	
}

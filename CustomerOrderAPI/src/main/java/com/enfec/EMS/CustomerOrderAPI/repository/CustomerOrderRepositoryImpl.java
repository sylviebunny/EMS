package com.enfec.EMS.CustomerOrderAPI.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderRowmapper;
import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderTable;
import com.enfec.EMS.CustomerOrderAPI.model.TicketRowmapper;
import com.enfec.EMS.CustomerOrderAPI.model.TicketTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomerOrderRepositoryImpl implements CustomerOrderRepository {
	private static final Logger logger = LoggerFactory.getLogger(CustomerOrderRepositoryImpl.class);
	
	//Customer Order
	final String SELECT_CUSTOMER_ORDER = "SELECT COrder_ID, Customer_ID, OrderCreateTime FROM Customer_Orders WHERE COrder_ID=?";
	final String CREATE_CUSTOMER_ORDER = "INSERT INTO Customer_Orders(Customer_ID,OrderCreateTime) VALUES (:customerID, :orderTime)";
	//final String UPDATE_CUSTOMER_ORDER = "UPDATE Customers SET User_name =:name, Email_Address =:email, CPassword =:psw, Phone =:phone WHERE Customer_ID =:id";
	final String DELETE_CUSTOMER_ORDER = "DELETE FROM Customer_Orders WHERE COrder_ID =?";
	
	//Customer Order
	final String SELECT_TICKET = "SELECT Ticket_ID, COrder_ID, Event_ID, Room_ID, Seat_ID, RealPrice FROM Tickets WHERE Ticket_ID=?";
	final String CREATE_TICKET = "INSERT INTO Tickets(COrder_ID, Event_ID, Room_ID, Seat_ID, RealPrice) VALUES (:customerOrderID, :eventID, :roomID, :seatID, :realPrice)";
	final String UPDATE_TICKET = "UPDATE Tickets SET Ticket_ID = :ticketID, COrder_ID = :customerOrderID, Event_ID = :eventID,"
			+ "Room_ID = :roomID, Seat_ID = :seatID, RealPrice = :realPrice WHERE Ticket_ID = :ticketID";
	final String DELETE_TICKET = "DELETE FROM Tickets WHERE Ticket_ID =?";

	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	//Customer Order Map
	private Map<String, Object>CustomerOrderMap(CustomerOrderTable customerOrderTable){
		Map<String, Object>cstmoMap = new HashMap<>();
		cstmoMap.put("customerOrderID", customerOrderTable.getCustomerOrderID());
		cstmoMap.put("customerID", customerOrderTable.getCustomerID());
		cstmoMap.put("orderTime", customerOrderTable.getOrderTime());

		return cstmoMap;
	}
	
	
	//Ticket Map
	private Map<String, Object>TicketMap(TicketTable ticketTable){
		Map<String, Object>ticketMap = new HashMap<>();
		
		ticketMap.put("ticketID", ticketTable.getTicketID());
		ticketMap.put("customerOrderID", ticketTable.getCustomerOrderID());
		ticketMap.put("eventID", ticketTable.getEventID());
		ticketMap.put("roomID", ticketTable.getRoomID());
		ticketMap.put("seatID", ticketTable.getSeatID());
		//ticketMap.put("categoryID", ticketTable.getCategoryID());
		ticketMap.put("realPrice", ticketTable.getRealPrice());
		
		return ticketMap;
	}
	
	
	//Customer order override function
	@Override
	public List<CustomerOrderTable>getCustomerOrder(String customerOrderID){
		return jdbcTemplate.query(SELECT_CUSTOMER_ORDER, new Object[] {customerOrderID}, new CustomerOrderRowmapper());
	}
	
	
	@Override
	public int createCustomerOrder(CustomerOrderTable customerOrderTable) {
		int affectedRow;
		Map<String, Object> param = CustomerOrderMap(customerOrderTable);
		
		SqlParameterSource paramSource = new MapSqlParameterSource(param);
		logger.info("Create Customer Order : {} ",paramSource);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_CUSTOMER_ORDER, paramSource);
		
		return affectedRow;
	}
	
	@Override
	public int deleteCustomerOrder(String customerOrderID) {
		int affectedRow = jdbcTemplate.update(DELETE_CUSTOMER_ORDER,customerOrderID);
		return affectedRow;
	}
	
	//Tickets override function
	
	@Override
	public List<TicketTable> getTicket(String ticketID){
		return jdbcTemplate.query(SELECT_TICKET, new Object[] {ticketID}, new TicketRowmapper());
	}

	@Override
	public int createTicket(TicketTable ticketTable) {
		int affectedRow;
		Map<String, Object> param = TicketMap(ticketTable);
		
		SqlParameterSource paramSource = new MapSqlParameterSource(param);
		logger.info("Create ticket : {} ",paramSource);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_TICKET, paramSource);
		
		return affectedRow;
	}
	
	@Override
	public int updateTicket(TicketTable ticketTable) {
		int affectedRow;
		Map<String, Object> param = TicketMap(ticketTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating ticket : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_TICKET, pramSource);
		
		return affectedRow;

	}
	
	@Override
	public int deleteTicket(String ticketID) {
		int affectedRow = jdbcTemplate.update(DELETE_TICKET,ticketID);
		return affectedRow;

	}
	
	

}

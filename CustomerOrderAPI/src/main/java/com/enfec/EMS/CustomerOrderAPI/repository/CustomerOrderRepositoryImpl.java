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
import org.springframework.transaction.annotation.Transactional;

import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderRowmapper;
import com.enfec.EMS.CustomerOrderAPI.model.CustomerOrderTable;
import com.enfec.EMS.CustomerOrderAPI.model.DiscountRowmapper;
import com.enfec.EMS.CustomerOrderAPI.model.DiscountTable;
import com.enfec.EMS.CustomerOrderAPI.model.TicketRowmapper;
import com.enfec.EMS.CustomerOrderAPI.model.TicketTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Transactional
public class CustomerOrderRepositoryImpl implements CustomerOrderRepository {
	private static final Logger logger = LoggerFactory.getLogger(CustomerOrderRepositoryImpl.class);
	
	//Customer Order SQL
	final String SELECT_CUSTOMER_ORDER = "SELECT COrder_ID, Customer_ID, OrderCreateTime FROM Customer_Orders WHERE COrder_ID=?";
	final String CREATE_CUSTOMER_ORDER = "INSERT INTO Customer_Orders(Customer_ID) VALUES (:customerID)";
	final String DELETE_CUSTOMER_ORDER = "DELETE FROM Customer_Orders WHERE COrder_ID =?";
	
	//Tickets SQL
	final String SELECT_TICKET = "SELECT Ticket_ID, COrder_ID, Event_ID, Tickets.Room_ID, Tickets.Seat_ID, Discount_Type, RealPrice, Price, Percentage_Off "
			+ "FROM Tickets, Seats, Seat_Category, Discounts WHERE Tickets.Seat_ID=Seats.Seat_ID "
			+ "AND Seats.Category_ID = Seat_Category.Category_ID AND Tickets.Discount_Type = Discounts.Discount_ID AND Ticket_ID =?";
	
	
	final String CREATE_TICKET = "INSERT INTO Tickets(COrder_ID, Event_ID, Room_ID, Seat_ID, Discount_Type, RealPrice) VALUES (:customerOrderID, :eventID, :roomID, :seatID, :discountType, :realPrice)";
	final String UPDATE_TICKET = "UPDATE Tickets SET RealPrice = :realPrice WHERE Ticket_ID = :ticketID";
	final String DELETE_TICKET = "DELETE FROM Tickets WHERE Ticket_ID =?";
	final String SELECT_TICKET_BY_ORDER = "SELECT Ticket_ID, COrder_ID, Event_ID, Room_ID, Seat_ID, RealPrice FROM Tickets WHERE COrder_ID=?";
	final String SELECT_PRICE = "SELECT Price FROM Seat_Category WHERE Category_ID=(SELECT category_id FROM Seats WHERE Seat_ID = ?)";
	final String SELECT_PERCENTAGE_OFF = "SELECT Percentage_Off FROM Discounts, Tickets WHERE Discount_ID = ?";
	
	//Discount SQL
	final String SELECT_DISCOUNT = "SELECT * FROM Discounts WHERE Discount_ID=?";
	final String CREATE_DISCOUNT = "INSERT INTO Discounts(Discount_Name, Percentage_Off) VALUES (:discountName, :percentageOff)";
	final String UPDATE_DISCOUNT = "UPDATE Discounts SET Discount_Name = :discountName, Percentage_Off = :percentageOff WHERE Discount_ID=:discountID";
	final String DELETE_DISCOUNT = "DELETE FROM Discounts WHERE Discount_ID =?";

	
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
		ticketMap.put("discountType", ticketTable.getDiscountType());
		
		
		if(ticketTable.getSeatID() != 0) {
			ticketMap.put("originalPrice", getPrice(String.valueOf(ticketTable.getSeatID())).get(0).getOriginalPrice());
			ticketMap.put("percentage_Off", getPercentageOff(String.valueOf(ticketTable.getDiscountType())).get(0).getPercentage_Off());
			ticketMap.put("realPrice", String.format("%.3f", (double)ticketMap.get("originalPrice") *(1-(double)ticketMap.get("percentage_Off"))));
		}else {
			ticketMap.put("realPrice", ticketTable.getRealPrice());
		}
		 
		
		
		return ticketMap;
	}
	
	
	//Discount Map
	private Map<String, Object>DiscountMap(DiscountTable discountTable){
		Map<String, Object>discountMap = new HashMap<>();
		
		discountMap.put("discountID", discountTable.getDiscountID());
		discountMap.put("discountName", discountTable.getDiscountName());
		discountMap.put("percentageOff", discountTable.getPercentageOff());
		
		return discountMap;
	}
	
	
	
	/***********************************
	 *Customer order override function**
	 *********************************** 
	 ***********************************/
	@Override
	//get Customer Order
	public List<CustomerOrderTable>getCustomerOrder(String customerOrderID){
		return jdbcTemplate.query(SELECT_CUSTOMER_ORDER, new Object[] {customerOrderID}, new CustomerOrderRowmapper());
	}
	
	
	@Override
	//create Customer Order
	public int createCustomerOrder(CustomerOrderTable customerOrderTable) {
		int affectedRow;
		Map<String, Object> param = CustomerOrderMap(customerOrderTable);
		
		SqlParameterSource paramSource = new MapSqlParameterSource(param);
		logger.info("Create Customer Order : {} ",paramSource);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_CUSTOMER_ORDER, paramSource);
		
		return affectedRow;
	}
	
	
	@Override
	//delete Customer Order
	public int deleteCustomerOrder(String customerOrderID) {
		int affectedRow = jdbcTemplate.update(DELETE_CUSTOMER_ORDER,customerOrderID);
		return affectedRow;
	}
	
	
	
	
	
	/***********************************
	 *** Tickets override function******
	 *********************************** 
	 ***********************************/
	@Override
	//get Ticket
	public List<TicketTable> getTicket(String ticketID){
		return jdbcTemplate.query(SELECT_TICKET, new Object[] {ticketID}, new TicketRowmapper());
	}
	
	
	
	@Override
	//get Ticket by Order
	public List<TicketTable>getTicketByOrder(String customerOrderID){
		return jdbcTemplate.query(SELECT_TICKET_BY_ORDER, new Object[] {customerOrderID}, new TicketRowmapper());
	}
	
	
	@Override
	//create Ticket
	public int createTicket(TicketTable ticketTable) {
		int affectedRow;
			
		Map<String, Object> param = TicketMap(ticketTable);
		
		SqlParameterSource paramSource = new MapSqlParameterSource(param);
		logger.info("Create ticket : {} ",paramSource);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_TICKET, paramSource);
		
		return affectedRow;
	}
	
	@Override
	//Update Ticket
	public int updateTicket(TicketTable ticketTable) {
		int affectedRow;
		Map<String, Object> param = TicketMap(ticketTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating ticket : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_TICKET, pramSource);
		
		return affectedRow;

	}
	
	
	@Override
	//delete Ticket
	public int deleteTicket(String ticketID) {
		int affectedRow = jdbcTemplate.update(DELETE_TICKET,ticketID);
		return affectedRow;

	}
	
	@Override
	public List<TicketTable> getPrice(String seatID) {
		TicketRowmapper r = new TicketRowmapper(); 
		return jdbcTemplate.query(SELECT_PRICE, new Object[] {seatID}, r);
	}
	
	
	@Override
	public List<TicketTable> getPercentageOff(String discountID) {
		return jdbcTemplate.query(SELECT_PERCENTAGE_OFF, new Object[] {discountID}, new TicketRowmapper());
		
	}
	
	
	/***********************************
	 ***Discount override function******
	 *********************************** 
	 ***********************************/
	@Override
	//get Discount
	public List<DiscountTable> getDiscount(String discountID){
		return jdbcTemplate.query(SELECT_DISCOUNT, new Object[] {discountID}, new DiscountRowmapper());
	}


	@Override
	//create Discount
	public int createDiscount(DiscountTable discountTable) {
		int affectedRow;
		Map<String, Object> param = DiscountMap(discountTable);
		
		SqlParameterSource paramSource = new MapSqlParameterSource(param);
		logger.info("Create discount : {} ",paramSource);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_DISCOUNT, paramSource);
		
		return affectedRow;
	}
	
	
	@Override
	//update Discount
	public int updateDiscount(DiscountTable discountTable) {
		int affectedRow;
		Map<String, Object> param = DiscountMap(discountTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating discount : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_DISCOUNT, pramSource);
		
		return affectedRow;

	}
	
	
	@Override
	//delete Discount
	public int deleteDiscount(String discountID) {
		int affectedRow = jdbcTemplate.update(DELETE_DISCOUNT,discountID);
		return affectedRow;

	}


}

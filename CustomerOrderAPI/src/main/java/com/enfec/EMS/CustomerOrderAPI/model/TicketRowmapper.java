package com.enfec.EMS.CustomerOrderAPI.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class TicketRowmapper implements RowMapper<TicketTable>  {
	@Override
	public TicketTable mapRow(ResultSet rs, int rowNum) throws SQLException{
		TicketTable ticketTable = new TicketTable();
		ticketTable.setTicketID(rs.getInt("Ticket_ID"));
		ticketTable.setCustomerOrderID(rs.getInt("COrder_ID"));
		ticketTable.setEventID(rs.getInt("Event_ID"));
		ticketTable.setRoomID(rs.getInt("Room_ID"));
		ticketTable.setSeatID(rs.getInt("Seat_ID"));
		ticketTable.setRealPrice(rs.getDouble("RealPrice"));
		
		return ticketTable;
	}
	

}

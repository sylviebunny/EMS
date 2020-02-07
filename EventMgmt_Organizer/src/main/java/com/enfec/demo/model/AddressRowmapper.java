package com.enfec.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AddressRowmapper implements RowMapper<Address>{
	@Override
	public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
		Address address = new Address();
		address.setAddress_ID(rs.getInt("Address_ID"));
		address.setStreet1((rs.getString("Street1")));
		address.setStreet2((rs.getString("Street2")));
		address.setCity((rs.getString("City")));
		address.setState((rs.getString("State")));
		address.setZipcode(rs.getInt("Zipcode"));
		address.setOther_Details(rs.getString("Other_Details"));
		address.setOrganizer_ID(rs.getInt("Organizer_ID"));
		return address;
	}
}

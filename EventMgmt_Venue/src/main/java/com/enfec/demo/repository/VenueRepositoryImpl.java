package com.enfec.demo.repository;

import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.enfec.demo.model.Venue;

@Component
public class VenueRepositoryImpl implements VenueRepository {
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int createVenue(Venue venue) {
		String CREATE_VENUE = "INSERT INTO `Venues` (`Venue_Name`, `Other_Details`) VALUES (?,?)";
		String CREATE_VENUE1 = "INSERT INTO `Venue_Address` (`Venue_ID`, `Street1`, `Street2`, `City`, `State`, `Zipcode`) VALUES (?,?,?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int count = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_VENUE, new String[]{"Venue_ID"});
	                ps.setString(1, venue.getVenue_Name());
	                ps.setString(2, venue.getOther_Details());
	                return ps;
	              }, keyHolder);
	    Number key = keyHolder.getKey();
	    //key is primary key

	    int count1 = jdbcTemplate.update(CREATE_VENUE1, key.longValue(),venue.getStreet1(),venue.getStreet2(),
	    		venue.getCity(),venue.getState(),venue.getZipcode());
	    return count;
	}
	
	@Override
	public Venue getVenueInfo(int Venue_ID) {
		String SELECT_VENUE = "select * from Venues v join Venue_Address a on v.Venue_ID = a.Venue_ID where v.Venue_ID = ?";
		Venue venue;
		try {
			venue = jdbcTemplate.queryForObject(SELECT_VENUE, new Object[] { Venue_ID }, 
					new BeanPropertyRowMapper<Venue>(Venue.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return venue;
	}
	/*
	public int updateVenue(Venue venue);	
	public int deleteVenue(int Venue_ID);*/
}

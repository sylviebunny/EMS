package com.enfec.demo.repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.demo.model.Venue;

@Component
@Transactional
public class VenueRepositoryImpl implements VenueRepository {
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int createVenue(Venue venue) {
		String CREATE_VENUE = "INSERT INTO `Venues` (`Venue_Name`, `Other_Details`) VALUES (?,?)";
		String CREATE_VENUE1 = "INSERT INTO `Venue_Address` (`Venue_ID`, `Street1`, `Street2`, `City`, `State`, `Zipcode`, `Latitude`, `Longitude`) VALUES (?,?,?,?,?,?,?,?)";
		
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
	    		venue.getCity(),venue.getState(),venue.getZipcode(),venue.getLatitude(),venue.getLongitude());
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
	
	//Update with partial information	
	@Override
	public int updateVenue(Venue venue) {
		//Update info of Rooms table
		String UPDATE_VENUE_INFO_PREFIX = "UPDATE `Venues` SET "; 
		String UPDATE_VENUE_INFO_SUFFIX = " WHERE Venue_ID = :Venue_ID";
		int affectedRow;
		Map<String, Object> param = venueMap(venue);
				
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_VENUE_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("Venue_ID")) {
				UPDATE_VENUE_INFO.append("`");
				UPDATE_VENUE_INFO.append(key + "`" +" = :" + key + ",");
			}
		}
		UPDATE_VENUE_INFO = UPDATE_VENUE_INFO.deleteCharAt(UPDATE_VENUE_INFO.length() - 1); 
				
		String UPDATE_VENUE = UPDATE_VENUE_INFO_PREFIX + UPDATE_VENUE_INFO + UPDATE_VENUE_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_VENUE, pramSource);
				
		//Update info of Space_Requests table
		String UPDATE_VADDRESS_INFO_PREFIX = "UPDATE `Venue_Address` SET "; 
		int affectedRow1;
		Map<String, Object> param1 = venue_addressMap(venue);
				
		SqlParameterSource pramSource1 = new MapSqlParameterSource(param1);
		StringBuilder UPDATE_VADDRESS_INFO = new StringBuilder();
		for (String key : param1.keySet()) {
			if (param1.get(key) != null && !key.equals("Venue_ID")) {
				UPDATE_VADDRESS_INFO.append(key + "=:" + key + ",");
			}
		}
		UPDATE_VADDRESS_INFO = UPDATE_VADDRESS_INFO.deleteCharAt(UPDATE_VADDRESS_INFO.length() - 1); 
				
		String UPDATE_VADDRESS = UPDATE_VADDRESS_INFO_PREFIX + UPDATE_VADDRESS_INFO + UPDATE_VENUE_INFO_SUFFIX;
		affectedRow1 = namedParameterJdbcTemplate.update(UPDATE_VADDRESS, pramSource1);
		return affectedRow;
	}
	
	@Override
	public int deleteVenue(int Venue_ID) {
		String DELETE_VENUE = "DELETE FROM Venues WHERE Venue_ID = ?";
		String DELETEfromVADDRESS = "DELETE FROM Venue_Address WHERE Venue_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_VENUE, Venue_ID);
		int affectedRow1 = jdbcTemplate.update(DELETEfromVADDRESS, Venue_ID);
		
		return affectedRow;
	}
	
	private Map<String, Object> venueMap(Venue venue) {
		Map<String, Object>param = new HashMap<>();

		if (venue.getVenue_ID() != 0) {
			param.put("Venue_ID", venue.getVenue_ID());
		} else {
			throw new NullPointerException("Venue_ID cannot be null");
		}	
		param.put("Venue_Name", venue.getVenue_Name() == null || venue.getVenue_Name().isEmpty() ? null:venue.getVenue_Name());
		param.put("Other_Details", venue.getOther_Details() == null || venue.getOther_Details().isEmpty() ? null:venue.getOther_Details());
		return param;
	}
	
	private Map<String, Object> venue_addressMap(Venue venue) {
		Map<String, Object>param = new HashMap<>();
		
		if (venue.getVenue_ID() != 0) {
			param.put("Venue_ID", venue.getVenue_ID());
		} else {
			throw new NullPointerException("Venue_ID cannot be null");
		}		
//		param.put("VenueAddress_ID", venue.getVenueAddress_ID() != 0 ? venue.getVenueAddress_ID() : null);	
		param.put("Street1", venue.getStreet1() == null || venue.getStreet1().isEmpty() ? null:venue.getStreet1());
		param.put("Street2", venue.getStreet2() == null || venue.getStreet2().isEmpty() ? null:venue.getStreet2());
		param.put("City", venue.getCity() == null || venue.getCity().isEmpty() ? null:venue.getCity());
		param.put("State", venue.getState() == null || venue.getState().isEmpty() ? null:venue.getState());
		param.put("Zipcode", venue.getZipcode() != 0 ? venue.getZipcode() : null);
		param.put("Latitude", venue.getLatitude() != 0 ? venue.getLatitude() : null);
		param.put("Longitude", venue.getLongitude() != 0 ? venue.getLongitude() : null);
		return param;
	}
}

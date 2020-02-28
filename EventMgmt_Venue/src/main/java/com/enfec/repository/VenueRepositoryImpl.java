package com.enfec.repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
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

import com.enfec.model.Venue;

/**
 * Implement CRUD methods for venue and venue address
 * @author sylvia zhao
 */
@Component
@Transactional
public class VenueRepositoryImpl implements VenueRepository {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	/**
     * Create venue information
     * Map venue table to MySql parameters and insert into 'Venues' and 'Venue_Address' table in database
     * 
     * @param venue: The information that needs to be created
     * @return number of affected rows
     */
	@Override
	public int createVenue(Venue venue) {
		String CREATE_VENUE = "INSERT INTO `Venues` (`Venue_Name`, `Other_Details`) VALUES (?,?)";
		String CREATE_VENUE1 = "INSERT INTO `Venue_Address` (`Venue_ID`, `Street1`, `Street2`, `City`, `State`, "
				+ "`Zipcode`, `Latitude`, `Longitude`) VALUES (?,?,?,?,?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int affectedRow = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_VENUE, new String[]{"Venue_ID"});
	                ps.setString(1, venue.getVenue_name());
	                ps.setString(2, venue.getOther_details());
	                return ps;
	              }, keyHolder);
	    Number primaryKey = keyHolder.getKey();   
	    jdbcTemplate.update(CREATE_VENUE1, primaryKey.longValue(),venue.getStreet1(),venue.getStreet2(),
	    		venue.getCity(),venue.getState(),venue.getZipcode(),venue.getLatitude(),venue.getLongitude());
	    return affectedRow;
	}
	
	/**
     * Get venue basic and address information from 'Venues' and 'Venue_Address' tables in database by venue id
     * @param Venue_ID
     * @return Venue: specific venue information that match the request
     */
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
	
	/**
     * Get all venues' basic and address information from 'Venues' and 'Venue_Address' tables in database
     * @return List<Venue>: all entries that match the request
     */
	@Override
	public List<Venue> getAllVenueInfo() {
		String SELECT_ALL_VENUE = "select * from Venues v join Venue_Address a on v.Venue_ID = a.Venue_ID";
		return jdbcTemplate.query(SELECT_ALL_VENUE, new BeanPropertyRowMapper<Venue>(Venue.class));
	}
	
	 /**
     * Update venue and its address information
     * Map venue table to MySql parameters, update 'Venues' and 'Venue_Address' tables in database
     * 
     * @param venue: The information that needs to be updated. 
     * @return number of affected rows
     */
	@Override
	public int updateVenue(Venue venue) {
		//Update info of Venues table
		String UPDATE_VENUE_INFO_PREFIX = "UPDATE `Venues` SET "; 
		String UPDATE_VENUE_INFO_SUFFIX = " WHERE Venue_ID = :venue_id";

		Map<String, Object> param = venueMap(venue);	
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_VENUE_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("venue_id")) {
				UPDATE_VENUE_INFO.append("`");
				UPDATE_VENUE_INFO.append(key + "`" +" = :" + key + ",");
			}
		}
		UPDATE_VENUE_INFO = UPDATE_VENUE_INFO.deleteCharAt(UPDATE_VENUE_INFO.length() - 1); 			
		String UPDATE_VENUE = UPDATE_VENUE_INFO_PREFIX + UPDATE_VENUE_INFO + UPDATE_VENUE_INFO_SUFFIX;		
		int affectedRow =namedParameterJdbcTemplate.update(UPDATE_VENUE, pramSource);
				
		//Update info of Venue_Address table
		String UPDATE_VADDRESS_INFO_PREFIX = "UPDATE `Venue_Address` SET "; 
		Map<String, Object> param1 = venue_addressMap(venue);				
		SqlParameterSource pramSource1 = new MapSqlParameterSource(param1);
		StringBuilder UPDATE_VADDRESS_INFO = new StringBuilder();
		for (String key : param1.keySet()) {
			if (param1.get(key) != null && !key.equals("venue_id")) {
				UPDATE_VADDRESS_INFO.append(key + "=:" + key + ",");
			}
		}
		UPDATE_VADDRESS_INFO = UPDATE_VADDRESS_INFO.deleteCharAt(UPDATE_VADDRESS_INFO.length() - 1); 				
		String UPDATE_VADDRESS = UPDATE_VADDRESS_INFO_PREFIX + UPDATE_VADDRESS_INFO + UPDATE_VENUE_INFO_SUFFIX;	
		namedParameterJdbcTemplate.update(UPDATE_VADDRESS, pramSource1);
		
		return affectedRow;
	}
	
	/**
     * Delete venue information from 'Venues' and 'Venue_Address' tables in database by venue id
     * @param Venue_ID
     * @return number of affected rows
     */
	@Override
	public int deleteVenue(int Venue_ID) {
		String DELETE_VENUE = "DELETE FROM Venues WHERE Venue_ID = ?";
		String DELETEfromVADDRESS = "DELETE FROM Venue_Address WHERE Venue_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_VENUE, Venue_ID);
		jdbcTemplate.update(DELETEfromVADDRESS, Venue_ID);
		
		return affectedRow;
	}
	
	
	 /**
     * For update with 'Venues' table in database
     * Mapping venue information between URL body information and database variable attributes
     * 
     * @param venue: venue information corresponding to 'Venues' table used for update
     * @return Map<String, Object>: contains variable and it's corresponding information 
     */
	private Map<String, Object> venueMap(Venue venue) {
		Map<String, Object>param = new HashMap<>();

		if (venue.getVenue_id() != 0) {
			param.put("venue_id", venue.getVenue_id());
		} 
		param.put("venue_name", venue.getVenue_name() == null || venue.getVenue_name().isEmpty() ? null : venue.getVenue_name());
		param.put("other_details", venue.getOther_details() == null || venue.getOther_details().isEmpty() ? null 
				: venue.getOther_details());
		return param;
	}
	
	/**
     * For update with 'Venue_Address' table in database
     * Mapping venue address information between URL body information and database variable attributes
     * 
     * @param venue: venue information corresponding to 'Venue_Address' table used for update
     * @return Map<String, Object>: contains variable and it's corresponding information 
     */
	private Map<String, Object> venue_addressMap(Venue venue) {
		Map<String, Object>param = new HashMap<>();
		
		if (venue.getVenue_id() != 0) {
			param.put("venue_id", venue.getVenue_id());
		} 		
		param.put("street1", venue.getStreet1() == null || venue.getStreet1().isEmpty() ? null : venue.getStreet1());
		param.put("street2", venue.getStreet2() == null || venue.getStreet2().isEmpty() ? null : venue.getStreet2());
		param.put("city", venue.getCity() == null || venue.getCity().isEmpty() ? null : venue.getCity());
		param.put("state", venue.getState() == null || venue.getState().isEmpty() ? null : venue.getState());
		param.put("zipcode", venue.getZipcode() != 0 ? venue.getZipcode() : null);
		param.put("latitude", venue.getLatitude() != 0 ? venue.getLatitude() : null);
		param.put("longitude", venue.getLongitude() != 0 ? venue.getLongitude() : null);
		return param;
	}
}

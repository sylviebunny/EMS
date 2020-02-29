package com.enfec.repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.enfec.model.SeatCategory;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Implement CRUD methods for seat category
* Class: SeatCategoryRepositoryImpl
*
************************************************/
@Component
@Transactional
public class SeatCategoryRepositoryImpl implements SeatCategoryRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(SeatCategoryRepositoryImpl.class);
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
     * Create seat category information
     * Map seat category table to MySql parameters and insert into database
     * 
     * @param sc: SeatCategory; The information that needs to be created
     * @return number of affected rows
     */
	@Override
	public int createSeatCategory(SeatCategory sc) {
		String CREATE_SEATCATEGORY = "INSERT INTO `Seat_Category` (`Category_Name`, `Price`) VALUES (?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int count = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_SEATCATEGORY, new String[]{"Category_ID"});
	                ps.setString(1, sc.getCategory_name() );
	                ps.setDouble(2, sc.getPrice());
	                return ps;
	              }, keyHolder);
	    return count;
	}
	
	/**
     * Get seat category information from database by seat category id
     * @param Category_ID
     * @return SeatCategory: specific seat category information that match the request
     */
	@Override
	public SeatCategory getSeatCategoryInfo(int Category_ID) {
		String SELECT_SEATCATEGORY = "select * from `Seat_Category` where Category_ID = ?";
		SeatCategory sc;
		try {
			sc = jdbcTemplate.queryForObject(SELECT_SEATCATEGORY, new Object[] { Category_ID }, 
					new BeanPropertyRowMapper<SeatCategory>(SeatCategory.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return sc;
	}
	
	/**
     * Update seat category information
     * Map seat category table to MySql parameters, and update 'Seat_Category' table in database
     * 
     * @param sc: SeatCategory; The information that needs to be updated. 
     * @return number of affected rows
     */
	@Override
	public int updateSeatCategory(SeatCategory sc) {
		String UPDATE_SEATCATEGORY_INFO_PREFIX = "UPDATE `Seat_Category` SET "; 
		String UPDATE_SEATCATEGORY_INFO_SUFFIX = " WHERE Category_ID = :category_id";
		
		Map<String, Object> param = seatCategoryMap(sc);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating Seat Category Info: {}", pramSource);
		
		StringBuilder UPDATE_SEATCATEGORY_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("category_id")) {
				UPDATE_SEATCATEGORY_INFO.append("`");
				UPDATE_SEATCATEGORY_INFO.append(key + "`" +" = :" + key + ",");
			}
		}
		UPDATE_SEATCATEGORY_INFO = UPDATE_SEATCATEGORY_INFO.deleteCharAt(UPDATE_SEATCATEGORY_INFO.length() - 1); 
		String UPDATE_SEATCATEGORY = UPDATE_SEATCATEGORY_INFO_PREFIX + UPDATE_SEATCATEGORY_INFO + UPDATE_SEATCATEGORY_INFO_SUFFIX;
		
		int affectedRow = namedParameterJdbcTemplate.update(UPDATE_SEATCATEGORY, pramSource);
		return affectedRow;
	}
	
	/**
     * Delete seat category information from database by seat category id
     * @param Category_ID
     * @return number of affected rows
     */
	@Override
	public int deleteSeatCategory(int Category_ID) {
		String DELETE_SEATCATEGORY = "DELETE FROM `Seat_Category` WHERE Category_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_SEATCATEGORY, Category_ID);
		return affectedRow;
	}
	

	/**
     * For seat category update in database
     * Mapping seat category information between URL body information and database variable attributes
     * 
     * @param sc: SeatCategory; seat category information used for update
     * @return Map<String, Object>: contains variable and it's corresponding information 
     */
	private Map<String, Object> seatCategoryMap(SeatCategory sc) {
		Map<String, Object> param = new HashMap<>();

		if (sc.getCategory_id() != 0) {
			param.put("category_id", sc.getCategory_id());
		} 
		param.put("category_name", sc.getCategory_name() == null || sc.getCategory_name().isEmpty() ? null 
				: sc.getCategory_name());
		param.put("price", sc.getPrice() != 0 ? sc.getPrice() : null);
		return param;
	}
}

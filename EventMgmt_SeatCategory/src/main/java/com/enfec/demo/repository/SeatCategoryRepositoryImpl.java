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

import com.enfec.demo.model.SeatCategory;

@Component
@Transactional
public class SeatCategoryRepositoryImpl implements SeatCategoryRepository {
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int createSeatCategory(SeatCategory sc) {
		String CREATE_SEATCATEGORY = "INSERT INTO `Seat_Category` (`Category_Name`, `Price`) VALUES (?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int count = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_SEATCATEGORY, new String[]{"Category_ID"});
	                ps.setString(1, sc.getCategory_Name() );
	                ps.setDouble(2, sc.getPrice());
	                return ps;
	              }, keyHolder);
	    return count;
	}
	
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
	
	@Override
	public int updateSeatCategory(SeatCategory sc) {
		String UPDATE_SEATCATEGORY_INFO_PREFIX = "UPDATE `Seat_Category` SET "; 
		String UPDATE_SEATCATEGORY_INFO_SUFFIX = " WHERE Category_ID = :Category_ID";
		int affectedRow;
		Map<String, Object> param = seatCategoryMap(sc);
//		UPDATE `evntmgmt_usa`.`Seats` SET `Category_ID` = '1' WHERE (`Seat_ID` = '2');
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_SEATCATEGORY_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("Category_ID")) {
				UPDATE_SEATCATEGORY_INFO.append("`");
				UPDATE_SEATCATEGORY_INFO.append(key + "`" +" = :" + key + ",");
			}
		}
		UPDATE_SEATCATEGORY_INFO = UPDATE_SEATCATEGORY_INFO.deleteCharAt(UPDATE_SEATCATEGORY_INFO.length() - 1); 
		
		String UPDATE_SEATCATEGORY = UPDATE_SEATCATEGORY_INFO_PREFIX + UPDATE_SEATCATEGORY_INFO + UPDATE_SEATCATEGORY_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_SEATCATEGORY, pramSource);
		return affectedRow;
	}
	
	@Override
	public int deleteSeatCategory(int Category_ID) {
		String DELETE_SEATCATEGORY = "DELETE FROM `Seat_Category` WHERE Category_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_SEATCATEGORY, Category_ID);
		return affectedRow;
	}
	
	private Map<String, Object> seatCategoryMap(SeatCategory sc) {
		Map<String, Object>param = new HashMap<>();

		if (sc.getCategory_ID() != 0) {
			param.put("Category_ID", sc.getCategory_ID());
		} else {
			throw new NullPointerException("Category_ID cannot be null");
		}
		param.put("Category_Name", sc.getCategory_Name() == null || sc.getCategory_Name().isEmpty() ? null:sc.getCategory_Name());
		param.put("Price", sc.getPrice() != 0 ? sc.getPrice() : null);
		return param;
	}
}

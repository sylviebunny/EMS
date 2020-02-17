package com.enfec.demo.repository;

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

import com.enfec.demo.model.Seat;

@Component
public class SeatRepositoryImpl implements SeatRepository{
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int createSeat(Seat seat) {
		/*Another way
		String CREATE_SEAT = "INSERT INTO `Seats` (`Room_ID`, `Category_ID`, `Row_Number`, `Col_Number`, `Availability`) VALUES (:Room_ID,:Category_ID,:Row_Number,:Col_Number,:Availability)";
		int affectedRow;
		Map<String, Object> param = SeatMap(seat);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_SEAT, pramSource);	
		return affectedRow;*/
		
		String CREATE_SEAT = "INSERT INTO `Seats` (`Room_ID`, `Category_ID`, `Row_Number`, `Col_Number`, `Availability`) VALUES (?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int count = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(CREATE_SEAT, new String[]{"Seat_ID"});
	                ps.setInt(1, seat.getRoom_ID());
	                ps.setInt(2, seat.getCategory_ID() );
	                ps.setString(3, seat.getRow_Number() );
	                ps.setString(4, seat.getCol_Number());
	                ps.setString(5, seat.getAvailability());
	                return ps;
	              }, keyHolder);
	    return count;
	}
	
	@Override
	public Seat getSeatInfo(int Seat_ID) {
		String SELECT_SEAT = "select * from Seats a join Seat_Category b on a.Category_ID = b.Category_ID where a.Seat_ID = ?";
		Seat seat;
		try {
			seat = jdbcTemplate.queryForObject(SELECT_SEAT, new Object[] { Seat_ID }, 
					new BeanPropertyRowMapper<Seat>(Seat.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return seat;
	}
	
	
	@Override
	public List<Seat> getAvailableSeatInfo(int Room_ID) {
		String SELECT_AVAILABLE_SEATS = "select * FROM Seats a join Seat_Category b on a.Category_ID = b.Category_ID where Room_ID = ? AND Availability = true";
		return jdbcTemplate.query(SELECT_AVAILABLE_SEATS, new Object[] { Room_ID }, new BeanPropertyRowMapper<Seat>(Seat.class));
	}
	
	
//	Update with partial info
	@Override
	public int updateSeat(Seat seat) {
		String UPDATE_SEAT_INFO_PREFIX = "UPDATE `Seats` SET "; 
		String UPDATE_SEAT_INFO_SUFFIX = " WHERE Seat_ID = :Seat_ID";
		int affectedRow;
		Map<String, Object> param = seatMap(seat);
//		UPDATE `evntmgmt_usa`.`Seats` SET `Category_ID` = '1' WHERE (`Seat_ID` = '2');
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_SEAT_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("Seat_ID")) {
				UPDATE_SEAT_INFO.append("`");
				UPDATE_SEAT_INFO.append(key + "`" +" = :" + key + ",");
			}
		}
		UPDATE_SEAT_INFO = UPDATE_SEAT_INFO.deleteCharAt(UPDATE_SEAT_INFO.length() - 1); 
		
		String UPDATE_SEAT = UPDATE_SEAT_INFO_PREFIX + UPDATE_SEAT_INFO + UPDATE_SEAT_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_SEAT, pramSource);
		return affectedRow;
	}	
	
	@Override
	public int deleteSeat(int Seat_ID) {
		String DELETE_SEAT = "DELETE FROM Seats WHERE Seat_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_SEAT, Seat_ID);
		return affectedRow;
	}
	
	
	/*
	//For create, update with total info
	private Map<String, Object> SeatMap(Seat seat) {
		Map<String, Object>param = new HashMap<>();
		param.put("Room_ID", seat.getRoom_ID() != 0 ? seat.getRoom_ID() : null);
		param.put("Category_ID", seat.getCategory_ID() != 0 ? seat.getCategory_ID() : null);
		param.put("Row_Number", seat.getRow_Number().isEmpty() ? null:seat.getRow_Number());
		param.put("Col_Number", seat.getCol_Number().isEmpty() ? null:seat.getCol_Number());
		param.put("Availability", seat.isAvailability() ? seat.isAvailability() : false);
		return param;
	}*/

	//For update with partial info
	private Map<String, Object> seatMap(Seat seat) {
		Map<String, Object>param = new HashMap<>();

		if (seat.getSeat_ID() != 0) {
			param.put("Seat_ID", seat.getSeat_ID());
		} else {
			throw new NullPointerException("Seat_ID cannot be null");
		}	
		param.put("Room_ID", seat.getRoom_ID() != 0 ? seat.getRoom_ID() : null);
		param.put("Category_ID", seat.getCategory_ID() != 0 ? seat.getCategory_ID() : null);
		param.put("Row_Number", seat.getRow_Number() == null || seat.getRow_Number().isEmpty() ? null:seat.getRow_Number());
		param.put("Col_Number", seat.getCol_Number() == null || seat.getCol_Number().isEmpty() ? null:seat.getCol_Number());
		param.put("Availability", seat.getAvailability() == null || seat.getAvailability().isEmpty() ? null:seat.getAvailability());
		return param;
	}
}

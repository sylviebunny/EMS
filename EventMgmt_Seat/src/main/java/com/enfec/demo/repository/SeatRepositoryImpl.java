package com.enfec.demo.repository;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.demo.model.Seat;

@Component
@Transactional
public class SeatRepositoryImpl implements SeatRepository{
	//Implement CRUD methods for seat
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int createSeat(Seat seat) {
		String CREATE_SEAT = "INSERT INTO `Seats` (`Room_ID`, `Category_ID`, `Row_Number`, `Col_Number`, `Availability`) VALUES (:room_id,:category_id,:row_number,:col_number,:availability)";
		Map<String, Object> param = seatMap(seat);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		int affectedRow = namedParameterJdbcTemplate.update(CREATE_SEAT, pramSource);	
		return affectedRow;
	}
	
	@Override
	public Seat getSeatInfo(int Seat_ID) {
		//Get seat info from "Seats" and "Seat_Category" tables
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
	
	//Get available seats in a specific room
	@Override
	public List<Seat> getAvailableSeatInfo(int Room_ID) {
		String SELECT_AVAILABLE_SEATS = "select * FROM Seats a join Seat_Category b on a.Category_ID = b.Category_ID where Room_ID = ? AND Availability = true";
		return jdbcTemplate.query(SELECT_AVAILABLE_SEATS, new Object[] { Room_ID }, new BeanPropertyRowMapper<Seat>(Seat.class));
	}
	
	//Get all seats in a specific room
	@Override
	public List<Seat> getAllSeatInfo(int Room_ID) {
		String SELECT_ALL_SEATS = "select * FROM Seats a join Seat_Category b on a.Category_ID = b.Category_ID where Room_ID = ?";
		return jdbcTemplate.query(SELECT_ALL_SEATS, new Object[] { Room_ID }, new BeanPropertyRowMapper<Seat>(Seat.class));
	}
	
	//Update seat info with partial information	
	@Override
	public int updateSeat(Seat seat) {
		String UPDATE_SEAT_INFO_PREFIX = "UPDATE `Seats` SET "; 
		String UPDATE_SEAT_INFO_SUFFIX = " WHERE Seat_ID = :seat_id";

		Map<String, Object> param = seatMap(seat);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_SEAT_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("seat_id")) {
				UPDATE_SEAT_INFO.append("`");
				UPDATE_SEAT_INFO.append(key + "`" +" = :" + key + ",");
			}
		}
		UPDATE_SEAT_INFO = UPDATE_SEAT_INFO.deleteCharAt(UPDATE_SEAT_INFO.length() - 1); 
		String UPDATE_SEAT = UPDATE_SEAT_INFO_PREFIX + UPDATE_SEAT_INFO + UPDATE_SEAT_INFO_SUFFIX;
		int affectedRow =namedParameterJdbcTemplate.update(UPDATE_SEAT, pramSource);
		return affectedRow;
	}	
	
	@Override
	public int deleteSeat(int Seat_ID) {
		String DELETE_SEAT = "DELETE FROM Seats WHERE Seat_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_SEAT, Seat_ID);
		return affectedRow;
	}

	//For seat create and update
	private Map<String, Object> seatMap(Seat seat) {
		Map<String, Object>param = new HashMap<>();

		if (seat.getSeat_id() != 0) {
			param.put("seat_id", seat.getSeat_id());
		} 
		param.put("room_id", seat.getRoom_id() != 0 ? seat.getRoom_id() : null);
		param.put("category_id", seat.getCategory_id() != 0 ? seat.getCategory_id() : null);
		param.put("row_number", seat.getRow_number() == null || seat.getRow_number().isEmpty() ? null : seat.getRow_number());
		param.put("col_number", seat.getCol_number() == null || seat.getCol_number().isEmpty() ? null : seat.getCol_number());
		param.put("availability", seat.getAvailability() == null || seat.getAvailability().isEmpty() ? null : seat.getAvailability());
		return param;
	}
}

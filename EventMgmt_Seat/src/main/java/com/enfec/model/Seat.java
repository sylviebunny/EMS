package com.enfec.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Seat information to map 'Seats' and 'Seat_Category' tables in database
* Class: Seat
* 
************************************************/
@Data
@Getter
@Setter
@Component
public class Seat {
	
	private int seat_id;
	private int room_id;
	private int category_id;
	private String row_number;
	private String col_number; 
	private String availability;
	private String category_name;
	private double price;
}

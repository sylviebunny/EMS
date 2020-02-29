package com.enfec.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Seat category information to map 'Seat_Category' table in database
* Class: SeatCategory 
* 
************************************************/
@Data
@Getter
@Setter
@Component
public class SeatCategory {
	
	private int category_id;
	private String category_name;
	private double price;
}

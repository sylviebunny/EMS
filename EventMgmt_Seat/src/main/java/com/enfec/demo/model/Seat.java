package com.enfec.demo.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

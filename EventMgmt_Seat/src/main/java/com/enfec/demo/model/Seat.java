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
	public int Seat_ID;
	public int Room_ID;
	public int Category_ID;
	public String Row_Number;
	public String Col_Number; 
	public String Availability;
	public String Category_Name;
	public double Price;
}

package com.enfec.demo.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Component
public class Address {
	public int Address_ID; 
	public String Street1;
	public String Street2;
	public String City;
	public String State;
	public int Zipcode;
	public String Other_Details;
	public int Organizer_ID;
}

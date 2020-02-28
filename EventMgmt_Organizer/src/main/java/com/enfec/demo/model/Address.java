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
	private int address_id; 
	private String street1;
	private String street2;
	private String city;
	private String state;
	private int zipcode;
	private String other_details;
	private int organizer_id;
}

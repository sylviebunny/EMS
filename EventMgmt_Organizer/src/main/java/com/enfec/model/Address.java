package com.enfec.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Organizer address information to map 'Address' table in database
* Class: Address
* 
************************************************/
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
	private String zipcode;
	private int organizer_id;
}

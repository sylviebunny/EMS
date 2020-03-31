package com.enfec.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Organizer information to map 'Organizers' table in database
* Class: OrganizerTable 
* 
************************************************/
@Data
@Component
@Getter
@Setter
public class OrganizerTable {

	private int organizer_id;
	private String organizer_name;
	private String email_address;
	private String password;
	private String phone;
	private String actived;
	private int contact_id; 
	private int address_id; 
	private String contact_name; 
	private String contact_telephone;
	private String web_site_address;
	private String street1; 
	private String street2;
	private String city; 
	private String state; 
	private String zipcode; 

}

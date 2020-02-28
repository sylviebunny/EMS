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
	private String other_details;
}

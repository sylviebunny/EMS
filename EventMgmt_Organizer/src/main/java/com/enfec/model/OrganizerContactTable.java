package com.enfec.model;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Organizer contact information to map 'Contacts' table in database
* Class: OrganizerContactTable
* 
************************************************/
@Data
@Component
@Getter
@Setter
@ApiModel
public class OrganizerContactTable {

	private int organizer_id;
	private int contact_id;
	private String contact_name;
	private String contact_telephone;
	private String web_site_address;
	private int address_id;
}

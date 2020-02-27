package com.enfec.model;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * OrganizerContactTable class to map 'Contacts' table in database
 */
@Data
@Component
@Getter
@Setter
@ApiModel
public class OrganizerContactTable {
	
	private int organizer_id; 
	private int contact_id; 
	private String contact_name; 
	private String telephone; 
	private String web_site_address; 
	private int address_id; 
	
}

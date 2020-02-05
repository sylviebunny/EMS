package com.enfec.demo.model;

import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
public class OrganizerTable {
	int Organizer_ID;
	private String Organizer_Name; 
	private String Email_Address;
	private String Password;
	private String Other_Details;
	
}

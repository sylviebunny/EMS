package com.enfec.demo.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
@Component
public class OrganizerTable {

	public int Organizer_ID;
	public String Organizer_Name; 
	public String Email_Address;
	public String Password;
	public String Other_Details;
}

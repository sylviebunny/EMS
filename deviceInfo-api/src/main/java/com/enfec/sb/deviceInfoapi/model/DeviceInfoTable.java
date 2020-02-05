package com.enfec.sb.deviceInfoapi.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DeviceInfoTable {
	
	int organizer_id;
	private String organizer_name; 
	private String email_address; 
	private String password; 
	private String other_details; 
	
	public void setOrganizer_id(int orgId) {
		organizer_id = orgId; 
	}
	
	public int getOrganizer_id() {
		return organizer_id; 
	}
	
	public void setOrganizer_name(String orgName) {
		organizer_name = orgName; 
	}
	
	public String getOrganizer_name() {
		return organizer_name; 
	}
	
	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOther_details() {
		return other_details;
	}

	public void setOther_details(String other_details) {
		this.other_details = other_details;
	}

}

package com.enfec.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
* Author: Chad Chai
* Assignment: Organizer token information to map 'Organizer_Token' table in database
* Class: OrganizerTokenTable 
************************************************/
@Data
@Component
@Getter
@Setter
public class OrganizerTokenTable {

	private static final int TOKEN_EXPIRATION = 1000 * 60 * 15;
	private int organizerTokenID;
	private String organizerEmail;
	private String organizerToken;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp organizerExpiryDate;

	public static int getTokenExpiration() {
		return TOKEN_EXPIRATION;
	}
}

package com.enfec.EMS.CustomerAPI.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
* Author: Chad Chai
* Assignment: Customer token information to map 'Customer_Token' table in database
* Class: CustomerTokenTable 
************************************************/

@Data
@Component
@Getter
@Setter

public class CustomerTokenTable {
	public static int getTokenExpiration() {
		return TOKEN_EXPIRATION;
	}

	private static final int TOKEN_EXPIRATION = 1000 * 60 * 15;

	private int customerTokenID;
	private String customerEmail;
	private String customerToken;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp customerExpiryDate;

}

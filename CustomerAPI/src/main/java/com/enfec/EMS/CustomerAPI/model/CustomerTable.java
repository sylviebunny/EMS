package com.enfec.EMS.CustomerAPI.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
* Author: Chad Chai
* Assignment: Customer information to map 'Customers' table in database
* Class: CustomerTable 
************************************************/

@Data
@Component
@Getter
@Setter
public class CustomerTable {
	private int id;
	private String name;
	private String email;
	private String psw;
	private String phone;
	private int hasActived;

}

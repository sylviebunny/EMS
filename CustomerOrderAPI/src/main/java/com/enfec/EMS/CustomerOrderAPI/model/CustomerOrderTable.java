package com.enfec.EMS.CustomerOrderAPI.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
* Author: Chad Chai
* Assignment: Customer Order information to map 'Customer_Orders' table in database
* Class: CustomerOrderTable 
************************************************/
@Data
@Component
@Getter
@Setter
public class CustomerOrderTable {
	private int customerOrderID;
	private int customerID;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp orderTime;
}

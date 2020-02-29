package com.enfec.EMS.CustomerOrderAPI.model;

import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/************************************************
* Author: Chad Chai
* Assignment: Discount information to map 'Discounts' table in database
* Class: DiscountTable 
************************************************/
@Data
@Component
@Getter
@Setter
public class DiscountTable {	
	private int discountID;
	private String discountName;
	private double percentageOff;
}

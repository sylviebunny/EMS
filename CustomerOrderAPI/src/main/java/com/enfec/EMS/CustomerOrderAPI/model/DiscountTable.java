package com.enfec.EMS.CustomerOrderAPI.model;

import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Component
@Getter
@Setter
public class DiscountTable {	
	private int discountID;
	private String discountName;
	private double percentageOff;
}

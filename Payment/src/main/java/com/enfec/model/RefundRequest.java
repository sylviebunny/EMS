package com.enfec.model;

import lombok.Data;

@Data
public class RefundRequest {
	
	private String charge_id;
    private int order_id;
//    private String user_type;
}

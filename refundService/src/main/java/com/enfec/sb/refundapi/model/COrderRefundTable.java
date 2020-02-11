package com.enfec.sb.refundapi.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Component
@Getter @Setter

public class COrderRefundTable {
	private int crefund_id; 
	private int corder_id; 
	private String crefund_description; 
	
	private Timestamp crefund_updated_time;
	
	private String crefund_status; 
}

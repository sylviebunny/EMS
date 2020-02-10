package com.enfec.sb.refundapi.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Component
@Getter @Setter
public class OOrderRefundTable {
	
	private int refund_id; 
	private int oorder_id; 
	private String description; 
	
	private Timestamp refund_created_time;
	
	private String refund_status; 
	
}

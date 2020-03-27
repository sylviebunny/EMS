package com.enfec.refundapi.model;

import java.sql.Timestamp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/************************************************
*
* Author: Heidi Huo
* Assignment: Organizer order refund table
* Class: OOrderRefundRowmapper
*
************************************************/
@Data
@Component
@Getter
@Setter
public class OOrderRefundTable {

    /**
     * Organizer refund id
     */
    private int refund_id;
    
    /**
     * Organizer order id
     */
    private int oorder_id;
    
    /**
     * Organizer refund description
     */
    private String description;
    
    /**
     * Organizer refund updated time
     */
    private Timestamp refund_updated_time;
    
    /**
     * Organizer refund status
     */
    private String refund_status;

    /**
     * Organizer refund stripe status
     */
    private String stripe_status; 
    
    /**
     * Organizer refund stripe id
     */
    private String stripe_refund_id; 
}

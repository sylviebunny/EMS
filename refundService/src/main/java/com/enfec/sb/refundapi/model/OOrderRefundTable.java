package com.enfec.sb.refundapi.model;

import java.sql.Timestamp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

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

}

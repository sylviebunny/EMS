package com.enfec.refundapi.model;

import java.sql.Timestamp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Component
@Getter
@Setter
public class COrderRefundTable {
    
    /**
     * Customer refund id
     */
    private int crefund_id;
    
    /**
     * Customer order id
     */
    private int corder_id;
    
    /**
     * Customer refund description
     */
    private String crefund_description;
    
    /**
     * Customer refund updated time
     */
    private Timestamp crefund_updated_time;
    
    /**
     * Customer refund status
     */
    private String crefund_status;
}

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

    private int refund_id;
    private int oorder_id;
    private String description;
    private Timestamp refund_updated_time;
    private String refund_status;

}

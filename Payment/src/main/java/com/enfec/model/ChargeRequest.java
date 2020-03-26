package com.enfec.model;

import lombok.Data;

@Data
public class ChargeRequest {

    private double amount;
    private String token;
    private int order_id;
    private String user_type;
}

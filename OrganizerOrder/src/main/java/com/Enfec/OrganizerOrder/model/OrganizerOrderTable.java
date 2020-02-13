package com.Enfec.OrganizerOrder.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component

@Getter
@Setter

public class OrganizerOrderTable {

    private int organizerOrderID;
    private int eventID;
    private int organizerID;
    private Timestamp dateTime;





}

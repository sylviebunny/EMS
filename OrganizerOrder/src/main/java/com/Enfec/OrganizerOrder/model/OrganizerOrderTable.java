package com.Enfec.OrganizerOrder.model;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component

public class OrganizerOrderTable {

    private int organizerOrderID;
    private int eventID;
    private int organizerID;
    private Timestamp dateTime;


    public int getOOrder_ID() {
        return organizerOrderID;
    }
    public void setOOrder_ID(int organizerOrderID) {
        this.organizerOrderID = organizerOrderID;
    }

    public int getEvent_ID() {
        return eventID;
    }
    public void setEvent_ID(int eventID) {
        this.eventID = eventID;
    }

    public int getOrganizer_ID() {
        return organizerID;
    }
    public void setOrganizer_ID(int organizerID) {
        this.organizerID = organizerID;
    }

    public Timestamp getTime() {
        return  dateTime;
    }
    public void setTime(Timestamp datetime) {
        this.dateTime = dateTime;
    }







}

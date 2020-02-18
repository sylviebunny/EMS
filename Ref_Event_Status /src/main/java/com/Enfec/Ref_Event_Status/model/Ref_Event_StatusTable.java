package com.Enfec.Ref_Event_Status.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;



@Data
@Component
@Getter
@Setter
public class Ref_Event_StatusTable {

    public int getEventStatusCode() {
        return eventStatusCode;
    }

    public String getEventStatusDescription() {
        return eventStatusDescription;
    }

    public void setEventStatusCode(int eventStatusCode) {
        this.eventStatusCode = eventStatusCode;
    }

    public void setEventStatusDescription(String eventStatusDescription) {
        this.eventStatusDescription = eventStatusDescription;
    }

    private int eventStatusCode;
    private String eventStatusDescription;




}

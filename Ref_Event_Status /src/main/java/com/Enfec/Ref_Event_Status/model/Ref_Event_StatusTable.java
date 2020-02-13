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

    public String getEventStatusCode() {
        return eventStatusCode;
    }

    public String getEventStatusDescription() {
        return eventStatusDescription;
    }

    public void setEventStatusCode(String eventStatusCode) {
        this.eventStatusCode = eventStatusCode;
    }

    public void setEventStatusDescription(String eventStatusDescription) {
        this.eventStatusDescription = eventStatusDescription;
    }

    private String eventStatusCode;
    private String eventStatusDescription;




}

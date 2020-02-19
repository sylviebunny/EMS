package com.Enfec.Ref_Event_Types.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;



@Data
@Component
@Getter
@Setter


public class Ref_Event_TypesTable {
    public int getEventTypeCode() {
        return eventTypeCode;
    }

    public String getEventTypeDescription() {
        return eventTypeDescription;
    }

    public void setEventTypeCode(int eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public void setEventTypeDescription(String eventTypeDescription) {
        this.eventTypeDescription = eventTypeDescription;
    }

    private int eventTypeCode;
    private String eventTypeDescription;


}

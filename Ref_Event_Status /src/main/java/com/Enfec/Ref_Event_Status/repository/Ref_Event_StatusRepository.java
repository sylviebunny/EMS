package com.Enfec.Ref_Event_Status.repository;

import com.Enfec.Ref_Event_Status.model.Ref_Event_StatusTable;

public interface Ref_Event_StatusRepository {

    public Object getRefEventStatus(int eventStatusCode);
    public int registerRefEventStatus(Ref_Event_StatusTable ref_event_statusTable);
    public int deleteRefEventStatus(int eventStatusCode);
    public int updateRefEventStatus(Ref_Event_StatusTable ref_event_statusTable);



}

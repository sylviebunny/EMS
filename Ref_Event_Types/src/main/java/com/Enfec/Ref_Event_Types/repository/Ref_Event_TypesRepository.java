package com.Enfec.Ref_Event_Types.repository;

import com.Enfec.Ref_Event_Types.model.Ref_Event_TypesTable;


public interface Ref_Event_TypesRepository {
    public Object getRefEventTypes(int eventTypeCode);
    public int registerRefEventTypes(Ref_Event_TypesTable ref_event_typesTable);
    public int deleteRefEventTypes(String eventTypeCode);
    public int updateRefEventTypes(Ref_Event_TypesTable ref_event_typesTable);

}

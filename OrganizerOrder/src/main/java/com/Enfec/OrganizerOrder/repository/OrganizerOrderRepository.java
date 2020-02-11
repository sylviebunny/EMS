package com.Enfec.OrganizerOrder.repository;

import com.Enfec.OrganizerOrder.model.OrganizerOrderTable;


public interface OrganizerOrderRepository {

    public Object getOrganizerOrder(int organizerOrderID);
    public int registerOrganizerOrder(OrganizerOrderTable organizerOrderTable);
    public int deleteOrganizerOrder(int organizerOrderID);



}

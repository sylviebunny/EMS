package com.Enfec.Ref_Event_Types.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import org.springframework.jdbc.core.RowMapper;

public class Ref_Event_TypesRowmapper implements RowMapper<Ref_Event_TypesTable> {

    @Override
    public Ref_Event_TypesTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ref_Event_TypesTable ref_event_typesTable = new Ref_Event_TypesTable();
        ref_event_typesTable.setEventTypeCode(rs.getString("Event_Type_Code"));
        ref_event_typesTable.setEventTypeDescription(rs.getString("Event_Type_Description"));
        return ref_event_typesTable;
    }








}

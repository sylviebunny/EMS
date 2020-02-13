package com.Enfec.Ref_Event_Status.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import org.springframework.jdbc.core.RowMapper;


public class Ref_Event_StatusRowmapper implements RowMapper<Ref_Event_StatusTable> {

    @Override
    public Ref_Event_StatusTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ref_Event_StatusTable ref_event_statusTable = new Ref_Event_StatusTable();
        ref_event_statusTable.setEventStatusCode(rs.getString("Event_Status_Code"));
        ref_event_statusTable.setEventStatusDescription(rs.getString("Event_Status_Description"));
        return ref_event_statusTable;


    }
}

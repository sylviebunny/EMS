package com.Enfec.OrganizerOrder.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;

public class OrganizerOrderRowmapper implements RowMapper<OrganizerOrderTable> {

    @Override
    public OrganizerOrderTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrganizerOrderTable organizerOrderTable = new OrganizerOrderTable();
        organizerOrderTable.setOrganizerOrderID(rs.getInt("OOrder_ID"));
        organizerOrderTable.setEventID(rs.getInt("Event_ID"));
        organizerOrderTable.setOrganizerID(rs.getInt("Organizer_ID"));
        organizerOrderTable.setDateTime(rs.getTimestamp("Time"));

        return organizerOrderTable;


    }

}

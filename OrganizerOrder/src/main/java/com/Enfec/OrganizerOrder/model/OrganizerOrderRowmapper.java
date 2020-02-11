package com.Enfec.OrganizerOrder.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;

public class OrganizerOrderRowmapper implements RowMapper<OrganizerOrderTable> {

    @Override
    public OrganizerOrderTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrganizerOrderTable organizerOrderTable = new OrganizerOrderTable();
        organizerOrderTable.setOOrder_ID(rs.getInt("OOrder_ID"));
        organizerOrderTable.setEventID(rs.getInt("Event_ID"));
        organizerOrderTable.setOrganizer_ID(rs.getInt("Organizer_ID"));
        organizerOrderTable.setTime(rs.getTimestamp("Time"));

        return organizerOrderTable;


    }

}

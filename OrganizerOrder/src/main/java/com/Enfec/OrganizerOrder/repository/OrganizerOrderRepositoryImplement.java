package com.Enfec.OrganizerOrder.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.Enfec.OrganizerOrder.model.OrganizerOrderTable;
import com.Enfec.OrganizerOrder.model.OrganizerOrderRowmapper;

@Component
public class OrganizerOrderRepositoryImplement implements OrganizerOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(OrganizerOrderRepositoryImplement.class);

    final String SELECT_ORGANIZER_ORDER = "SELECT OOrder_ID, Event_ID, Organizer_ID, TIME FROM Organizer_Orders where OOrder_ID = ?";
    final String REGISTER_ORGANIZER_ORDER = "INSERT INTO Organizer_Orders(OOrder_ID, Event_ID, Organizer_ID, Time) VALUES" + "(:organizerOrderID,:eventID,:organizerID,:dateTime)";
    final String DELETE_ORGANIZER_ORDER = "DELETE FROM Organizer_Orders WHERE OOrder_ID = ?;";


    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Map<String, Object> organizerOrderMap(OrganizerOrderTable organizerOrderTable) {
        Map<String, Object> param = new HashMap<>();
        if (organizerOrderTable.getOOrder_ID() != 0) {
            param.put("Organizer_Order_ID", organizerOrderTable.getOOrder_ID());
        } else {
            logger.error("Organizer order ID is missing");
            throw new NullPointerException("Organizer order ID cannot be null");
        }

        param.put("Event_ID ", organizerOrderTable.getEvent_ID() == 0 ? 0 : organizerOrderTable.getEvent_ID());
        param.put("Organizer_ID", organizerOrderTable.getOrganizer_ID() == 0 ? 0 : organizerOrderTable.getOrganizer_ID());
        param.put("Time", organizerOrderTable.getTime() == null ? null : organizerOrderTable.getTime());

        return param;

    }

    @Override
    public List<OrganizerOrderTable> getOrganizerOrder(int organizerOrderID) {
        return jdbcTemplate.query(SELECT_ORGANIZER_ORDER, new Object[] {organizerOrderID}, new OrganizerOrderRowmapper());

    }

    @Override
    public int registerOrganizerOrder(OrganizerOrderTable organizerOrderTable) {
        int affectedRow;
        Map<String, Object> param = organizerOrderMap(organizerOrderTable);

        SqlParameterSource paramSource = new MapSqlParameterSource(param);
        logger.info("Register Organizer Order Info: {} ", paramSource);
        affectedRow = namedParameterJdbcTemplate.update(REGISTER_ORGANIZER_ORDER, paramSource);

        return affectedRow;

    }
    @Override
    public int deleteOrganizerOrder(int organizerOrderID) {
        int affectedRow = jdbcTemplate.update(DELETE_ORGANIZER_ORDER, organizerOrderID);

        return affectedRow;
    }






}

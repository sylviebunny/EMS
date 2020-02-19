package com.Enfec.Ref_Event_Status.repository;

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

import com.Enfec.Ref_Event_Status.model.Ref_Event_StatusTable;
import com.Enfec.Ref_Event_Status.model.Ref_Event_StatusRowmapper;


@Component
public class Ref_Event_StatusRepositoryImplement implements Ref_Event_StatusRepository {
    private static final Logger logger = LoggerFactory.getLogger(Ref_Event_StatusRepositoryImplement.class);

    final String SELECT_REF_EVENT_STATUS = "SELECT Event_Status_Code, Event_Status_Description FROM Ref_Event_Status WHERE Event_Status_Code = ?";
    final String REGISTER_REF_EVENT_STATUS = "INSERT INTO Ref_Event_Status(Event_Status_Description) VALUES" + "(:eventStatusDescription)";
    final String DELETE_REF_EVENT_STATUS = "DELETE FROM Ref_Event_Status WHERE Event_Status_Code = ?";
    final String UPDATE_REF_EVENT_STATUS = "UPDATE Ref_Event_Status SET Event_Status_Description = :eventStatusDescription WHERE Event_Status_Code =:eventStatusCode";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Map<String, Object> refEventStatusMap(Ref_Event_StatusTable ref_event_statusTable) {
        Map<String, Object> param = new HashMap<>();

        param.put("eventStatusCode", ref_event_statusTable.getEventStatusCode());


        param.put("eventStatusDescription", ref_event_statusTable.getEventStatusDescription() == null ? null : ref_event_statusTable.getEventStatusDescription());

        return param;

    }

    @Override
    public List<Ref_Event_StatusTable> getRefEventStatus(int eventStatusCode) {
        return jdbcTemplate.query(SELECT_REF_EVENT_STATUS, new Object[] {eventStatusCode}, new Ref_Event_StatusRowmapper());

    }

    @Override
    public int registerRefEventStatus(Ref_Event_StatusTable ref_event_statusTable) {
        int affectedRow;
        Map<String, Object> param = refEventStatusMap(ref_event_statusTable);
        SqlParameterSource paramSource = new MapSqlParameterSource(param);
        logger.info("Create Ref Event Status: {}", paramSource);
        affectedRow = namedParameterJdbcTemplate.update(REGISTER_REF_EVENT_STATUS, paramSource);

        return affectedRow;
    }

    @Override
    public int deleteRefEventStatus(int eventStatusCode) {
        int affectedRow = jdbcTemplate.update(DELETE_REF_EVENT_STATUS, eventStatusCode);
        return affectedRow;
    }

    @Override
    public int updateRefEventStatus(Ref_Event_StatusTable ref_event_statusTable) {
        int affectedRow;
        Map<String, Object> param = refEventStatusMap(ref_event_statusTable);
        SqlParameterSource pramSource = new MapSqlParameterSource(param);
        logger.info("Updating Ref Event Status: {}", pramSource);
        affectedRow = namedParameterJdbcTemplate.update(UPDATE_REF_EVENT_STATUS, pramSource);
        return affectedRow;

    }










}




















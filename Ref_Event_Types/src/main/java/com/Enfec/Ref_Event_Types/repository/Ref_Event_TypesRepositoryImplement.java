package com.Enfec.Ref_Event_Types.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Enfec.Ref_Event_Types.model.Ref_Event_TypesRowmapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.Enfec.Ref_Event_Types.model.Ref_Event_TypesTable;

@Component
public class Ref_Event_TypesRepositoryImplement implements Ref_Event_TypesRepository {
    private static final Logger logger = LoggerFactory.getLogger(Ref_Event_TypesRepositoryImplement.class);

    final String SELECT_REF_EVENT_TYPES = "SELECT Event_Type_Code, Event_Type_Description FROM Ref_Event_Types WHERE Event_Type_Code = ?";
    final String REGISTER_REF_EVENT_TYPES = "INSERT INTO Ref_Event_Types(Event_Type_Code, Event_Type_Description) VALUES" + "(:eventTypeCode, :eventTypeDescription)";
    final String DELETE_REF_EVENT_TYPES = "DELETE FROM Ref_Event_Types WHERE Event_Type_Code = ?";
    final String UPDATE_REF_EVENT_TYPES = "UPDATE Ref_Event_Types SET Event_Type_Description = :eventTypeDescription WHERE Event_Type_Code =:eventTypeCode";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Map<String, Object> refEventTypesMap(Ref_Event_TypesTable ref_event_typesTable) {
        Map<String, Object> param = new HashMap<>();
        if (ref_event_typesTable.getEventTypeCode() != null) {
            param.put("eventTypeCode", ref_event_typesTable.getEventTypeCode());
        } else {
            logger.error("There is no Event Type Code.");
            throw new NullPointerException("Event Type Code cannot be null.");
        }

        param.put("eventTypeDescription", ref_event_typesTable.getEventTypeDescription() == null ? null : ref_event_typesTable.getEventTypeDescription());

        return param;

    }

    @Override
    public List<Ref_Event_TypesTable> getRefEventTypes(String eventTypeCode) {
        return jdbcTemplate.query(SELECT_REF_EVENT_TYPES, new Object[] {eventTypeCode}, new Ref_Event_TypesRowmapper());

    }

    @Override
    public int registerRefEventTypes(Ref_Event_TypesTable ref_event_typesTable) {
        int affectedRow;
        Map<String, Object> param = refEventTypesMap(ref_event_typesTable);
        SqlParameterSource paramSource = new MapSqlParameterSource(param);
        logger.info("Create Ref Event Types: {}", paramSource);
        affectedRow = namedParameterJdbcTemplate.update(REGISTER_REF_EVENT_TYPES, paramSource);

        return affectedRow;
    }

    @Override
    public int deleteRefEventTypes(String eventTypeCode) {
        int affectedRow = jdbcTemplate.update(DELETE_REF_EVENT_TYPES, eventTypeCode);
        return affectedRow;
    }

    @Override
    public int updateRefEventTypes(Ref_Event_TypesTable ref_event_typesTable) {
        int affectedRow;
        Map<String, Object> param = refEventTypesMap(ref_event_typesTable);
        SqlParameterSource pramSource = new MapSqlParameterSource(param);
        logger.info("Updating Ref Event Types: {}", pramSource);
        affectedRow = namedParameterJdbcTemplate.update(UPDATE_REF_EVENT_TYPES, pramSource);
        return affectedRow;

    }












}

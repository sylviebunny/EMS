package com.Enfec.Ref_Event_Types.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Enfec.Ref_Event_Types.model.Ref_Event_TypesTable;
import com.Enfec.Ref_Event_Types.repository.Ref_Event_TypesRepositoryImplement;
import com.google.gson.Gson;


@RestController
public class Ref_Event_TypesController {
    private static final Logger logger = LoggerFactory.getLogger(Ref_Event_TypesController.class);

    @Autowired
    Ref_Event_TypesRepositoryImplement ref_event_typesRepositoryImplement;
    @RequestMapping(value = "/getrefeventtypes/{eventTypeCode}", method = RequestMethod.GET, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> getRefEventTypesList(@PathVariable int eventTypeCode) {
        try {
            List<Ref_Event_TypesTable> refEventTypesList = ref_event_typesRepositoryImplement.getRefEventTypes(eventTypeCode);
            if (refEventTypesList.isEmpty()) {
                logger.info("No Ref Event Types found for: {}", eventTypeCode);
                return new ResponseEntity<>("{\"message\" : \"There is no Ref Event Types found\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new Gson().toJson((ref_event_typesRepositoryImplement.getRefEventTypes(eventTypeCode))), HttpStatus.OK);

            }
        } catch (Exception e) {
            logger.error("Exception in getting Ref Event Types: {}", e.getMessage());
            return new ResponseEntity<>(
                    "{\"message\" : \"Exception in getting Ref Event Types\"}", HttpStatus.OK.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(value = "/registerrefeventtypes", method = RequestMethod.POST, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> registerRefEventTypes(
            @RequestBody(required = true) Ref_Event_TypesTable ref_event_typesTable) {

        try {
            int affectedRow = ref_event_typesRepositoryImplement.registerRefEventTypes(ref_event_typesTable);
            if (affectedRow == 0) {
                logger.info("Event type code not registered event type description: {}", ref_event_typesTable.getEventTypeCode());
                return new ResponseEntity<>("{\"message\" : \"Event type code not registered\"}", HttpStatus.OK);
            } else {
                logger.info("Event type code is registered: {}", ref_event_typesTable.getEventTypeCode());
                return new ResponseEntity<>("{\"message\" : \"Event type code is created\"}", HttpStatus.OK);
            }
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            logger.error("Invalid event type code: {} ", ref_event_typesTable.getEventTypeCode());
            return new ResponseEntity<>("{\"message\" : \"Invalid event type code\"}", HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            logger.error("Exception in creating event type code:{}", exception.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Exception in registering event type code\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/refeventtypes/delete/{eventTypeCode}", method = RequestMethod.DELETE, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> deleteEventTypeCode(@PathVariable("eventTypeCode") String code) {
        int affectedRow = ref_event_typesRepositoryImplement.deleteRefEventTypes(code);
        if (affectedRow > 0) {
            return new ResponseEntity<>(
                    "{\"message\" : \"Event type code deleted\"}", HttpStatus.OK);

        } else {
            return new ResponseEntity<>(
                    "{\"message\" : \"Event type code is not able to delete\"}", HttpStatus.OK
            );
        }
    }


    @RequestMapping(value = "/updaterefeventtypes", method = RequestMethod.PUT, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> updateRefEventTypes(
            @RequestBody(required = true) Ref_Event_TypesTable ref_event_typesTable) {
        try {
            int affectedRow = ref_event_typesRepositoryImplement.updateRefEventTypes(ref_event_typesTable);
            if (affectedRow == 0) {
                logger.info("Ref_Event_Types is not updated: {} ", ref_event_typesTable.getEventTypeCode());
                return new ResponseEntity<>(
                        "{\"message\" : \"Ref_Event_Types not found\"}", HttpStatus.OK
                );


            } else {
                logger.info("Event type code is updated: {} ", ref_event_typesTable.getEventTypeCode());
                return new ResponseEntity<>(
                        "{\"message\" : \"Ref event types updated\"}", HttpStatus.OK);
            }

        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            logger.error("Invalid event type code: {} ", ref_event_typesTable.getEventTypeCode());
            return new ResponseEntity<>("{\"message\" : \"Invalid Event Types Code\"}", HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            logger.error("Exception in updating Ref event types: {}", exception.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Exception in updating Ref Event Types\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

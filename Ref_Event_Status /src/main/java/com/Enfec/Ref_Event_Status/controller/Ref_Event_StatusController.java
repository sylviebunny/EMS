package com.Enfec.Ref_Event_Status.controller;


import java.util.List;


import com.Enfec.Ref_Event_Status.repository.Ref_Event_StatusRepository;
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

import com.Enfec.Ref_Event_Status.model.Ref_Event_StatusTable;
import com.Enfec.Ref_Event_Status.repository.Ref_Event_StatusRepositoryImplement;
import com.google.gson.Gson;


@RestController

public class Ref_Event_StatusController {

    private static final Logger logger = LoggerFactory.getLogger(Ref_Event_StatusController.class);

    @Autowired
    Ref_Event_StatusRepositoryImplement ref_event_statusRepositoryImplement;
    @RequestMapping(value = "/getrefeventstatus/{eventStatusCode}", method = RequestMethod.GET, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> getRefEventStatusList(@PathVariable int eventStatusCode) {
        try {
            List<Ref_Event_StatusTable> refEventStatusList = ref_event_statusRepositoryImplement.getRefEventStatus(eventStatusCode);
            if (refEventStatusList.isEmpty()) {
                logger.info("No Ref Event Status found for: {}", eventStatusCode);
                return new ResponseEntity<>("{\"message\" : \"There is no Ref Event Status found\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new Gson().toJson((ref_event_statusRepositoryImplement.getRefEventStatus(eventStatusCode))), HttpStatus.OK);

            }
        } catch (Exception e) {
            logger.error("Exception in getting Ref Event Status: {}", e.getMessage());
            return new ResponseEntity<>(
                    "{\"message\" : \"Exception in getting Ref Event Status\"}", HttpStatus.OK.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(value = "/registerrefeventstatus", method = RequestMethod.POST, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> registerRefEventStatus(
            @RequestBody(required = true) Ref_Event_StatusTable ref_event_statusTable) {

        try {
            int affectedRow = ref_event_statusRepositoryImplement.registerRefEventStatus(ref_event_statusTable);
            if (affectedRow == 0) {
                logger.info("Event status code not registered event status description: {}", ref_event_statusTable.getEventStatusCode());
                return new ResponseEntity<>("{\"message\" : \"Event status code not registered\"}", HttpStatus.OK);
            } else {
                logger.info("Event status code is registered: {}", ref_event_statusTable.getEventStatusCode());
                return new ResponseEntity<>("{\"message\" : \"Event status code is created\"}", HttpStatus.OK);
            }
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            logger.error("Invalid event status code: {} ", ref_event_statusTable.getEventStatusCode());
            return new ResponseEntity<>("{\"message\" : \"Invalid event status code\"}", HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            logger.error("Exception in creating event status code:{}", exception.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Exception in registering event status code\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/refeventstatus/delete/{eventStatusCode}", method = RequestMethod.DELETE, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> deleteEventStatusCode(@PathVariable("eventStatusCode") int code) {
        int affectedRow = ref_event_statusRepositoryImplement.deleteRefEventStatus(code);
        if (affectedRow > 0) {
            return new ResponseEntity<>(
                    "{\"message\" : \"Event status code deleted\"}", HttpStatus.OK);

        } else {
            return new ResponseEntity<>(
                    "{\"message\" : \"Event status code is not able to delete\"}", HttpStatus.OK
            );
        }
    }


    @RequestMapping(value = "/updaterefeventstatus", method = RequestMethod.PUT, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> updateRefEventStatus(
            @RequestBody(required = true) Ref_Event_StatusTable ref_event_statusTable) {
        try {
            int affectedRow = ref_event_statusRepositoryImplement.updateRefEventStatus(ref_event_statusTable);
            if (affectedRow == 0) {
                logger.info("Ref_Event_Status is not updated: {} ", ref_event_statusTable.getEventStatusCode());
                return new ResponseEntity<>(
                        "{\"message\" : \"Ref_Event_Status not found\"}", HttpStatus.OK
                );


            } else {
                logger.info("Event status code is updated: {} ", ref_event_statusTable.getEventStatusCode());
                return new ResponseEntity<>(
                        "{\"message\" : \"Ref event status updated\"}", HttpStatus.OK);
            }

        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            logger.error("Invalid event status code: {} ", ref_event_statusTable.getEventStatusCode());
            return new ResponseEntity<>("{\"message\" : \"Invalid Event Status Code\"}", HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            logger.error("Exception in updating Ref event status: {}", exception.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Exception in updating Ref Event Status\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }








}



















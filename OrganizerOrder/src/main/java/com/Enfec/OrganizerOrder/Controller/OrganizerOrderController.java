package com.Enfec.OrganizerOrder.Controller;

import java.util.List;

import com.Enfec.OrganizerOrder.repository.OrganizerOrderRepository;
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

import com.Enfec.OrganizerOrder.model.OrganizerOrderTable;
import com.Enfec.OrganizerOrder.repository.OrganizerOrderRepositoryImplement;
import com.google.gson.Gson;

@RestController

public class OrganizerOrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizerOrderController.class);

    @Autowired
    OrganizerOrderRepositoryImplement organizerOrderRepositoryImplement;

    @RequestMapping(value = "/getorganizerorder-info/{organizerOrderID}", method = RequestMethod.GET, produces = "application/json;charset = UTF-8")
    public ResponseEntity<String> getOrganizerOrderList(@PathVariable int organizerOrderID) {

        try {
            List<OrganizerOrderTable> organizerOrderlist = organizerOrderRepositoryImplement.getOrganizerOrder(organizerOrderID);
            if (organizerOrderlist.isEmpty()) {
                logger.info("No organizer order is found for:{}", organizerOrderID);
                return new ResponseEntity<>("{\"message\" : \"No organizer order found\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new Gson().toJson((organizerOrderRepositoryImplement.getOrganizerOrder(organizerOrderID))), HttpStatus.OK);
            }


        } catch (Exception e) {
            logger.error("Exception in getting organizer order info: {}", e.getMessage());
            return new ResponseEntity<>(
                    "{\"message\" : \"Exception in getting organizer info\"}", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


        @RequestMapping(value = "/registerorganizerorder", method = RequestMethod.POST, produces = "application/json;charset = UTF-8")
        public ResponseEntity<String> registerOrganizerOrder (
                @RequestBody(required = true) OrganizerOrderTable organizerOrderTable) {

            try {
                int affectedRow = organizerOrderRepositoryImplement.registerOrganizerOrder(organizerOrderTable);
                if (affectedRow == 0) {
                    logger.info("Organizer order not registered event_ID: {} ", organizerOrderTable.getEventID());
                    return new ResponseEntity<>("{\"message\" : \"Organizer order not registered\"}", HttpStatus.OK);
                } else {
                    logger.info("Organizer registered event_ID: {}", organizerOrderTable.getEventID());
                    return new ResponseEntity<>("{\"message\" : \"Organizer order made\"}", HttpStatus.OK);
                }
            } catch (DataIntegrityViolationException dataIntegrityViolationException) {
                logger.error("Invalid Event_ID: {} ", organizerOrderTable.getEventID());
                return new ResponseEntity<>("{\"MESSAGE\" : \"Invalid Event_ID\"}", HttpStatus.BAD_REQUEST);

            } catch (Exception exception) {
                logger.error("Exception in creating organizer order:{}", exception.getMessage());
                return new ResponseEntity<>(
                        "{\"message\" : \"Exception in registering organizer order\"}", HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }

            @RequestMapping(value = "/organizerorder/delete/{organizerOrderID}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
            public ResponseEntity<String> deleteOrganizerOrder(@PathVariable("organizerOrderID") int id) {
                int affectedRow = organizerOrderRepositoryImplement.deleteOrganizerOrder(id);
                if (affectedRow > 0) {
                    return new ResponseEntity<>(
                            "{\"message\" : \"Organizer order deleted\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "{\"message\" : \"Organizer order is not able to delete\"}", HttpStatus.OK);

                }
            }


    }
















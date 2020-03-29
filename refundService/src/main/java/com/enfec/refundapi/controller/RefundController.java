package com.enfec.refundapi.controller;

import com.enfec.refundapi.model.COrderRefundTable;
import com.enfec.refundapi.model.OOrderRefundTable;
import com.enfec.refundapi.repository.RefundRepositoryImpl;
import com.google.gson.Gson;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/************************************************
*
* Author: Heidi Huo
* Assignment: Controller class for refund APIs
* Class: RefundController
*
************************************************/
@CrossOrigin
@RestController
public class RefundController {

    private static final Logger logger = LoggerFactory.getLogger(RefundController.class); 
    
    @Autowired
    RefundRepositoryImpl refundRepositoryImpl;
    
    /** 
     * Get organizer refunds by organizer id
     * @param organizer_id
     * @return ResponseEntity with message and data 
     */
    @RequestMapping(value = "/organizer_refund/get_organizer_refund/{organizer_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getOrganzerRefundByOrganizerID(@PathVariable int organizer_id) {
        try {
            List<OOrderRefundTable> organizerRefundList = refundRepositoryImpl.getOrganizerRefundByOrganizerID(organizer_id); 
            
            if (organizerRefundList == null || organizerRefundList.size() == 0) {
                logger.info("No organizer refund found for customer_id: " + organizer_id);
                return new ResponseEntity<>("{\"message\" : \"No organizer refund found \"}", 
                        HttpStatus.OK); 
            } else {
                logger.info(String.format("%s organizer refund found for customer id: %s", organizerRefundList.size(), organizer_id));
                return new ResponseEntity<>(new Gson().toJson(organizerRefundList), 
                        HttpStatus.OK); 
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Input organizer id: {}", organizer_id);
            logger.error("Exception info in searching otganizer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get organizer refund information from database by organizer refund id
     * 
     * @param organizer_refund_id. Cannot be null and must be positive
     * @return ResponseEntity with message and data 
     */
    @RequestMapping(value = "/organizer_refund/search/{organizer_refund_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getOrganizerRefundByRefundID(
            @PathVariable int organizer_refund_id) {
        try {
            List<OOrderRefundTable> organizerRefundList =
                    refundRepositoryImpl.getOrganizerRefundByRefundID(organizer_refund_id);

            if (organizerRefundList == null || organizerRefundList.isEmpty()) {
                logger.info("No refund found, organizer refund id: {}", organizer_refund_id);
                return new ResponseEntity<>("{\"message\" : \"No refund found\"}", HttpStatus.OK);
            } else {
                logger.info("organizer refund found based on organizer refund id: {}", organizer_refund_id);
                return new ResponseEntity<>(new Gson().toJson(
                        (refundRepositoryImpl.getOrganizerRefundByRefundID(organizer_refund_id))),
                        HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Exception info in searching organizer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get organizer refund information from database by organizer order id
     * 
     * @param organizer_order_id. Cannot be null and must be positive
     * @return ResponseEntity with message and data 
     */
    @RequestMapping(value = "/organizer_refund/search/organizer_order_id/{oorder_id}",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getOrganizerRefundByOorder(@PathVariable int oorder_id) {

        try {
            List<OOrderRefundTable> organizerRefundList =
                    refundRepositoryImpl.getOrganizerRefundByOorderID(oorder_id);

            if (organizerRefundList == null || organizerRefundList.isEmpty()) {
                logger.info("No organizer refund found based on organizer order id: {}", oorder_id);
                return new ResponseEntity<>("{\"message\" : \"No organizer refund found\"}", HttpStatus.OK);
            } else {
                logger.info("Organizer refund found by organizer order id: {}", oorder_id);
                return new ResponseEntity<>(new Gson().toJson(organizerRefundList),
                        HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Exception in searching organizer refund: " + e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create an organizer refund and put information to database
     * 
     * @param organizerRefundTable. Organizer_order_id Cannot be null and must exist in database; 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/organizer_refund/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> createOrganizerRefund(
            @RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
        try {
            String newORefundID = refundRepositoryImpl.createOrganizerRefund(organizerRefundTable);

            if (newORefundID == null || newORefundID.length() == 0) {
                logger.info("Organizer refund not created, organizer order id: {}", organizerRefundTable.getOorder_id());
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not created\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Organizer refund successfully created");
                return new ResponseEntity<>(String.format("{\"message\" : \"Organizer order refund successfully created, refund ID: %s\"}", newORefundID),
                        HttpStatus.OK);
            }
        } catch (DuplicateKeyException d) {
            logger.error("Organizer order refund duplicate created");
            return new ResponseEntity<>("{\"message\" : \"Organizer order refund already created, cannot create again\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException di) {
            logger.error("Invalid input: {}", organizerRefundTable.getOorder_id());
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Exception info in creating organizer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update organizer refund information 
     * 
     * @param organizerRefundTable. Organizer_refund_id Cannot be null and must exist in database; 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/organizer_refund/update", method = RequestMethod.PUT,
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateOrganizerRefund(
            @RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
        try {
            int affectedRow = refundRepositoryImpl.updateOrganizerRefund(organizerRefundTable);

            if (affectedRow == 0) {
                logger.info("Organizer refund not updated, organizer refund id: " + organizerRefundTable.getRefund_id());
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not updated\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Organizer successfully updated, organizer refund id: " + organizerRefundTable.getRefund_id());
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully updated\"}", HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException di) {
            logger.error("Invalid input, organizer refund id: " + organizerRefundTable.getRefund_id()); 
            logger.error("Exceptioin message: {}", di.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Exception info in updating organizer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete organizer refund
     * 
     * @param organizer_refund_id. Cannot be null and must be positive
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/organizer_refund/delete/{organizer_refund_id}",
            method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> deleteOrganizerRefund(@PathVariable int organizer_refund_id) {
        try {
            int affectedRow = refundRepositoryImpl.deleteOrganizerRefund(organizer_refund_id);

            if (affectedRow == Integer.MIN_VALUE) {
                logger.info("Organizer refund not found, organizer refund id: {}", organizer_refund_id);
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not found\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Organizer refund successfully deleted, organizer refund id: {}", organizer_refund_id);
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully deleted\"}", HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Exception in deleting organizer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get customer refunds by customer id
     * @param customer_id. Cannot be null and must be positive
     * @return ResponseEntity with message and data
     */
    @RequestMapping(value = "/customer_refund/get_customer_refund/{customer_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getCustomerRefundByCustomerID(@PathVariable int customer_id) {
        try {
            List<COrderRefundTable> customerRefundList = refundRepositoryImpl.getCustomerRefundByCustomerID(customer_id); 
            
            if (customerRefundList == null || customerRefundList.size() == 0) {
                logger.info("No customer refund found for customer_id: " + customer_id);
                return new ResponseEntity<>("{\"message\" : \"No customer refund found \"}", 
                        HttpStatus.OK); 
            } else {
                logger.info(String.format("%s customer refund found for customer id: %s", customerRefundList.size(), customer_id));
                return new ResponseEntity<>(new Gson().toJson(customerRefundList), 
                        HttpStatus.OK); 
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Input customer id: {}", customer_id);
            logger.error("Exception info in searching customer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
        
    /**
     * Get customer refund by customer refund id
     * 
     * @param customer_refund_id. Cannot be null and must be positive
     * @return ResponseEntity with message and data
     */
    @RequestMapping(value = "/customer_refund/search/{customer_refund_id}",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getCustomerRefundByRefundID(
            @PathVariable int customer_refund_id) {

        try {
            List<COrderRefundTable> customerRefundList =
                    refundRepositoryImpl.getCustomerRefundByCRefundID(customer_refund_id);

            if (customerRefundList == null || customerRefundList.isEmpty()) {
                logger.info("No customer refund found by customer refund id: {}", customer_refund_id);
                return new ResponseEntity<>("{\"message\" : \"No customer refund found\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Customer refund found by customer refund id: {}", customer_refund_id);
                return new ResponseEntity<>(new Gson().toJson(customerRefundList),
                        HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Input customer refund id: {}", customer_refund_id);
            logger.error("Exception info in searching customer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get customer refund by customer order id
     * 
     * @param customer_order_id. Cannot be null and must be positive
     * @return ResponseEntity with message and data
     */
    @RequestMapping(value = "/customer_refund/search/customer_order_id/{corder_id}",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getCustomerRefundByCorderID(@PathVariable int corder_id) {

        try {
            List<COrderRefundTable> customerRefundList =
                    refundRepositoryImpl.getCustomerRefundByCorderID(corder_id);

            if (customerRefundList == null || customerRefundList.isEmpty()) {
                logger.info("No customer refund found by customer order id: " + corder_id);
                return new ResponseEntity<>("{\"message\" : \"No customer refund found\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Customer refund found by customer order id: " + corder_id);
                return new ResponseEntity<>(new Gson().toJson(customerRefundList),
                        HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Input cutomer order id: " + corder_id);
            logger.error("Exception info in searching customer refund: " + e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a customer refund and insert information into database
     * 
     * @param CustomerRefundTable. Customer_order_id cannot be null and must exist in database; 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/customer_refund/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> createCustomerRefund(
            @RequestBody(required = true) COrderRefundTable customerRefund) {
        try {
            String newCRefund_ID = refundRepositoryImpl.createCustomerRefund(customerRefund);

            if (newCRefund_ID == null || newCRefund_ID.length() == 0) {
                logger.info("Customer refund not created");
                return new ResponseEntity<>("{\"message\" : \"Customer refund not created\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Customer refund successfully created for customer order id: " + customerRefund.getCorder_id());
                return new ResponseEntity<>(String.format("{\"message\" : \"Customer order refund successfully created\"}"),
                        HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DuplicateKeyException d) {
            logger.error("Customer order refund duplicate created");
            return new ResponseEntity<>("{\"message\" : \"Customer order refund already created, cannot create again\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException di) {
            logger.error("Invalid input");
            logger.error("Customer order id: {}", customerRefund.getCorder_id());
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Customer order id: {}", customerRefund.getCorder_id());
            logger.error("Exception info in creating customer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an customer refund information
     * 
     * @param CustomerRefundTable. Customer_refund_id cannot be null and must exist in database;  
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/customer_refund/update", method = RequestMethod.PUT,
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateCustomerRefund(
            @RequestBody(required = true) COrderRefundTable customerRefund) {
        try {
            int affectedRow = refundRepositoryImpl.updateCustomerRefund(customerRefund);

            if (affectedRow == 0) {
                logger.info("Customer refund not updated, customer refund id: {}", customerRefund.getCrefund_id());
                return new ResponseEntity<>("{\"message\" : \"Customer refund not updated\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Customer refund successfully updated, customer refund id: {}", customerRefund.getCrefund_id());
                return new ResponseEntity<>(
                        "{\"message\" : \"Customer refund successfully updated\"}", HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException di) {
            logger.error("Invalid input");
            logger.error("Input customer refund id: {}", customerRefund.getCrefund_id());
            logger.error("Input customer order id: {}", customerRefund.getCorder_id());
            logger.error("Exception message: ", di.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Exception info in updating customer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a customer refund
     * 
     * @param customer_refund_id. Cannot be null and must be positive 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/customer_refund/delete/{customer_refund_id}",
            method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> deleteCustomerRefund(@PathVariable int customer_refund_id) {
        try {
            int affectedRow = refundRepositoryImpl.deleteCustomerRefund(customer_refund_id);

            if (affectedRow == Integer.MIN_VALUE) {
                logger.info("Customer refund not found by customer refund id: {}", customer_refund_id);
                return new ResponseEntity<>("{\"message\" : \"Customer refund not found\"}",
                        HttpStatus.OK);
            } else {
                logger.info("Customer refund successfully deleted: {}", customer_refund_id);
                return new ResponseEntity<>(
                        "{\"message\" : \"Customer refund successfully deleted\"}", HttpStatus.OK);
            }
        } catch (CannotGetJdbcConnectionException ce) {
            logger.error("Fail to connect database: {}", ce.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Fail to connect database\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Exception info in deleting customer refund: {}", e.getMessage());
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

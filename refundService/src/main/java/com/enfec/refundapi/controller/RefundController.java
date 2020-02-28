package com.enfec.refundapi.controller;

import com.enfec.refundapi.model.COrderRefundTable;
import com.enfec.refundapi.model.OOrderRefundTable;
import com.enfec.refundapi.repository.RefundRepositoryImpl;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is controller class for refund APIs
 * @author Heidi Huo
 */
@CrossOrigin
@RestController
public class RefundController {

    @Autowired
    RefundRepositoryImpl refundRepositoryImpl;

    /**
     * Get organizer refund information from database by organizer refund id
     * 
     * @param organizer_refund_id. Cannot be null and must be positive
     * @return ResponseEntity with message and data 
     */
    @RequestMapping(value = "/organizer_refund/search/{organizer_refund_id}",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> getOrganizerRefundByRefundID(
            @PathVariable int organizer_refund_id) {

        try {
            List<OOrderRefundTable> organizerRefundList =
                    refundRepositoryImpl.getOrganizerRefundByRefundID(organizer_refund_id);

            if (organizerRefundList == null || organizerRefundList.isEmpty()) {
                return new ResponseEntity<>("{\"message\" : \"No refund found\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Gson().toJson(
                        (refundRepositoryImpl.getOrganizerRefundByRefundID(organizer_refund_id))),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
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
                return new ResponseEntity<>("{\"message\" : \"No refund found\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new Gson().toJson(
                                (refundRepositoryImpl.getOrganizerRefundByOorderID(oorder_id))),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create an organizer refund and put information to database
     * 
     * @param organizerRefundTable. Organizer_order_id Cannot be null and must exist in database; 
     * Description can be null 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/organizer_refund/create", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> createOrganizerRefund(
            @RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
        try {
            int affectedRow = refundRepositoryImpl.createOrganizerRefund(organizerRefundTable);

            if (affectedRow == 0) {
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not created\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully registered\"}",
                        HttpStatus.OK);
            }
        } catch (DataIntegrityViolationException di) {
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update organizer refund information 
     * 
     * @param organizerRefundTable. Organizer_refund_id Cannot be null and must exist in database; 
     * Description can be null; Organizer refund status can be null 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/organizer_refund/update", method = RequestMethod.PUT,
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateOrganizerRefund(
            @RequestBody(required = true) OOrderRefundTable organizerRefundTable) {
        try {
            int affectedRow = refundRepositoryImpl.updateOrganizerRefund(organizerRefundTable);

            if (affectedRow == 0) {
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not updated\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully updated\"}", HttpStatus.OK);
            }
        } catch (DataIntegrityViolationException di) {
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
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
                // Didn't find this event by event_id
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not found\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully deleted\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get customer refund
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
                return new ResponseEntity<>("{\"message\" : \"No customer refund found\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Gson().toJson(
                        (refundRepositoryImpl.getCustomerRefundByCRefundID(customer_refund_id))),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get customer refund
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
                return new ResponseEntity<>("{\"message\" : \"No customer refund found\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new Gson().toJson(
                                (refundRepositoryImpl.getCustomerRefundByCorderID(corder_id))),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create an organizer refund and insert information into database
     * 
     * @param CustomerRefundTable. Customer_order_id cannot be null and must exist in database; 
     * Customer_description can be null. 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/customer_refund/create", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> createCustomerRefund(
            @RequestBody(required = true) COrderRefundTable customerRefund) {
        try {
            int affectedRow = refundRepositoryImpl.createCustomerRefund(customerRefund);

            if (affectedRow == 0) {
                return new ResponseEntity<>("{\"message\" : \"Customer refund not created\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        "{\"message\" : \"Customer refund successfully registered\"}",
                        HttpStatus.OK);
            }
        } catch (DataIntegrityViolationException di) {
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an organizer refund information
     * 
     * @param CustomerRefundTable. Customer_refund_id cannot be null and must exist in database; 
     * Customer_description can be null; Customer_refund_status can be null. 
     * @return ResponseEntity with message
     */
    @RequestMapping(value = "/customer_refund/update", method = RequestMethod.PUT,
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> updateCustomerRefund(
            @RequestBody(required = true) COrderRefundTable customerRefund) {
        try {
            int affectedRow = refundRepositoryImpl.updateCustomerRefund(customerRefund);

            if (affectedRow == 0) {
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not updated\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully updated\"}", HttpStatus.OK);
            }
        } catch (DataIntegrityViolationException di) {
            return new ResponseEntity<>("{\"message\" : \"Invalid input, lack of data\"}",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete an organizer refund
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
                // Didn't find this event by event_id
                return new ResponseEntity<>("{\"message\" : \"Organizer refund not found\"}",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        "{\"message\" : \"Organizer refund successfully deleted\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("{\"message\" : \"Unknown error\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

package com.enfec.refundapi.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.enfec.refundapi.model.COrderRefundRowmapper;
import com.enfec.refundapi.model.COrderRefundTable;
import com.enfec.refundapi.model.OOrderRefundRowmapper;
import com.enfec.refundapi.model.OOrderRefundTable;

/**
 * @author heidi huo
 * 
 */
@Component
@Transactional
public class RefundRepositoryImpl implements RefundRepository {
    private static final String DELETE_ORGANIZER_REFUND = "DELETE FROM Refund WHERE Refund_ID =?";

    private static final String SELECT_ORGANIZER_REFUND = "SELECT * FROM Refund WHERE Refund_ID =?";

    private static final String CREATE_ORGANIZER_REFUND =
            "INSERT INTO Refund (OOrder_ID, Description, Refund_Updated_Time, Refund_Status)"
                    + "VALUES(:oorder_id, :description, :refund_updated_time, :refund_status)";

    private static final String UPDATE_ORGANIZER_REFUND =
            "UPDATE evntmgmt_usa.Refund SET Refund.Refund_Status = :refund_status, "
                    + "Refund.Description = :description, Refund_Updated_Time = :refund_updated_time WHERE Refund.Refund_ID = :refund_id;";

    private static final String SELECT_ORGANIZER_REFUND_BY_OORDER_ID =
            "SELECT * FROM Refund WHERE OOrder_ID =?";

    private static final String CREATE_CUSTOMER_REFUND =
            "INSERT INTO Customer_Refund (COrder_ID, CRefund_Description, CRefund_Updated_Time, CRefund_Status)"
                    + "VALUES(:corder_id, :crefund_description, :crefund_updated_time, :crefund_status)";

    private static final String DELETE_CUSTOMER_REFUND =
            "DELETE FROM Customer_Refund WHERE CRefund_ID =?";

    private static final String SELECT_CUSTOMER_REFUND_BY_CREFUND_ID =
            "SELECT * FROM Customer_Refund WHERE CRefund_ID =?";

    private static final String SELECT_CUSTOMER_REFUND_BY_CORDER_ID =
            "SELECT * FROM Customer_Refund WHERE COrder_ID =?";

    private static final String UPDATE_CUSTOMER_REFUND =
            "UPDATE Customer_Refund SET CRefund_Status = :crefund_status, "
                    + "CRefund_Description = :crefund_description, CRefund_Updated_Time = :crefund_updated_time WHERE CRefund_ID = :crefund_id;";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    /**
     * Get organizer refund information from database by organizer refund id
     * 
     * @param refund_id: organizer_refund_id, cannot be null and must be positive
     * @return List<OOrderRefundTable>: all entries that match refund_id
     */
    @Override
    public List<OOrderRefundTable> getOrganizerRefundByRefundID(int refund_id) {
        return jdbcTemplate.query(SELECT_ORGANIZER_REFUND, new Object[] {refund_id},
                new OOrderRefundRowmapper());
    }

    /**
     * Get organizer refund information from database by organizer order id
     * 
     * @param oorder_id: organizer_order_id, cannot be null and must be positive
     * @return List<OOrderRefundTable>: all entries that match oorder_id
     */
    @Override
    public List<OOrderRefundTable> getOrganizerRefundByOorderID(int oorder_id) {
        return jdbcTemplate.query(SELECT_ORGANIZER_REFUND_BY_OORDER_ID, new Object[] {oorder_id},
                new OOrderRefundRowmapper());
    }

    /**
     * Create organizer refund information
     * 
     * Map organizer refund table to MySql information content, and insert into MySql
     * @param OOrderRefundTable: Organizer_refund_table
     * @return int: affectedRow
     */
    @Override
    public int createOrganizerRefund(OOrderRefundTable organizerRefundTable) {
        int affectedRow;
        Map<String, Object> param = getOrganizerRefundMap(organizerRefundTable, Integer.MIN_VALUE);

        SqlParameterSource pramSource = new MapSqlParameterSource(param);
        affectedRow = namedParameterJdbcTemplate.update(CREATE_ORGANIZER_REFUND, pramSource);

        return affectedRow;
    }

    /**
     * Update organizer refund information
     * 
     * Map organizer refund table to MySql information content, and update database
     * @param OOrderRefundTable: Organizer_refund_table
     * @return int: affectedRow
     */
    /**
     *
     */
    @Override
    public int updateOrganizerRefund(OOrderRefundTable organizerRefundTable) {
        int affectedRow;
        Map<String, Object> param =
                getOrganizerRefundMap(organizerRefundTable, organizerRefundTable.getRefund_id());

        SqlParameterSource pramSource = new MapSqlParameterSource(param);
        affectedRow = namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_REFUND, pramSource);

        return affectedRow;

    }

    /**
     * Delete an organizer refund record
     * 
     * Search if current refund_id exists in database, if exist then delete 
     * @param int: refund_id
     * @return int: affectedRow
     */
    @Override
    public int deleteOrganizerRefund(int refund_id) {
        List<OOrderRefundTable> et = getOrganizerRefundByRefundID(refund_id);

        if (et == null) {       // Didn't find this organizer refund;
            return Integer.MIN_VALUE;
        } else {
            int affectedRow = jdbcTemplate.update(DELETE_ORGANIZER_REFUND, refund_id);
            return affectedRow;
        }
    }

    /**
     * Map organizer refund table 
     * 
     * Mapping organizer refund's information variables from JSON body to entity
     * variables
     * @param OOrderRefundTable: organizer refund information
     * @param Integer: refund_id, if refund_id is Integer.MIN_VALUE then this map is for creating, other 
     * wise it's for updating
     * @return Map<String, Object>
     */
    private Map<String, Object> getOrganizerRefundMap(OOrderRefundTable organizerRefund,
            Integer refund_id) {
        Map<String, Object> param = new HashMap<>();

        if (refund_id != null && refund_id != Integer.MIN_VALUE) { // Means we need to update an organizer refund
            param.put("refund_id", refund_id);
        }
        
        param.put("oorder_id", organizerRefund.getOorder_id()); // oorder_id cannot be null, must give oorder_id

        param.put("description",
                organizerRefund.getDescription() == null
                        || organizerRefund.getDescription().length() == 0 ? null
                                : organizerRefund.getDescription());
        
        Date date = new Date(); // Default time is current time based on machine
        param.put("refund_updated_time",
                organizerRefund.getRefund_updated_time() == null ? new Timestamp(date.getTime())
                        : organizerRefund.getRefund_updated_time());
        
        param.put("refund_status",                   
                organizerRefund.getRefund_status() == null ? new String("Created")
                        : organizerRefund.getRefund_status());  // Default status is created

        return param;
    }

    /**
     * Create customer refund information
     * 
     * Map customer refund table to MySql information content, and insert into MySql
     * @param COrderRefundTable: Customer refund information
     * @return int: affectedRow
     */
    @Override
    public int createCustomerRefund(COrderRefundTable customerRefundTable) {
        int affectedRow;
        Map<String, Object> param = getCustomerRefundMap(customerRefundTable, Integer.MIN_VALUE);

        SqlParameterSource pramSource = new MapSqlParameterSource(param);
        affectedRow = namedParameterJdbcTemplate.update(CREATE_CUSTOMER_REFUND, pramSource);

        return affectedRow;
    }

    /**
     * Get customer order refund info mapping 
     * 
     * Mapping customer refund's information variables from JSON body to entity variables
     * @param COrderRefundTable: Customer refund information
     * @param Integer: crefund_id, if crefund_id is Integer.MIN_VALUE then this map is for creating, 
     * otherwise it's for updating
     * @return Map<String, Object>
     */
    private Map<String, Object> getCustomerRefundMap(COrderRefundTable customerRefund,
            Integer crefund_id) {
        Map<String, Object> param = new HashMap<>();

        if (crefund_id != null && crefund_id != Integer.MIN_VALUE) {
            param.put("crefund_id", crefund_id);
        }

        param.put("corder_id", customerRefund.getCorder_id());         // corder_id cannot be null, corder_id must be given

        param.put("crefund_description",
                customerRefund.getCrefund_description() == null
                        || customerRefund.getCrefund_description().length() == 0 ? null
                                : customerRefund.getCrefund_description());

        Date date = new Date();             // Default time is current time based on machine
        param.put("crefund_updated_time",
                customerRefund.getCrefund_updated_time() == null ? new Timestamp(date.getTime())
                        : customerRefund.getCrefund_updated_time());

        param.put("crefund_status",
                customerRefund.getCrefund_status() == null ? new String("Created")
                        : customerRefund.getCrefund_status());  // Default status is created

        return param;
    }

    /**
     * Delete a customer refund record
     * 
     * Search if current crefund_id exists in database, if exist then delete 
     * @param int: crefund_id
     * @return int: affectedRow
     */
    @Override
    public int deleteCustomerRefund(int crefund_id) {
        List<COrderRefundTable> et = getCustomerRefundByCRefundID(crefund_id);

        if (et == null) {       // Didn't find this customer refund;
            return Integer.MIN_VALUE;
        } else {
            int affectedRow = jdbcTemplate.update(DELETE_CUSTOMER_REFUND, crefund_id);
            return affectedRow;
        }
    }

    /**
     * Get customer refund information from database by customer refund id
     * 
     * @param crefund_id: customer_refund_id, cannot be null and must be positive
     * @return List<COrderRefundTable>: all entries that match crefund_id
     */
    @Override
    public List<COrderRefundTable> getCustomerRefundByCRefundID(int crefund_id) {
        return jdbcTemplate.query(SELECT_CUSTOMER_REFUND_BY_CREFUND_ID, new Object[] {crefund_id},
                new COrderRefundRowmapper());
    }

    /**
     * Get customer refund information from database by customer order id
     * 
     * @param corder_id: customer_order_id, cannot be null and must be positive
     * @return List<COrderRefundTable>: all entries that match corder_id
     */
    @Override
    public List<COrderRefundTable> getCustomerRefundByCorderID(int corder_id) {
        return jdbcTemplate.query(SELECT_CUSTOMER_REFUND_BY_CORDER_ID, new Object[] {corder_id},
                new COrderRefundRowmapper());
    }

    /**
     * Update customer refund information
     * 
     * Map customer refund table to MySql information content, and update database
     * @param COrderRefundTable: Customer_refund_table
     * @return int: affectedRow
     */
    @Override
    public int updateCustomerRefund(COrderRefundTable customerRefundTable) {
        int affectedRow;
        Map<String, Object> param =
                getCustomerRefundMap(customerRefundTable, customerRefundTable.getCrefund_id());

        SqlParameterSource pramSource = new MapSqlParameterSource(param);
        affectedRow = namedParameterJdbcTemplate.update(UPDATE_CUSTOMER_REFUND, pramSource);

        return affectedRow;
    }

}

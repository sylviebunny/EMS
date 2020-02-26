package com.enfec.demo.repository;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.OrganizerContactRowmapper;
import com.enfec.demo.model.OrganizerContactTable;
import com.enfec.demo.model.OrganizerRowmapper;
import com.enfec.demo.model.OrganizerTable;

/**
 * Implements CRUD methods for Organizer, Address, Contact; Organizer login
 * @author Sylvia Zhao
 */
@Component
@Transactional
public class OrganizerRepositoryImpl implements OrganizerRepository {
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
    /**
     * Create organizer basic information
     * 
     * Map organizer table to MySql parameters, and insert into database
     * @param organizerTable: The information that needs to be created
     * @return affectedRow
     */
	@Override
	public int createOrganizer(OrganizerTable organizerTable) {
		String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_Name, Email_Address, Password, Other_Details) VALUES "
				+ "(:organizer_name, :email_address, :password, :other_details)";	

		Map<String, Object> param = OrganizerMap(organizerTable);	
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		int affectedRow = namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);	
		return affectedRow;
	}
	
	 /**
     * Get organizer basic information from database by organizer id
     * 
     * @param Organizer_ID
     * @return List<OrganizerTable>: all entries that match the request
     */
	@Override
	public List<OrganizerTable> getOrganizerInfo(int Organizer_ID) {
		String SELECT_ORGANIZER = "select * FROM Organizers where Organizer_ID = ?";
		return jdbcTemplate.query(SELECT_ORGANIZER, new Object[] { Organizer_ID }, 
				new OrganizerRowmapper());
	}
	
    /**
     * Update organizer basic information
     * 
     * Map organizer table to MySql parameters, and update database
     * @param organizerTable: The information that needs to be updated. 
     * @return affectedRow
     */
	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		String UPDATE_ORGANIZER_INFO_PREFIX = "UPDATE Organizers SET "; 
		String UPDATE_ORGANIZER_INFO_SUFFIX = " WHERE Organizer_ID = :organizer_id";
		
		Map<String, Object> param = OrganizerMap(organizerTable);	
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_ORGANIZER_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("organizer_id")) {
				UPDATE_ORGANIZER_INFO.append(key + "=:" + key + ",");
			}
		}
		UPDATE_ORGANIZER_INFO = UPDATE_ORGANIZER_INFO.deleteCharAt(UPDATE_ORGANIZER_INFO.length() - 1); 	
		String UPDATE_ORGANIZER = UPDATE_ORGANIZER_INFO_PREFIX + UPDATE_ORGANIZER_INFO + UPDATE_ORGANIZER_INFO_SUFFIX;
		
		int affectedRow = namedParameterJdbcTemplate.update(UPDATE_ORGANIZER, pramSource);		
		return affectedRow;
	}
		
    /**
     * Delete the organizer basic, address, contact information from database by organizer id
     * 
     * @param Organizer_ID
     * @return affectedRow
     */
	@Override
	public int deleteOrganizer(int Organizer_ID) {
		String DELETE_ORGANIZER = "DELETE FROM Organizers WHERE Organizer_ID = ?";
		String DELETEfromADDRESS = "DELETE FROM Address WHERE Organizer_ID = ?";
		String DELETEfromCONTACTS = "DELETE FROM Contacts WHERE Organizer_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_ORGANIZER, Organizer_ID);
		jdbcTemplate.update(DELETEfromADDRESS, Organizer_ID);
		jdbcTemplate.update(DELETEfromCONTACTS, Organizer_ID);
		return affectedRow;
	}
	
	
	/**
     * Create organizer address information
     * 
     * Map organizer address table to MySql parameters, and insert into database
     * @param address: The information that needs to be created
     * @return affectedRow
     */
	@Override
	public int createAddress(Address address) {
		String CREATE_ADDRESS = "INSERT INTO Address(Street1, Street2, City, State, Zipcode, Other_Details, Organizer_ID) VALUES " 
				+ "(:street1, :street2, :city, :state, :zipcode, :other_details, :organizer_id)";

		Map<String, Object> param = AddressMap(address);		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		int affectedRow = namedParameterJdbcTemplate.update(CREATE_ADDRESS, pramSource);
		return affectedRow;
	}
	
	/**
     * Get organizer address information from database by organizer id
     * 
     * @param Organizer_ID
     * @return List<Address>: all entries that match the request
     */
	@Override
	public List<Address> getAddressInfo(int Organizer_ID) {
		String SELECT_ADDRESS = "select * FROM Address where Organizer_ID = ?";
		return jdbcTemplate.query(SELECT_ADDRESS, new Object[] { Organizer_ID }, 
				new BeanPropertyRowMapper<Address>(Address.class));
	}

	 /**
     * Update organizer address information
     * 
     * Map organizer address table to MySql parameters, and update database
     * @param address: The information that needs to be updated. 
     * @return affectedRow
     */
	@Override
	public int updateAddress(Address address) {
		String UPDATE_ADDRESS = "UPDATE Address SET Street1 = :street1, Street2 = :street2, "
				+ "City = :city, State = :state, Zipcode = :zipcode, Other_Details = :other_details "
				+ "WHERE Organizer_ID = :organizer_id AND Address_ID = :address_id";
		
		Map<String, Object> param = AddressMap(address);		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		int affectedRow = namedParameterJdbcTemplate.update(UPDATE_ADDRESS, pramSource);
		return affectedRow;
	}
	
	
	/**
     * Create organizer contact information
     * 
     * Map organizer contact table to MySql parameters, and insert into database
     * @param organizerContactTable: The information that needs to be created
     * @return affectedRow
     */
	@Override
	public int createOrganizerContact(OrganizerContactTable organizerContactTable) {
		// Insert contact information of organizer into Contact table
		String CREATE_REGISTER_CONTACT_INFO = "INSERT INTO Contacts(Organizer_ID, Address_ID, Contact_Name, Telephone, Web_Site_Address) VALUES"
				+ "(:organizer_id, :address_id, :contact_name, :telephone, :web_site_address)"; 
		
		try {
			Map<String, Object> param = getOrganizerContactMap(organizerContactTable); 
			SqlParameterSource pramSource = new MapSqlParameterSource(param); 
			int affectedRow = namedParameterJdbcTemplate.update(CREATE_REGISTER_CONTACT_INFO, pramSource); 
			return affectedRow; 
		} catch (RuntimeException rt) {
			return -1; 
		} catch (Exception e) {
			return Integer.MIN_VALUE; 
		}
	}
	
	/**
     * Get organizer contact information from database by organizer id
     * 
     * @param organizer_id
     * @return List<OrganizerContactTable>: all entries that match the request
     */
	@Override
	public  List<OrganizerContactTable> getOrganizerContactInfo(int organizer_id) {
		String SELECT_ORGANIZER_CONTACT = "SELECT Contact_ID, Organizer_ID, Address_ID, "
				+ "Contact_Name, Telephone, Web_Site_Address from Contacts where Contacts.Organizer_ID = ?;";
		return jdbcTemplate.query(SELECT_ORGANIZER_CONTACT,new Object[] { organizer_id }, 
				new OrganizerContactRowmapper());
	}
	
	 /**
     * Update organizer contact information
     * 
     * Map organizer contact table to MySql parameters, and update database
     * @param organizerContactTable: The information that needs to be updated. 
     * @return affectedRow
     */
	@Override
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable) {
		// Update contact information of organizer into contact table
		String UPDATE_ORGANIZER_CONTACT_INFO = "UPDATE Contacts SET Contact_Name=:contact_name, "
				+ "Telephone=:telephone, Web_Site_Address=:web_site_address WHERE "
				+ "Organizer_ID=:organizer_id AND Contact_ID =:contact_id";
		
		try {
			Map<String, Object> param = getOrganizerContactMap(organizerContactTable); 
			SqlParameterSource pramSource = new MapSqlParameterSource(param); 
			int affectedRow = namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_CONTACT_INFO, pramSource); 
			return affectedRow; 
		} catch (RuntimeException rt) {
			return -1; 
		}
	}
	
	 /**
     * Organizer login: determine if email and password match in database
     * 
     * @param OEmail: Organizer email which is used to login
	 * @param oPwd: Organizer input password
     * @return affectedRow
     */
	@Override
	public boolean isMatching(String oEmail, String oPwd){
		String SELECT_OPWD = "select * FROM Organizers where Email_Address = ?";
		List<OrganizerTable> orgPwd = jdbcTemplate.query(SELECT_OPWD, new Object[] {oEmail}, 
				new OrganizerRowmapper());
		
		if(orgPwd.isEmpty()) {
			return false;
		}
		String eCpwd = Base64.getEncoder().encodeToString(oPwd.getBytes());
		if(orgPwd.get(0).getPassword().equals(eCpwd)) {
			return true;
		}else {
			return false;
		}
	}
	
	
    /**
     * Map organizer table 
     * 
     * Mapping organizer's information variables from JSON body to entity variables
     * @param organizerTable: organizer's information used for create or update
     * @return Map<String, Object>
     */
	private Map<String, Object> OrganizerMap(OrganizerTable organizerTable) {
		// Mapping organizer's information query's variable to URL POST body
		Map<String, Object> param = new HashMap<>();

		if (organizerTable.getOrganizer_id() != 0) {
			param.put("organizer_id", organizerTable.getOrganizer_id());
		}
		param.put("organizer_name", organizerTable.getOrganizer_name() == null 
				|| organizerTable.getOrganizer_name().isEmpty() ? null 
						: organizerTable.getOrganizer_name());
		
		param.put("email_address", organizerTable.getEmail_address() == null 
				|| organizerTable.getEmail_address().isEmpty() ? null 
						: organizerTable.getEmail_address());
		
		param.put("password", organizerTable.getPassword() == null 
				|| organizerTable.getPassword().isEmpty() ? null 
						: Base64.getEncoder().encode((organizerTable.getPassword().getBytes())));
		
		param.put("other_details", organizerTable.getOther_details() == null 
				|| organizerTable.getOther_details().isEmpty() ? null 
						: organizerTable.getOther_details());
		return param;
	}
	
	/**
     * Map organizer address table 
     * 
     * Mapping organizer address's information variables from JSON body to entity variables
     * @param address: organizer address's information used for create or update
     * @return Map<String, Object>
     */
	private Map<String, Object> AddressMap(Address address) {
		Map<String, Object>param = new HashMap<>();
		param.put("address_id", address.getAddress_id()); 
		param.put("street1", address.getStreet1().isEmpty() ? null : address.getStreet1());
		param.put("street2", address.getStreet2().isEmpty() ? null : address.getStreet2());
		param.put("city", address.getCity().isEmpty() ? null : address.getCity());
		param.put("state", address.getState().isEmpty() ? null : address.getState());
		param.put("zipcode", address.getZipcode() == 0 ? null : address.getZipcode());
		param.put("other_details", address.getOther_details().isEmpty() ? null 
				: address.getOther_details());
		param.put("organizer_id", address.getOrganizer_id() == 0 ? null 
				: address.getOrganizer_id());
		return param;
	}
	
	/**
     * Map organizer contact table 
     * 
     * Mapping organizer contact's information variables from JSON body to entity variables
     * @param organizerContactTable: organizer contact's information used for create or update
     * @return Map<String, Object>
     */
	private Map<String, Object> getOrganizerContactMap(OrganizerContactTable organizerContactTable) {
		Map<String, Object> contactMap = new HashMap<>(); 
		contactMap.put("contact_id", organizerContactTable.getContact_id()); 
		contactMap.put("organizer_id", organizerContactTable.getOrganizer_id()); 
		contactMap.put("address_id", organizerContactTable.getAddress_id()); 
		contactMap.put("contact_name", organizerContactTable.getContact_name().isEmpty() ? null
				: organizerContactTable.getContact_name()); 
		contactMap.put("telephone", organizerContactTable.getTelephone().isEmpty() ? null 
				: organizerContactTable.getTelephone()); 
		contactMap.put("web_site_address", organizerContactTable.getWeb_site_address().isEmpty() ? 
				null : organizerContactTable.getWeb_site_address()); 
		return contactMap; 
	}
}

package com.enfec.demo.repository;

import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.AddressRowmapper;
import com.enfec.demo.model.OrganizerContactRowmapper;
import com.enfec.demo.model.OrganizerContactTable;
import com.enfec.demo.model.OrganizerRowmapper;
import com.enfec.demo.model.OrganizerTable;


@Component
public class OrganizerRepositoryImpl implements OrganizerRepository{
	//CRUD: Organizer, Address, Contacts 

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	//Organizer CRUD methods
	@Override
	public int createOrganizer(OrganizerTable organizerTable) {
		String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_Name, Email_Address, Password, Other_Details) VALUES "
				+ "(:Organizer_Name,:Email_Address,:Password,:Other_Details)";	
//		Another way for create://String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_Name, Email_Address, Password, Other_Details) VALUES(?,?,?,?)";
		
		int affectedRow;
		Map<String, Object> param = OrganizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);	
		return affectedRow;
		
		/* Another way to create
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    int count = jdbcTemplate.update(
	    		connection -> {
	    			PreparedStatement ps = connection.prepareStatement(REGISTER_ORGANIZER, new String[]{"Organizer_ID"});
	                ps.setString(1, organizerTable.getOrganizer_Name());
	                ps.setString(2, organizerTable.getEmail_Address() );
	                ps.setString(3, organizerTable.getPassword() );
	                ps.setString(4, organizerTable.getOther_Details());
	                return ps;
	              }, keyHolder);
	    return count;*/
	}
	
	@Override
	public List<OrganizerTable> getOrganizerInfo(int Organizer_ID) {
		String SELECT_ORGANIZER = "select Organizer_ID,Organizer_Name,Email_Address,Password,Other_Details FROM Organizers where Organizer_ID = ?";
		return jdbcTemplate.query(SELECT_ORGANIZER, new Object[] { Organizer_ID }, new OrganizerRowmapper());
	}
	
	/*
	//Update with total information
	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		String UPDATE_ORGANIZER = "UPDATE Organizers SET Email_Address = :Email_Address, Password = :Password, Other_Details = :Other_Details WHERE Organizer_ID = :Organizer_ID AND Organizer_Name = :Organizer_Name";	

		int affectedRow;
		Map<String, Object> param = OrganizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER, pramSource);
		return affectedRow;
	}*/
	
	
	//Update with partial information
	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		String UPDATE_ORGANIZER_INFO_PREFIX = "UPDATE Organizers SET "; 
		String UPDATE_ORGANIZER_INFO_SUFFIX = " WHERE Organizer_ID = :Organizer_ID";
		
		int affectedRow;
		Map<String, Object> param = organizerMap(organizerTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		StringBuilder UPDATE_ORGANIZER_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if(param.get(key) != null && !key.equals("Organizer_ID")) {
				UPDATE_ORGANIZER_INFO.append(key + "=:" + key + ",");
			}
		}
		// remove the last colon
		UPDATE_ORGANIZER_INFO = UPDATE_ORGANIZER_INFO.deleteCharAt(UPDATE_ORGANIZER_INFO.length() - 1); 
		
		String UPDATE_ORGANIZER = UPDATE_ORGANIZER_INFO_PREFIX + UPDATE_ORGANIZER_INFO + UPDATE_ORGANIZER_INFO_SUFFIX;
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZER, pramSource);		
		return affectedRow;
	}
		
	@Override
	public int deleteOrganizer(int Organizer_ID) {
		String DELETE_ORGANIZER = "DELETE FROM Organizers WHERE Organizer_ID = ?";
		String DELETEfromADDRESS = "DELETE FROM Address WHERE Organizer_ID = ?";
		String DELETEfromCONTACTS = "DELETE FROM Contacts WHERE Organizer_ID = ?";
		int affectedRow = jdbcTemplate.update(DELETE_ORGANIZER, Organizer_ID);
		int affectedRow1 = jdbcTemplate.update(DELETEfromADDRESS, Organizer_ID);
		int affectedRow2 = jdbcTemplate.update(DELETEfromCONTACTS, Organizer_ID);
		return affectedRow;
	}
	
	
	//Organizer's Address CRU methods
	@Override
	public int createAddress(Address address) {
		String CREATE_ADDRESS = "INSERT INTO Address(Street1, Street2, City, State, Zipcode, Other_Details, Organizer_ID) VALUES " 
				+ "(:Street1,:Street2,:City,:State,:Zipcode,:Other_Details,:Organizer_ID)";
		int affectedRow;
		Map<String, Object> param = AddressMap(address);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(CREATE_ADDRESS, pramSource);
		return affectedRow;
	}
	
	@Override
	public List<Address> getAddressInfo(int Organizer_ID) {
		String SELECT_ADDRESS = "select Address_ID, Street1, Street2, City, State, Zipcode, Other_Details, Organizer_ID FROM Address where Organizer_ID = ?";
		return jdbcTemplate.query(SELECT_ADDRESS, new Object[] { Organizer_ID }, new BeanPropertyRowMapper<Address>(Address.class));
	}

	@Override
	public int updateAddress(Address address) {
		String UPDATE_ADDRESS = "UPDATE Address SET Street1 = :Street1, Street2 = :Street2, City = :City, State = :State, Zipcode = :Zipcode, Other_Details = :Other_Details WHERE Organizer_ID = :Organizer_ID AND Address_ID =:Address_ID";
		
		int affectedRow;
		Map<String, Object> param = AddressMap(address);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ADDRESS, pramSource);
		return affectedRow;
	}
	
	
	//Organizer's Address CRU methods
	@Override
	public int createOrganizerContact(OrganizerContactTable organizerContactTable) {
		// Insert contact information of organizer into Contact table
		String CREATE_REGISTER_CONTACT_INFO = "INSERT INTO Contacts(Organizer_ID, Address_ID, Contact_Name, Telephone, Web_Site_Address) VALUES"
				+ "(:organizer_id, :address_id, :contact_name, :telephone, :web_site_address)"; 
		
		try {
			int affectedRow; 
			Map<String, Object> param = getOrganizerContactMap(organizerContactTable); 
			
			SqlParameterSource pramSource = new MapSqlParameterSource(param); 
			affectedRow = namedParameterJdbcTemplate.update(CREATE_REGISTER_CONTACT_INFO, pramSource); 
			return affectedRow; 
		} catch (RuntimeException rt) {
			return -1; 
		} catch (Exception e) {
			return Integer.MIN_VALUE; 
		}
	}
	
	@Override
	public  List<OrganizerContactTable> getOrganizerContactInfo(int organizer_id) {
		String SELECT_ORGANIZER_CONTACT = "SELECT Contact_ID, Organizer_ID, Address_ID, Contact_Name, Telephone, Web_Site_Address from Contacts where Contacts.Organizer_ID = ?;";
		return jdbcTemplate.query(SELECT_ORGANIZER_CONTACT,new Object[] { organizer_id }, new OrganizerContactRowmapper());
	}
	
	@Override
	public int updateOrganizerContact(OrganizerContactTable organizerContactTable) {
		// Update contact information of organizer into contact table
		String UPDATE_ORGANIZER_CONTACT_INFO = "UPDATE Contacts SET Contact_Name=:contact_name, Telephone=:telephone, "
				+ "Web_Site_Address=:web_site_address WHERE Organizer_ID=:organizer_id AND Contact_ID =:contact_id";
		
		try {
			int affectedRow; 
			Map<String, Object> param = getOrganizerContactMap(organizerContactTable); 
			
			SqlParameterSource pramSource = new MapSqlParameterSource(param); 
			affectedRow = namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_CONTACT_INFO, pramSource); 
			return affectedRow; 
		} catch (RuntimeException rt) {
			return -1; 
		}
	}
	
	//For create, update with total info
	private Map<String, Object> OrganizerMap(OrganizerTable organizerTable) {
		Map<String, Object>param = new HashMap<>();

		param.put("Organizer_Name", organizerTable.getOrganizer_Name().isEmpty() ? null:organizerTable.getOrganizer_Name());
		param.put("Email_Address", organizerTable.getEmail_Address().isEmpty() ? null:organizerTable.getEmail_Address());
		param.put("Password", organizerTable.getPassword().isEmpty() ? null:Base64.getEncoder().encode((organizerTable.getPassword().getBytes())));
		param.put("Other_Details", organizerTable.getOther_Details().isEmpty() ? null:organizerTable.getOther_Details());
		return param;
	}
	
	//For update with partial part
	private Map<String, Object> organizerMap(OrganizerTable organizerTable) {
		// Mapping organizer's information query's variable to URL POST body
		Map<String, Object>param = new HashMap<>();
	
		if (organizerTable.getOrganizer_ID() != 0) {
			param.put("Organizer_ID", organizerTable.getOrganizer_ID());
		} else {
			throw new NullPointerException("Organizer_ID cannot be null");
		}
		param.put("Organizer_Name", organizerTable.getOrganizer_Name() == null || organizerTable.getOrganizer_Name().isEmpty() ? null:organizerTable.getOrganizer_Name());
		param.put("Email_Address", organizerTable.getEmail_Address() == null || organizerTable.getEmail_Address().isEmpty() ? null:organizerTable.getEmail_Address());
		param.put("Password", organizerTable.getPassword() == null || organizerTable.getPassword().isEmpty() ? null:Base64.getEncoder().encode((organizerTable.getPassword().getBytes())));
		param.put("Other_Details", organizerTable.getOther_Details() == null || organizerTable.getOther_Details().isEmpty() ? null:organizerTable.getOther_Details());
		return param;
	}
	
	private Map<String, Object> AddressMap(Address address) {
		Map<String, Object>param = new HashMap<>();

		param.put("Address_ID", address.getAddress_ID()); 
		param.put("Street1", address.getStreet1().isEmpty() ? null:address.getStreet1());
		param.put("Street2", address.getStreet2().isEmpty() ? null:address.getStreet2());
		param.put("City", address.getCity().isEmpty() ? null:address.getCity());
		param.put("State", address.getState().isEmpty() ? null:address.getState());
		param.put("Zipcode", address.getZipcode() == 0 ? null:address.getZipcode());
		param.put("Other_Details", address.getOther_Details().isEmpty() ? null:address.getOther_Details());
		param.put("Organizer_ID", address.getOrganizer_ID() == 0 ? null:address.getOrganizer_ID());
		return param;
	}
	
	private Map<String, Object> getOrganizerContactMap(OrganizerContactTable organizerContactTable) {
		Map<String, Object> contactMap = new HashMap<>(); 
		contactMap.put("contact_id", organizerContactTable.getContact_id()); 
		contactMap.put("organizer_id", organizerContactTable.getOrganizer_id()); 
		contactMap.put("address_id", organizerContactTable.getAddress_id()); 
		contactMap.put("contact_name", organizerContactTable.getContact_name() == null || organizerContactTable.getContact_name().isEmpty() ? null : organizerContactTable.getContact_name()); 
		contactMap.put("telephone", organizerContactTable.getTelephone() == null || organizerContactTable.getTelephone().isEmpty() ? null : organizerContactTable.getTelephone()); 
		contactMap.put("web_site_address", organizerContactTable.getWeb_site_address() == null || organizerContactTable.getWeb_site_address().isEmpty() ? null : organizerContactTable.getWeb_site_address()); 
		return contactMap; 
	}
}

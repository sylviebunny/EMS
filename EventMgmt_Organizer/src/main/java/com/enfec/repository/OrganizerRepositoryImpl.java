package com.enfec.repository;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enfec.model.Address;
import com.enfec.model.OrganizerContactRowmapper;
import com.enfec.model.OrganizerContactTable;
import com.enfec.model.OrganizerRowmapper;
import com.enfec.model.OrganizerTable;
import com.enfec.model.OrganizerTokenRowmapper;
import com.enfec.model.OrganizerTokenTable;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Implement CRUD methods for organizer, address and contacts
* Author: Chad Chai
* Assignment: Organizer login, password handling
* Class: OrganizerRepositoryImpl
*
************************************************/
@Component
@Transactional
public class OrganizerRepositoryImpl implements OrganizerRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizerRepositoryImpl.class);
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Create organizer basic information 
	 * Map organizer table to MySql parameters and insert into database
	 * 
	 * @param organizerTable: The information that needs to be created
	 * @return number of affected rows
	 */
	@Override
	public int createOrganizer(OrganizerTable organizerTable) {
		String REGISTER_ORGANIZER = "INSERT INTO Organizers(Organizer_Name, Email_Address, Password, Other_Details) VALUES "
				+ "(:organizer_name, :email_address, :password, :other_details)";

		Map<String, Object> param = OrganizerMap(organizerTable);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Register Organizer Info: {}", pramSource);
		int affectedRow = namedParameterJdbcTemplate.update(REGISTER_ORGANIZER, pramSource);
		return affectedRow;
	}

	/**
	 * Get organizer information from database by organizer id
	 * 
	 * @param Organizer_ID
	 * @return List<OrganizerTable>: all entries that match the request
	 */
	@Override
	public OrganizerTable getOrganizerInfo(int Organizer_ID) {
//		String SELECT_ORGANIZER = "select * FROM Organizers where Organizer_ID = ?";
//		return jdbcTemplate.query(SELECT_ORGANIZER, new Object[] { Organizer_ID }, new OrganizerRowmapper());
		String SELECT_ORGANIZER = "select * from Organizers o join Contacts c on o.Organizer_ID = c.Organizer_ID join Address a on a.Address_ID=c.Address_ID where o.Organizer_ID=?";
		
		OrganizerTable org;
		try {
			org = jdbcTemplate.queryForObject(SELECT_ORGANIZER, new Object[] { Organizer_ID }, 
					new BeanPropertyRowMapper<OrganizerTable>(OrganizerTable.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return org;
	}

	/**
     * Get all rooms' information from 'Rooms' and 'Space_Requests' tables in database
     * @return List<Room>: all entries that match the request
     */
	@Override
	public List<OrganizerTable> getAllOrganizerInfo() {
		String SELECT_ALL_ORGANZIER = "select * from Organizers o join Contacts c on o.Organizer_ID = c.Organizer_ID join Address a on a.Address_ID=c.Address_ID";
		return jdbcTemplate.query(SELECT_ALL_ORGANZIER, new BeanPropertyRowMapper<OrganizerTable>(OrganizerTable.class));
	}
	
	/**
	 * Update organizer basic information 
	 * Map organizer table to MySql parameters and update database
	 * 
	 * @param organizerTable: The information that needs to be updated.
	 * @return number of affected rows
	 */
	@Override
	public int updateOrganizer(OrganizerTable organizerTable) {
		String UPDATE_ORGANIZER_INFO_PREFIX = "UPDATE Organizers SET ";
		String UPDATE_ORGANIZER_INFO_SUFFIX = " WHERE Organizer_ID = :organizer_id";

		Map<String, Object> param = OrganizerMap(organizerTable);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating Organizer Info: {}",pramSource);
		
		StringBuilder UPDATE_ORGANIZER_INFO = new StringBuilder();
		for (String key : param.keySet()) {
			if (param.get(key) != null && !key.equals("organizer_id")) {
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
	 * @return number of affected rows
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
	 * Map organizer address table to MySql parameters and insert into database
	 * 
	 * @param address: The information that needs to be created
	 * @return number of affected rows
	 */
	@Override
	public int createAddress(Address address) {
		String CREATE_ADDRESS = "INSERT INTO Address(Street1, Street2, City, State, Zipcode, Organizer_ID) VALUES "
				+ "(:street1, :street2, :city, :state, :zipcode, :organizer_id)";

		Map<String, Object> param = AddressMap(address);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Create Organizer Address Info: {}", pramSource);
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
	 * Map organizer address table to MySql parameters and update database
	 * 
	 * @param address: The information that needs to be updated.
	 * @return number of affected rows
	 */
	@Override
	public int updateAddress(Address address) {
		String UPDATE_ADDRESS = "UPDATE Address SET Street1 = :street1, Street2 = :street2, "
				+ "City = :city, State = :state, Zipcode = :zipcode "
				+ "WHERE Organizer_ID = :organizer_id AND Address_ID = :address_id";

		Map<String, Object> param = AddressMap(address);
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating Organizer Address Info: {}", pramSource);
		int affectedRow = namedParameterJdbcTemplate.update(UPDATE_ADDRESS, pramSource);
		return affectedRow;
	}

	/**
	 * Create organizer contact information 
	 * Map organizer contact table to MySql parameters and insert into database
	 * 
	 * @param organizerContactTable: The information that needs to be created
	 * @return number of affected rows
	 */
	@Override
	public int createOrganizerContact(OrganizerContactTable organizerContactTable) {
		// Insert contact information of organizer into Contact table
		String CREATE_REGISTER_CONTACT_INFO = "INSERT INTO Contacts(Organizer_ID, Address_ID, Contact_Name, Telephone, Web_Site_Address) VALUES"
				+ "(:organizer_id, :address_id, :contact_name, :telephone, :web_site_address)";

		try {
			Map<String, Object> param = getOrganizerContactMap(organizerContactTable);
			SqlParameterSource pramSource = new MapSqlParameterSource(param);
			logger.info("Create Organizer Contact Info: {}", pramSource);
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
	public List<OrganizerContactTable> getOrganizerContactInfo(int organizer_id) {
		String SELECT_ORGANIZER_CONTACT = "SELECT Contact_ID, Organizer_ID, Address_ID, "
				+ "Contact_Name, Telephone, Web_Site_Address from Contacts where Contacts.Organizer_ID = ?;";
		return jdbcTemplate.query(SELECT_ORGANIZER_CONTACT, new Object[] { organizer_id },
				new OrganizerContactRowmapper());
	}

	/**
	 * Update organizer contact information 
	 * Map organizer contact table to MySql parameters and update database
	 * 
	 * @param organizerContactTable: The information that needs to be updated.
	 * @return number of affected rows
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
			logger.info("Updating Organizer Contact Info: {}", pramSource);
			int affectedRow = namedParameterJdbcTemplate.update(UPDATE_ORGANIZER_CONTACT_INFO, pramSource);
			return affectedRow;
		} catch (RuntimeException rt) {
			return -1;
		}
	}

	/**
	 * For create and update with 'Organizers' table in database 
	 * Mapping organizer information between URL body information and database variable attributes
	 * 
	 * @param organizerTable: organizer's information used for create or update
	 * @return Map<String, Object>: contains variable and it's corresponding
	 *         information
	 */
	private Map<String, Object> OrganizerMap(OrganizerTable organizerTable) {
		Map<String, Object> param = new HashMap<>();

		if (organizerTable.getOrganizer_id() != 0) {
			param.put("organizer_id", organizerTable.getOrganizer_id());
		}
		param.put("organizer_name",
				organizerTable.getOrganizer_name() == null || organizerTable.getOrganizer_name().isEmpty() ? null
						: organizerTable.getOrganizer_name());

		param.put("email_address",
				organizerTable.getEmail_address() == null || organizerTable.getEmail_address().isEmpty() ? null
						: organizerTable.getEmail_address());

		param.put("password", organizerTable.getPassword() == null || organizerTable.getPassword().isEmpty() ? null
				: Base64.getEncoder().encode((organizerTable.getPassword().getBytes())));

		param.put("other_details",
				organizerTable.getOther_details() == null || organizerTable.getOther_details().isEmpty() ? null
						: organizerTable.getOther_details());
		return param;
	}

	/**
	 * For create and update with 'Address' table in database 
	 * Mapping organizer address information between URL body information and database variable attributes
	 * 
	 * @param address: organizer address's information used for create or update
	 * @return Map<String, Object>: contains variable and it's corresponding information
	 */
	private Map<String, Object> AddressMap(Address address) {
		Map<String, Object> param = new HashMap<>();
		param.put("address_id", address.getAddress_id());
		param.put("street1", address.getStreet1().isEmpty() ? null : address.getStreet1());
		param.put("street2", address.getStreet2().isEmpty() ? null : address.getStreet2());
		param.put("city", address.getCity().isEmpty() ? null : address.getCity());
		param.put("state", address.getState().isEmpty() ? null : address.getState());
		param.put("zipcode", address.getZipcode().isEmpty() ? null : address.getZipcode());
		param.put("organizer_id", address.getOrganizer_id() == 0 ? null : address.getOrganizer_id());
		return param;
	}

	/**
	 * For create and update with 'Contacts' table in database 
	 * Mapping organizer contact information between URL body information and database variable attributes
	 * 
	 * @param organizerContactTable: organizer contact's information used for create or update
	 * @return Map<String, Object>: contains variable and it's corresponding information
	 */
	private Map<String, Object> getOrganizerContactMap(OrganizerContactTable organizerContactTable) {
		Map<String, Object> contactMap = new HashMap<>();
		contactMap.put("contact_id", organizerContactTable.getContact_id());
		contactMap.put("organizer_id", organizerContactTable.getOrganizer_id());
		contactMap.put("address_id", organizerContactTable.getAddress_id());
		contactMap.put("contact_name",
				organizerContactTable.getContact_name().isEmpty() ? null : organizerContactTable.getContact_name());
		contactMap.put("telephone",
				organizerContactTable.getTelephone().isEmpty() ? null : organizerContactTable.getTelephone());
		contactMap.put("web_site_address", organizerContactTable.getWeb_site_address().isEmpty() ? null
				: organizerContactTable.getWeb_site_address());
		return contactMap;
	}

	/**
	 * Organizer login: determine if email and password match in database
	 * 
	 * @param OEmail: Organizer email which is used to login
	 * @param oPwd: Organizer input password
	 * @return whether oEmail and oPwd match or not
	 */
	@Override
	public boolean isMatching(String oEmail, String oPwd) {
		String SELECT_OPWD = "select * FROM Organizers where Email_Address = ?";
		List<OrganizerTable> orgPwd = jdbcTemplate.query(SELECT_OPWD, new Object[] { oEmail },
				new OrganizerRowmapper());

		if (orgPwd.isEmpty()) {
			return false;
		}
		String eCpwd = Base64.getEncoder().encodeToString(oPwd.getBytes());
		if (orgPwd.get(0).getPassword().equals(eCpwd)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * Organizer register: determine if the email exist in database
     * @param organizerEmail: organizer email which is used to register as a new customer
     * @return whether the organizerEmail exist in database or not.
     */
	@Override
	public boolean hasRegistered(String email) {
		String CUSTOMER = "SELECT Email_Address FROM Customers WHERE Email_Address=?";
		String ORGANIZER = "SELECT Email_Address FROM Organizers WHERE Email_Address=?";

		List<Map<String, Object>> regisAsCustomer = jdbcTemplate.queryForList(CUSTOMER, new Object[] { email });
		List<Map<String, Object>> regisOrganizer = jdbcTemplate.queryForList(ORGANIZER, new Object[] { email });
		if (regisAsCustomer.isEmpty() && regisOrganizer.isEmpty()) {
			logger.info("not register before: {}", email);
			return false;
		} else if (!regisAsCustomer.isEmpty()) {
			logger.info("registed as Customer already");
			return true;
		} else {
			logger.info("registed as Organizer already");
			return true;
		}

	}
	

	/**
     * Organizer register: send register confirmation email to organizer
     * 
     * @param to: the email address of the organizer
     * @param subject: the subject of the confirmation email
     * @param body: the detail of confirmation email
     * @param oToken: the OTP for confirmation email
     * @return null
     */
	@Override
	public void sendGreetMail(String to, String subject, String body, String oToken) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setText(body, true);// true indicate html
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For create and update with 'Organizer_Token' table in database 
	 * Mapping organizer token information between URL body information and database variable attributes
	 * 
	 * @param organizerTokenTable: organizer token's information used for create or update
	 * @return Map<String, Object>: contains variable and it's corresponding information
	 */
	private Map<String, Object> OrganizerTokenMap(OrganizerTokenTable organizerTokenTable) {
		Map<String, Object> ofgpwdMap = new HashMap<>();
		ofgpwdMap.put("organizerTokenID", organizerTokenTable.getOrganizerTokenID());
		ofgpwdMap.put("organizerEmail", organizerTokenTable.getOrganizerEmail());
		ofgpwdMap.put("organizerToken", organizerTokenTable.getOrganizerToken());
		ofgpwdMap.put("organizerExpiryDate", organizerTokenTable.getOrganizerExpiryDate());
		ofgpwdMap.put("hasChecked", organizerTokenTable.getHasChecked());
		return ofgpwdMap;
	}

	/**
     * Organizer forget password: determine if email is in organizer table
     * 
     * @param organizerEmail: organizer email which is used to get back password
     * @return whether the email address is in the organizer table or not
     */
	@Override
	public boolean isValidOrganizer(String organizerEmail) {
		String VALID_ORGANIZER = "SELECT * FROM Organizers WHERE Email_Address=?";
		List<OrganizerTable> orgEmail = jdbcTemplate.query(VALID_ORGANIZER, new Object[] { organizerEmail },
				new OrganizerRowmapper());
		if (orgEmail.isEmpty() || orgEmail.get(0).getEmail_address().isEmpty()) {
			logger.info("not valid organizer email: {}", organizerEmail);
			return false;
		} else {
			logger.info("Valid organizer email");
			return true;
		}
	}

	/**
     * Organizer forget password: determine if email is in organizer token table
     * 
     * @param organizerEmail: organizer email which is used to get back password
     * @return whether the email address is in the organizer token table or not
     */
	@Override
	public boolean hasForgetenPWD(String organizerEmail) {
		String HAS_FORGET_PWD = "SELECT * FROM Organizer_Token WHERE OEmail=?";
		List<OrganizerTokenTable> fgorgEmail = jdbcTemplate.query(HAS_FORGET_PWD, new Object[] { organizerEmail },
				new OrganizerTokenRowmapper());
		if (fgorgEmail.isEmpty() || fgorgEmail.get(0).getOrganizerEmail().isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

	/**
     * Create organizer basic information
     * Map organizerTokenTable to MySql parameters and insert into database
     * 
     * @param organizerEmail: the email address of the customer
     * @param organizerToken: the random generated OTP 
     * @param organizerExpiryDate: the expire time of the cToken
     * @return number of affected rows
     */
	@Override
	public int saveTokenInfo(String organizerEmail, String organizerToken, Timestamp organizerExpiryDate) {
		String CREATE_TOKEN = "INSERT INTO Organizer_Token(OEmail, OToken, OTExpire) VALUE (:organizerEmail, :organizerToken, :organizerExpiryDate)";

		int affectedRow;
		OrganizerTokenTable ot = new OrganizerTokenTable();
		ot.setOrganizerEmail(organizerEmail);
		ot.setOrganizerToken(organizerToken);
		ot.setOrganizerExpiryDate(organizerExpiryDate);
		Map<String, Object> resMap = OrganizerTokenMap(ot);
		SqlParameterSource parameterSource = new MapSqlParameterSource(resMap);
		affectedRow = namedParameterJdbcTemplate.update(CREATE_TOKEN, parameterSource);
		return affectedRow;
	}

	@Autowired
	private JavaMailSender mailSender;

	/**
     * Organizer reset password: send reset password link to organizer email
     * @param to: the email address of the organizer
     * @param subject: the subject of the reset password email
     * @param body: the detail of reset password email
     * @param OToken: the OTP for reset password
     * @return null
     */
	@Override
	public void sendPwdMail(String to, String subject, String body, String OToken) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setText(body, true);// true indicate html
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
     * Generate a random string as token
     */
	@Override
	public String generateToken() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder token = new StringBuilder(20);
		for (int i = 0; i < 20; i++) {
			int sequence = (int) (AlphaNumericString.length() * Math.random());
			token.append(AlphaNumericString.charAt(sequence));
		}

		return token.toString();
	}

	/**
     * Verify token: determine if the token is correct and not expire
     * 
     * @param OToken: organizer token 
     * @return whether the organizer token is in the token table and expired or not
     */
	@Override
	public boolean validToken(String OToken) {
		String VALID_TOKEN = "SELECT * FROM Organizer_Token WHERE OToken=?";

		List<OrganizerTokenTable> orgToken = jdbcTemplate.query(VALID_TOKEN, new Object[] { OToken },
				new OrganizerTokenRowmapper());
		if (orgToken.isEmpty() || orgToken.get(0).getOrganizerExpiryDate().equals(null)) {
			return false;
		} else {
			Timestamp ot = new Timestamp(System.currentTimeMillis() - OrganizerTokenTable.getTokenExpiration());
			if (orgToken.get(0).getOrganizerExpiryDate().before(ot)) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
     * Get organizer basic information from database by organizer token
     * 
     * @param OToken
     * @return List<OrganizerTokenTable>: all entries that match the request
     */
	@Override
	public List<OrganizerTokenTable> findEmailByToken(String OToken) {
		String FIND_EMAIL_BY_TOKEN = "SELECT * FROM Organizer_Token WHERE OToken=?";
		return jdbcTemplate.query(FIND_EMAIL_BY_TOKEN, new Object[] { OToken }, new OrganizerTokenRowmapper());
	}

	/**
     * Update organizer password: save the new password to organizer table
     * Map organizer table to MySql parameters, and update database
     * 
     * @param oEmail: the email address of the organizer
     * @param newpwd: the random generated OTP 
     * @return affected row
     */
	@Override
	public int updatePassword(String oEmail, String newpwd) {
		String UPDATE_PASSWORD = "UPDATE Organizers SET Password =:password WHERE Email_Address =:email_address";

		int affectedRow;
		OrganizerTable organizerTable = new OrganizerTable();
		organizerTable.setEmail_address(oEmail);
		organizerTable.setPassword(newpwd);
		Map<String, Object> updatePwdMap = OrganizerMap(organizerTable);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updatePwdMap);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_PASSWORD, parameterSource);
		return affectedRow;
	}

	/**
     * Update organizer token table if organizer token is expired
     * Map organizer token table to MySql parameters, and update database
     * 
     * @param oEmail: the email address of the organizer
     * @param oToken: the random generated OTP 
     * @param expiryDate: the expire time of the oToken
     * @return affected row
     */
	@Override
	public int updateToken(String oEmail, String oToken, Timestamp expireDate) {
		String UPDATE_TOKEN = "UPDATE Organizer_Token SET OToken =:organizerToken, OTExpire =:organizerExpiryDate WHERE OEmail =:organizerEmail";

		int affectedRow;
		OrganizerTokenTable ott = new OrganizerTokenTable();
		ott.setOrganizerEmail(oEmail);
		ott.setOrganizerToken(oToken);
		ott.setOrganizerExpiryDate(expireDate);

		Map<String, Object> updateTokenMap = OrganizerTokenMap(ott);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updateTokenMap);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_TOKEN, parameterSource);
		return affectedRow;
	}
	
	/**
     * Verify token: determine if the token is checked
     * @param OToken: organizer token 
     * @return whether the organizer token is checked or not
     */
	@Override
	public boolean hasChecked(String OToken) {
		String VALID_TOKEN = "SELECT * FROM Organizer_Token WHERE OToken=?";
		List<OrganizerTokenTable> orgToken = jdbcTemplate.query(VALID_TOKEN, new Object[] { OToken },
				new OrganizerTokenRowmapper());
		if (orgToken.isEmpty() || orgToken.get(0).getHasChecked()==0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Update organizer basic information, can update partial info
	 * @param OrganizerTable. Organizer id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@Override
	public int updateTokenStatus(String oToken) {
		String UPDATE_TOKEN_STATUS = "UPDATE Organizer_Token SET Checked =:hasChecked WHERE OToken =:organizerToken";
		int affectedRow;
		OrganizerTokenTable ott = new OrganizerTokenTable();
		ott.setOrganizerToken(oToken);
		ott.setHasChecked(1);
		logger.info("Organizer token status change to: {}", ott.getHasChecked());

		Map<String, Object> updateTokenMap = OrganizerTokenMap(ott);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updateTokenMap);
		logger.info("Update organizer token status to :{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_TOKEN_STATUS, parameterSource);

		return affectedRow;
	}

	/**
     * Update organizer email address: save the new address to organizer table
     * Map organizer table to MySql parameters, and update database
     * @param oldEmail: the original email address of the organizer
     * @param newEmail: the new email address
     * @return affected row
     */
	@Override
	public int updateEmail(int oID, String newEmail) {
		String UPDATE_EMAIL = "UPDATE Organizers SET Email_Address =:email_address WHERE Organizer_ID =:organizer_id";
		int affectedRow;
		OrganizerTable ot = new OrganizerTable();
		ot.setOrganizer_id(oID);
		logger.info(String.valueOf(oID));
		
		ot.setEmail_address(newEmail);
		logger.info(newEmail);
		
		Map<String, Object> updateEmailMap = OrganizerMap(ot);
		SqlParameterSource parameterSource = new MapSqlParameterSource(updateEmailMap);
		logger.info("Update organizer email:{}", parameterSource);
		affectedRow = namedParameterJdbcTemplate.update(UPDATE_EMAIL, parameterSource);
		return affectedRow;
	}
	

	/**
     * Get customer id information from database by customer email
     * @param CEmail
     * @return List<CustomerTable>: all entries that match the request
     */
	@Override
	public List<OrganizerTable> findIDByEmail(String OEmail) {
		String FIND_ID_BY_EMAIL = "SELECT * FROM Organizers WHERE Email_Address=?";
		return jdbcTemplate.query(FIND_ID_BY_EMAIL, new Object[] {OEmail }, new OrganizerRowmapper());
	}
}

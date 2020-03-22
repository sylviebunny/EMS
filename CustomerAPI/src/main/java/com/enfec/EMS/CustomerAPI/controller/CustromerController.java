package com.enfec.EMS.CustomerAPI.controller;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.EMS.CustomerAPI.model.CustomerTable;
import com.enfec.EMS.CustomerAPI.repository.CustomerRepositoryImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;


/************************************************
* Author: Chad Chai
* Assignment: Customer Controller
* Class: CustomerController
************************************************/
@RestController
public class CustromerController {
	private static final Logger logger = LoggerFactory.getLogger(CustromerController.class);

	@Autowired
	CustomerRepositoryImpl customerRepositoryImpl;
	
	
	/**
	 * Get customer basic information from database by customer id
	 * @param id
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/Customers/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getCustomerList(@PathVariable String id) {
		List<CustomerTable> customerList = customerRepositoryImpl.getCustomer(id);
		if (customerList.isEmpty()) {
			logger.info("No Customer found for: {} ", id);
			return new ResponseEntity<>("{\"message\" : \"No Customer found\"}", HttpStatus.OK);

		}
		return new ResponseEntity<>(new Gson().toJson((customerRepositoryImpl.getCustomer(id))), HttpStatus.OK);
	}
	
	
	/**
	 * Get customer basic information from database by customer id
	 * @param id
	 * @return ResponseEntity with message and data
	 */
	@RequestMapping(value = "/AllCustomers", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getCustomerList() {
		List<CustomerTable> customerList = customerRepositoryImpl.getAllCustomer();
		if (customerList.isEmpty()) {
			logger.info("No Customer found ");
			return new ResponseEntity<>("{\"message\" : \"No Customer found\"}", HttpStatus.OK);

		}
		return new ResponseEntity<>(new Gson().toJson((customerRepositoryImpl.getAllCustomer())), HttpStatus.OK);
	}
	
	
	/**
	 * Create or register an customer user, send account active email to user and put basic information into database
	 * @param customerTable. Contains customer basic information; email_address and password cannot be null
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/Customer/Register", method = RequestMethod.POST, produces = "applications/json; charset=UTF-8")
	public ResponseEntity<String> customerRegister(@RequestBody(required = true) CustomerTable customerTable) {
		try {
			if (customerRepositoryImpl.hasRegistered(customerTable.getEmail())) {
				return new ResponseEntity<String>("{\"message\" : \"Customer registered already, Please log in\"}",
						HttpStatus.OK);
			} else {
				int affectedRow = customerRepositoryImpl.registerCustomer(customerTable);
				if (affectedRow == 0) {
					logger.info("Customer not registered customer_name: {}", customerTable.getName());
					return new ResponseEntity<String>("{\"message\" : \"Customer not registered\"}", HttpStatus.OK);
				} else {
					logger.info("Custoer registered customer_name: {}", customerTable.getName());
					String cEmail = customerTable.getEmail();
					String cToken = customerRepositoryImpl.generateToken();
					Timestamp expireDate = new Timestamp(System.currentTimeMillis());
					customerRepositoryImpl.saveTokenInfo(cEmail, cToken, expireDate);
					customerRepositoryImpl.sendGreetMail(customerTable.getEmail(), "Welcome to EMS", "<p>Dear</p>"
							+ "<p><b>" + customerTable.getName() + "</b></p>"
							+ "<p>Welcome to join the EMS. Your account is all set!</p>"
							+ "<p><a href = 'http://localhost:4200/users/registerconfirm?cToken="
							+ cToken + "'>Please click this link to Active your account</a></p>" +

							"<p>This is a system generated mail. Please do not reply to this email ID. If you have a query or need any clarification you may:</p>"
							+ "<p>(1) Call our 24-hour Customer Care or\r\n</p>"
							+ "<p>(2) Email Us support@enfec.com\r\n</p>" +

							"<p>For any problem please contact us at 24*7 Hrs. Customer Support at 18001231234 (TBD) or mail us at support@enfec.com\r\n"
							+ "Thank you for using our Event Management System\r\n</p>", cToken);
					logger.info("Greeting send to the eamil address: {}", customerTable.getEmail());

					return new ResponseEntity<String>(
							"{\"message\": \"Customer registered and Greeting send to the eamil\"}", HttpStatus.OK);
				}
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Customer id:{}", customerTable.getId());
			return new ResponseEntity<String>("{\"message\": \"Invalid Customer id\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exceprtion in regestering Customer:{}", exception.getMessage());
			return new ResponseEntity<String>("{\"message\": \"Exception in regestering Customer info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Active the customer user account, check if the token is valid or not
	 * @param cToken. Directly get from the URL
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> confirmRegistration(@RequestParam("cToken") String cToken) {
		if (customerRepositoryImpl.validToken(cToken) && !customerRepositoryImpl.hasChecked(cToken)) {
			logger.info("valid token: {} ", cToken);
			logger.info("token has not checked ");
			
			customerRepositoryImpl.updateTokenStatus(cToken);
			logger.info("change token status to checked ");
			
			String cEmail = customerRepositoryImpl.findEmailByToken(cToken).get(0).getCustomerEmail();
			customerRepositoryImpl.updateActiveStatus(cEmail);
			logger.info("Customer Active Status changed");
			
			return new ResponseEntity<String>("{\"message\": \"Customer account actived\"}", HttpStatus.OK);
		} else if (customerRepositoryImpl.validToken(cToken) && customerRepositoryImpl.hasChecked(cToken)){
			logger.info(" has checked already ");
			return new ResponseEntity<String>("{\"message\": \"Customer account actived already, please login\"}", HttpStatus.OK);
		} else {
			logger.info("invalid token: {} ", cToken);
			return new ResponseEntity<String>("{\"message\": \"Customer account active failed\"}", HttpStatus.OK);
		}
	}
	
	
	/**
	 * Update customer basic information, can update partial info
	 * @param CustomerTable. Customer id cannot be null and must exist in database
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/Customer/Update", method = RequestMethod.PUT, produces = "applications/json; charset=UTF-8")
	public ResponseEntity<String> CustomerUpdate(@RequestBody(required = true) CustomerTable customerTable) {
		try {
			int affectedRow = customerRepositoryImpl.updateCustomer(customerTable);
			if (affectedRow == 0) {
				logger.info("Customer not updated customer_name: {}", customerTable.getName());
				return new ResponseEntity<String>("{\"message\" : \"Customer not updated\"}", HttpStatus.OK);
			} else {
				logger.info("Custoer updated customer_name: {}", customerTable.getName());
				return new ResponseEntity<String>("{\"message\": \"Customer updated\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Customer id:{}", customerTable.getId());
			return new ResponseEntity<String>("{\"message\": \"Invalid Customer id\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exceprtion in updating Customer:{}", exception.getMessage());
			return new ResponseEntity<String>("{\"message\": \"Exception in updating Customer info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Delete customer basic info from database by customer id
	 * @param id
	 * @return ResponseEntity with message
	 */
	@RequestMapping(value = "/Customer/Delete/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteCustomer(@PathVariable String id) {

		int affectedRow = customerRepositoryImpl.deleteCustomer(id);
		if (affectedRow == 0) {
			return new ResponseEntity<>("{\"message\" : \"Customer not found\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"message\" : \"Customer deleted\"}", HttpStatus.OK);
		}

	}

	
	/**
	 * Customer login
	 * @param customerTable. Contains customer email and password, cannot be null
	 * @return ResponseEntity with login result message
	 */
	@RequestMapping(value = "/Customers/Login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> cLogin(@RequestBody(required = true) CustomerTable customerTable) {
		try {			
			boolean hasActived = customerRepositoryImpl.hasActived(customerTable.getEmail());
			if(hasActived) {
				boolean isMatch = customerRepositoryImpl.isMatching(customerTable.getEmail(), customerTable.getPsw());
				if (isMatch) {
					return new ResponseEntity<>("{\"message\" : \"Customer login success\",\n" 
							+ "\"CustomerID\" :"  
							+ customerRepositoryImpl.findIDByEmail(customerTable.getEmail()).get(0).getId()
							+ "\n}", HttpStatus.OK) ;
				} else {
					return new ResponseEntity<>(
							"{\"message\" : \"Customer login fail: Email or Password is not correct...\"}", HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Customer hasn't active account yet, please active first...\"}", HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>("{\"message\" : \"login fail: need assistance\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	
	/**
	 * Customer forget password
	 * @param customerTable. Customer email cannot be null and must exist in database
	 * @return ResponseEntity with forget password result message
	 */
	@RequestMapping(value = "/forget_password", method = RequestMethod.POST, produces = "applications/json;charset=UTF-8")
	public ResponseEntity<String> forgetPassword(@RequestBody(required = true) CustomerTable customerTable) {
		if (customerRepositoryImpl.isValidCustomer(customerTable.getEmail())) {
			String cEmail = customerTable.getEmail();
			String cToken = customerRepositoryImpl.generateToken();
			Timestamp expireDate = new Timestamp(System.currentTimeMillis());
			if (customerRepositoryImpl.hasForgetenPWD(cEmail)) {
				logger.info("has forget password before");
				customerRepositoryImpl.updateToken(cEmail, cToken, expireDate);
			} else {
				logger.info("first time forget");
				customerRepositoryImpl.saveTokenInfo(cEmail, cToken, expireDate);

			}
			customerRepositoryImpl.sendPwdMail(cEmail, "Reset Password",
					"<p>This is a system generated mail. Please do not reply to this email ID. If you have a query or need any clarification you may:</p>"
							+ "<p>(1) Call our 24-hour Customer Care or\r\n</p>"
							+ "<p>(2) Email Us support@enfec.com\r\n</p>"
							+ "<p>Your One Time Password (OTP) for First Time Registration or Forgot Password recovery on Event Management System is: \r\n</p>"
							+ "<p><b>" + cToken + "</b></p>"
							+ "<p><a href = 'http://localhost:4200/users/resetpassword?cToken="
							+ cToken + "'>Please click this link to Reset Password</a></p>"
							+ "<p>For any problem please contact us at 24*7 Hrs. Customer Support at 18001231234 (TBD) or mail us at support@enfec.com\r\n"
							+ "Thank you for using our Event Management System\r\n</p>",
					cToken);
			logger.info("OTP send to the eamil address: {}", cEmail);
			return new ResponseEntity<String>(
					"{\"message\" : \"Send reset link and OTP to the customer email address\"}", HttpStatus.OK);
		} else {
			logger.info("Custoer not found");
			return new ResponseEntity<String>("{\"message\": \"Customer not found, invalid Email address\"}",
					HttpStatus.OK);
		}

	}

	
	/**
	 * Customer reset password
	 * @param json. the ObjectNode include one parameter: newPassword
	 * @param cToken: the customer token get from the URL
	 * @return ResponseEntity with reset password result message
	 */
	@RequestMapping(value = "/reset_password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> get(@RequestBody(required = true) ObjectNode json, @RequestParam("cToken") String cToken) {
		//String cusToken = json.get("customerToken").textValue();
		if (customerRepositoryImpl.validToken(cToken)) {
			logger.info("valid token: {} ", cToken);
			String customerEmail = customerRepositoryImpl.findEmailByToken(cToken).get(0).getCustomerEmail();
			logger.info("customer email: {} ", customerEmail);

			String newPassword = json.get("newPassword").textValue();
			customerRepositoryImpl.updatePassword(customerEmail, newPassword);
			logger.info("Password reset successfully.");
			return new ResponseEntity<>("{\"message\" : \"Password reset successfully!\"}", HttpStatus.OK);

		}
		
		logger.info("Not valid token: {}", cToken);
		return new ResponseEntity<>("{\"message\" : \"Token expired. Please re-reset password.\"}", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/LoginRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> lr(@RequestBody(required = true) ObjectNode json) {
		try {
			String cEmail = json.get("email").textValue();
			String role = customerRepositoryImpl.roleType(cEmail);
			if (role == "Customer") {
				return new ResponseEntity<>("{\"message\" : \"Customer\"}", HttpStatus.OK);
			} 
			if (role == "Organizer") {
				return new ResponseEntity<>("{\"message\" : \"Organizer\"}", HttpStatus.OK);
			} 
			else {
				return new ResponseEntity<>(
						"{\"message\" : \"Please Regist first\"}", HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>("{\"message\" : \"login fail: need assistance\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Customer change email
	 * @param customerTable. Customer email cannot be null and must exist in database
	 * @return ResponseEntity with forget password result message
	 */
	@RequestMapping(value = "/changeEmail", method = RequestMethod.POST, produces = "applications/json;charset=UTF-8")
	public ResponseEntity<String> resetEmail(@RequestBody(required = true) CustomerTable customerTable) {
		
			String cEmail = customerTable.getEmail();
			String cToken = customerRepositoryImpl.generateToken();
			Timestamp expireDate = new Timestamp(System.currentTimeMillis());
			if (customerRepositoryImpl.hasForgetenPWD(cEmail)) {
				logger.info("has forget password before");
				customerRepositoryImpl.updateToken(cEmail, cToken, expireDate);
			} else {
				logger.info("first time forget");
				customerRepositoryImpl.saveTokenInfo(cEmail, cToken, expireDate);

			}
			customerRepositoryImpl.sendPwdMail(cEmail, "Reset Email",
					"<p>This is a system generated mail. Please do not reply to this email ID. If you have a query or need any clarification you may:</p>"
							+ "<p>(1) Call our 24-hour Customer Care or\r\n</p>"
							+ "<p>(2) Email Us support@enfec.com\r\n</p>"
							+ "<p>Your One Time Password (OTP) for Reset Email Address on Event Management System is: \r\n</p>"
							+ "<p><b>" + cToken + "</b></p>"
							+ "<p><a href = 'http://localhost:4200/users/resetemail?cToken="
							//+ "<p><a href = 'http://localhost:8080/reset_email?cToken="
							+ cToken + "'>Please click this link to Reset Email</a></p>"
							+ "<p>For any problem please contact us at 24*7 Hrs. Customer Support at 18001231234 (TBD) or mail us at support@enfec.com\r\n"
							+ "Thank you for using our Event Management System\r\n</p>",
					cToken);
			logger.info("OTP send to the eamil address: {}", cEmail);
			return new ResponseEntity<String>(
					"{\"message\" : \"Send reset link and OTP to the customer email address\"}", HttpStatus.OK);
		}
	
	
	/**
	 * Customer reset email
	 * @param json. the ObjectNode include one parameter: newPassword
	 * @param cToken: the customer token get from the URL
	 * @return ResponseEntity with reset password result message
	 */
	@RequestMapping(value = "/reset_email", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> setEmail(@RequestBody(required = true) ObjectNode jEmail, @RequestParam("cToken") String cToken) {
		if (customerRepositoryImpl.validToken(cToken)) {
			logger.info("valid token: {} ", cToken);
			String oldEmail = customerRepositoryImpl.findEmailByToken(cToken).get(0).getCustomerEmail();
			logger.info("old email: {} ", oldEmail);
			int cID = customerRepositoryImpl.findIDByEmail(oldEmail).get(0).getId();
			logger.info("customer ID: {} ", cID);

			String newEmail = jEmail.get("newEmail").textValue();
			customerRepositoryImpl.updateEmail(cID, newEmail);
			logger.info("Email reset successfully.");
			
			customerRepositoryImpl.sendGreetMail(newEmail, "Welcome to EMS", 
					"<p>You have successfully changed your account to this Email Address</p>"
					+"<p>Thank you for using our Event Management System\r\n</p>", 
					cToken);
			logger.info("Greeting send to the eamil address: {}", newEmail);
			
			
			return new ResponseEntity<>("{\"message\" : \"Email reset successfully!\"}", HttpStatus.OK);

		}
		logger.info("Not valid token: {}", cToken);
		return new ResponseEntity<>("{\"message\" : \"Invalid Toke. Please re-reset Email.\"}", HttpStatus.OK);
	}
	
	

}

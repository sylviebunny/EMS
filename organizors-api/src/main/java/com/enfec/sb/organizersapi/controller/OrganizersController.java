package com.enfec.sb.organizersapi.controller;

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

import com.enfec.sb.organizersapi.model.OrganizersTable;
import com.enfec.sb.organizersapi.repository.OrganizersRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class OrganizersController {

	private static final Logger logger = LoggerFactory
			.getLogger(OrganizersController.class);

	@Autowired
	OrganizersRepositoryImpl organizersRepositoryImpl;

	@RequestMapping(value = "/getdevice-info/{organizer_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizersList(@PathVariable String organizer_id) {

		try {
			List<OrganizersTable> organizorsList = organizersRepositoryImpl
					.getOrganizer(organizer_id);
			if (organizorsList.isEmpty()) {
				logger.info("No device found for: {} ", organizer_id);
				return new ResponseEntity<>(
						"{\"message\" : \"No device found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((organizersRepositoryImpl
								.getOrganizer(organizer_id))), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting device info: {} ",
					e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting device info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/registerdevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerOrganizers(
			@RequestBody(required = true) OrganizersTable organizorsTable) {

		try {
			int affectedRow = organizersRepositoryImpl
					.registerOrganizer(organizorsTable);

			if (affectedRow == 0) {
				logger.info("Device not registered chip_id: {} ",
						organizorsTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Device not registerd\"}",
						HttpStatus.OK);
			} else {
				logger.info("Device registered chip_id: {} ",
						organizorsTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Device Registered\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Accnt_id: {} ", organizorsTable.getOrganizer_id());
			return new ResponseEntity<>("{\"message\" : \"Invalid organizor_id\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in Registering device :{}",
					exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in registering device info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/updatedevice", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateOrganizers(
			@RequestBody(required = true) OrganizersTable organizersTable) {
		
		try {
			int affectedRow = organizersRepositoryImpl
					.updateOrganizer(organizersTable);

			if (affectedRow == 0) {
				logger.info("Device not updated chip_id: {} ",
						organizersTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Device not found\"}", HttpStatus.OK);
			} else {
				logger.info("Device updated chip_id: {} ",
						organizersTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Device updated\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Accnt_id: {} ", organizersTable.getOrganizer_id());
			return new ResponseEntity<>("{\"message\" : \"Invalid accnt_id\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating device :{}",
					exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in registering device info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

}

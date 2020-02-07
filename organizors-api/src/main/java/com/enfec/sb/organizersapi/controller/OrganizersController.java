package com.enfec.sb.organizersapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@RequestMapping(value = "/getorganizer-info/{organizer_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizersList(@PathVariable String organizer_id) {

		try {
			List<OrganizersTable> organizorsList = organizersRepositoryImpl
					.getOrganizer(organizer_id);
			if (organizorsList.isEmpty()) {
				logger.info("No organizer found for: {} ", organizer_id);
				return new ResponseEntity<>(
						"{\"message\" : \"No organizer found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((organizersRepositoryImpl
								.getOrganizer(organizer_id))), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception in getting organizer info: {} ",
					e.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in getting organizer info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/registerorganizer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerOrganizers(
			@RequestBody(required = true) OrganizersTable organizorsTable) {

		try {
			int affectedRow = organizersRepositoryImpl
					.registerOrganizer(organizorsTable);

			if (affectedRow == 0) {
				logger.info("Organizer not registered organizer_name: {} ",
						organizorsTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not registered\"}",
						HttpStatus.OK);
			} else {
				logger.info("Organizer registered organizer_name: {} ",
						organizorsTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer Registered\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Organizer_id: {} ", organizorsTable.getOrganizer_id());
			return new ResponseEntity<>("{\"message\" : \"Invalid Organizer_id\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in Registering organizer:{}",
					exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in registering organizer info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/updateorganizer", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateOrganizers(
			@RequestBody(required = true) OrganizersTable organizersTable) {

		try {
			int affectedRow = organizersRepositoryImpl
					.updateOrganizer(organizersTable);

			if (affectedRow == 0) {
				logger.info("Organizer not updated organizer_name: {} ",
						organizersTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not found\"}", HttpStatus.OK);
			} else {
				logger.info("Organizer updated organizer_name: {} ",
						organizersTable.getOrganizer_name());
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer updated\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Organizer_id: {} ", organizersTable.getOrganizer_id());
			return new ResponseEntity<>("{\"message\" : \"Invalid Organizer_id\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in updating organizer :{}",
					exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in registering organizer info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deleteorganizer/{organizer_id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteOrganizers(@PathVariable String organizer_id) {

			int affectedRow = organizersRepositoryImpl.deleteOrganizer(organizer_id);
			if (affectedRow==0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not found\"}", HttpStatus.OK);
			}else {
			return new ResponseEntity<>(
					"{\"message\" : \"Organizer deleted\"}", HttpStatus.OK);
			}

	}

}

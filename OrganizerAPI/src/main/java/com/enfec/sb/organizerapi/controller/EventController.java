package com.enfec.sb.organizerapi.controller;

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

import com.enfec.sb.organizerapi.model.EventTable;
import com.enfec.sb.organizerapi.repository.EventRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class EventController {

	@Autowired
	EventRepositoryImpl organizerRepositoryImpl;

	@RequestMapping(value = "/organizer/search/{Organizer_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizerList(@PathVariable int Organizer_ID) {
			List<EventTable> organizerList = organizerRepositoryImpl
					.getOrganizerInfo(Organizer_ID);
			if (organizerList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No organizer found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((organizerRepositoryImpl
								.getOrganizerInfo(Organizer_ID))), HttpStatus.OK);
			}
	}

	@RequestMapping(value = "/organizer/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerOrganizer(
			@RequestBody(required = true) EventTable organizerTable) {
			int affectedRow = organizerRepositoryImpl
					.registerOrganizer(organizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not registerd\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer Registered\"}", HttpStatus.OK);
			}
	}
	
	// This method for updating information of organizer
	@RequestMapping(value = "/organizer/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateOrganizer(
			@RequestBody(required = true) EventTable organizerTable) {
			int affectedRow = organizerRepositoryImpl
					.updateOrganizer(organizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer updated\"}", HttpStatus.OK);
			}
	}
	
}

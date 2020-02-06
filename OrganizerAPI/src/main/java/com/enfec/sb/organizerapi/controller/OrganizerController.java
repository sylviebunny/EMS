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

import com.enfec.sb.organizerapi.model.OrganizerTable;
import com.enfec.sb.organizerapi.repository.OrganizerRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class OrganizerController {

	@Autowired
	OrganizerRepositoryImpl organizerRepositoryImpl;

	@RequestMapping(value = "/organizer/search/{Organizer_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getDeviceList(@PathVariable int Organizer_ID) {
			List<OrganizerTable> deviceList = organizerRepositoryImpl
					.getOrganizerInfo(Organizer_ID);
			if (deviceList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No device found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((organizerRepositoryImpl
								.getOrganizerInfo(Organizer_ID))), HttpStatus.OK);
			}
	}

	@RequestMapping(value = "/organizer/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerDevice(
			@RequestBody(required = true) OrganizerTable organizerTable) {
			int affectedRow = organizerRepositoryImpl
					.registerDevice(organizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Device not registerd\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Device Registered\"}", HttpStatus.OK);
			}
	}

	@RequestMapping(value = "/organizer/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateDevice(
			@RequestBody(required = true) OrganizerTable organizerTable) {
			int affectedRow = organizerRepositoryImpl
					.updateDevice(organizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Device not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Device updated\"}", HttpStatus.OK);
			}
	}

}

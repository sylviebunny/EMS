package com.enfec.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enfec.demo.model.Address;
import com.enfec.demo.model.OrganizerTable;
import com.enfec.demo.repository.OrganizerRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class OrganizerController {
	@Autowired
	OrganizerRepositoryImpl OrganizerRepositoryImpl;

	@RequestMapping(value = "/organizer/search/{Organizer_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizerList(@PathVariable int Organizer_ID) {
			List<OrganizerTable> organizerList = OrganizerRepositoryImpl.getOrganizerInfo(Organizer_ID);
			
			if (organizerList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No device found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((OrganizerRepositoryImpl
								.getOrganizerInfo(Organizer_ID))), HttpStatus.OK);
			}
	}

	@RequestMapping(value = "/organizer/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerOrganizer(@RequestBody(required = true) OrganizerTable organizerTable) {
			int affectedRow = OrganizerRepositoryImpl.registerOrganizer(organizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not registerd\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer Registered\"}", HttpStatus.OK);
			}
	}

	@RequestMapping(value = "/organizer/addaddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createAddress(@RequestBody(required = true) Address address) {
			int affectedRow = OrganizerRepositoryImpl.createAddress(address);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Address not added\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Address added\"}", HttpStatus.OK);
			}
	}
	
	@RequestMapping(value = "/organizer/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateOrganizer(@RequestBody(required = true) OrganizerTable OrganizerTable) {
			int affectedRow = OrganizerRepositoryImpl.updateOrganizer(OrganizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer updated\"}", HttpStatus.OK);
			}
	}

	@RequestMapping(value="/organizer/delete/{Organizer_ID}",method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> deleteOrganizer(@PathVariable("Organizer_ID") int id) {
		int affectedRow = OrganizerRepositoryImpl.deleteOrganizer(id);
		if(affectedRow > 0 )  {
			return new ResponseEntity<>(
					"{\"message\" : \"Organizer deleted\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					"{\"message\" : \"Organizer is not able to delete\"}", HttpStatus.OK);
		}
	}
}

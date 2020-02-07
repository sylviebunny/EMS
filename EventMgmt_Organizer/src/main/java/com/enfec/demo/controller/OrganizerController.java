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
import com.enfec.demo.model.OrganizerContactTable;
import com.enfec.demo.model.OrganizerTable;
import com.enfec.demo.repository.OrganizerRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class OrganizerController {
	@Autowired
	OrganizerRepositoryImpl OrganizerRepositoryImpl;

	//For "Organizers" table
	@RequestMapping(value = "/organizer/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> createOrganizer(@RequestBody(required = true) OrganizerTable organizerTable) {
			int affectedRow = OrganizerRepositoryImpl.createOrganizer(organizerTable);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer not registerd\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer Registered\"}", HttpStatus.OK);
			}
	}
	
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
	
	
	//For Organizer's "Address" table
	@RequestMapping(value = "/organizer/address/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
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
	
	@RequestMapping(value = "/organizer/address/search/{Organizer_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getAddressList(@PathVariable int Organizer_ID) {
			List<Address> addressList = OrganizerRepositoryImpl.getAddressInfo(Organizer_ID);
			
			if (addressList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No address found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((OrganizerRepositoryImpl
								.getAddressInfo(Organizer_ID))), HttpStatus.OK);
			}
	}
	
	@RequestMapping(value = "/organizer/address/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateAddress(@RequestBody(required = true) Address address) {
			int affectedRow = OrganizerRepositoryImpl.updateAddress(address);

			if (affectedRow == 0) {
				return new ResponseEntity<>(
						"{\"message\" : \"Information not found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Address updated\"}", HttpStatus.OK);
			}
	}
	
	
	//For Organizer's "Contacts" table
	// This method for creating contact information of organizer
	@RequestMapping(value = "/organizer/contact/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> registerOrganizerInfo(
			@RequestBody(required = true) OrganizerContactTable organizerContactTable) {
			int affectedRow = OrganizerRepositoryImpl
					.createOrganizerContact(organizerContactTable);

			if (affectedRow == -1) {
				// Generate Runtime Exception 
				return new ResponseEntity<>(
						"{\"message\" : \"Address_ID or organizer_ID is not in Database\"}",
						HttpStatus.OK);
			} else if (affectedRow == Integer.MIN_VALUE) {
				return new ResponseEntity<>(
						"{\"message\" : \"Error that has not been defined\"}", 
						HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer contact created\"}", HttpStatus.OK);
			}
	}
	
	@RequestMapping(value = "/organizer/contact/search/{Organizer_ID}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getOrganizerContactList(@PathVariable int Organizer_ID) {
			List<OrganizerContactTable> organizerContactList = OrganizerRepositoryImpl
					.getOrganizerContactInfo(Organizer_ID);
			if (organizerContactList.isEmpty()) {
				return new ResponseEntity<>(
						"{\"message\" : \"No organizer found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((OrganizerRepositoryImpl
								.getOrganizerContactInfo(Organizer_ID))), HttpStatus.OK);
			}
	}
	
	// This method for creating contact information of organizer
	@RequestMapping(value = "/organizer/contact/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateOrganizerInfo(
			@RequestBody(required = true) OrganizerContactTable organizerContactTable) {
			int affectedRow = OrganizerRepositoryImpl
					.updateOrganizerContact(organizerContactTable);

			if (affectedRow == 0) {
				// Generate Runtime Exception 
				return new ResponseEntity<>(
						"{\"message\" : \"Address_ID or organizer_ID is not in Database\"}",
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						"{\"message\" : \"Organizer contact successfully updated\"}", HttpStatus.OK);
			}
	}
}

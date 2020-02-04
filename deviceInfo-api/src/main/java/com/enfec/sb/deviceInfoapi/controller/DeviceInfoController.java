package com.enfec.sb.deviceInfoapi.controller;

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

import com.enfec.sb.deviceInfoapi.model.DeviceInfoTable;
import com.enfec.sb.deviceInfoapi.repository.DeviceInfoRepositoryImpl;
import com.google.gson.Gson;

@RestController
public class DeviceInfoController {

	private static final Logger logger = LoggerFactory
			.getLogger(DeviceInfoController.class);

	@Autowired
	DeviceInfoRepositoryImpl deviceInfoRepositoryImpl;

	@RequestMapping(value = "/getdevice-info/{accnt_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> getDeviceList(@PathVariable String accnt_id) {

		try {
			List<DeviceInfoTable> deviceList = deviceInfoRepositoryImpl
					.getDeviceInfo(accnt_id);
			if (deviceList.isEmpty()) {
				logger.info("No device found for: {} ", accnt_id);
				return new ResponseEntity<>(
						"{\"message\" : \"No device found\"}", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Gson().toJson((deviceInfoRepositoryImpl
								.getDeviceInfo(accnt_id))), HttpStatus.OK);
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
	public ResponseEntity<String> registerDevice(
			@RequestBody(required = true) DeviceInfoTable deviceInfoTable) {

		try {
			int affectedRow = deviceInfoRepositoryImpl
					.registerDevice(deviceInfoTable);

			if (affectedRow == 0) {
				logger.info("Device not registered chip_id: {} ",
						deviceInfoTable.getChip_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Device not registerd\"}",
						HttpStatus.OK);
			} else {
				logger.info("Device registered chip_id: {} ",
						deviceInfoTable.getChip_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Device Registered\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Accnt_id: {} ", deviceInfoTable.getAccnt_id());
			return new ResponseEntity<>("{\"message\" : \"Invalid accnt_id\"}",
					HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			logger.error("Exception in Registering device :{}",
					exception.getMessage());
			return new ResponseEntity<>(
					"{\"message\" : \"Exception in registering device info\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/updatedevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> updateDevice(
			@RequestBody(required = true) DeviceInfoTable deviceInfoTable) {

		try {
			int affectedRow = deviceInfoRepositoryImpl
					.updateDevice(deviceInfoTable);

			if (affectedRow == 0) {
				logger.info("Device not updated chip_id: {} ",
						deviceInfoTable.getChip_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Device not found\"}", HttpStatus.OK);
			} else {
				logger.info("Device updated chip_id: {} ",
						deviceInfoTable.getChip_id());
				return new ResponseEntity<>(
						"{\"message\" : \"Device updated\"}", HttpStatus.OK);
			}

		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Invalid Accnt_id: {} ", deviceInfoTable.getAccnt_id());
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

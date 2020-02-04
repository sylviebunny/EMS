package com.enfec.sb.deviceInfoapi.repository;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.enfec.sb.deviceInfoapi.model.DeviceInfoRowmapper;
import com.enfec.sb.deviceInfoapi.model.DeviceInfoTable;

@Component
public class DeviceInfoRepositoryImpl implements DeviceInfoRepository {
	private static final Logger logger = LoggerFactory.getLogger(DeviceInfoRepositoryImpl.class);
	
	final String SELECT_DEVICE = "select ACCNT_ID,CHIP_ID,SERIAL_NUMBER,MAKE,MODEL,FIRMWARE,WIFI_SSID,WIFI_PWD from DEVICE_INFO where ACCNT_ID =?";

	final String REGISTER_DEVICE = "INSERT INTO DEVICE_INFO(ACCNT_ID,CHIP_ID,SERIAL_NUMBER,MAKE,MODEL,FIRMWARE,WIFI_SSID,WIFI_PWD) VALUES "
			+ "(:accnt_id,:chip_id,:serial_number,:make,:firmware,:model,:wifi_ssid,:wifi_pwd)";
	
	final String UPDATE_DEVICE_INFO = "UPDATE DEVICE_INFO SET SERIAL_NUMBER = :serial_number ,MAKE=:make,MODEL=:model"
			+ ",FIRMWARE=:firmware,WIFI_SSID=:wifi_ssid,WIFI_PWD=:wifi_pwd where ACCNT_ID = :accnt_id AND CHIP_ID =:chip_id" ;	
	
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public  List<DeviceInfoTable> getDeviceInfo(String accnt_id) {
		
		return jdbcTemplate.query(SELECT_DEVICE,new Object[] { accnt_id }, new DeviceInfoRowmapper());
	}

	@Override
	public int registerDevice(DeviceInfoTable deviceInfoTable) {
		int affectedRow;
		Map<String, Object> param = deviceInfoMap(deviceInfoTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Register Device Info : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_DEVICE, pramSource);
		
		return affectedRow;
	
		
	}

	@Override
	public int updateDevice(DeviceInfoTable deviceInfoTable) {
		int affectedRow;
		Map<String, Object> param = deviceInfoMap(deviceInfoTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating Device Info : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_DEVICE_INFO, pramSource);
		
		return affectedRow;

	}
	
	private Map<String, Object> deviceInfoMap(DeviceInfoTable deviceInfoTable) {
		Map<String, Object>param = new HashMap<>();
	
			if (deviceInfoTable.getAccnt_id() != 0) {
				param.put("accnt_id", deviceInfoTable.getAccnt_id());
			} else {
				logger.error("Account id missing");
				throw new NullPointerException("Accnt_id cannot be null");
			}
		
		param.put("chip_id", deviceInfoTable.getChip_id().isEmpty() ? null:deviceInfoTable.getChip_id());
		param.put("serial_number", deviceInfoTable.getSerial_number()==null ? null:deviceInfoTable.getSerial_number());
		param.put("make", deviceInfoTable.getMake()==null ? null:deviceInfoTable.getMake());
		param.put("firmware", deviceInfoTable.getFirmware()==null ? null:deviceInfoTable.getFirmware());
		param.put("model", deviceInfoTable.getModel()==null ? null :deviceInfoTable.getModel());
		param.put("wifi_ssid", deviceInfoTable.getWifi_ssid()==null? null :deviceInfoTable.getWifi_ssid());
		param.put("wifi_pwd", deviceInfoTable.getWifi_pwd()==null? null : Base64.getEncoder().encode((deviceInfoTable.getWifi_pwd().getBytes())));
		return param;
	}

	

}


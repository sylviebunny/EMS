package com.enfec.sb.deviceInfoapi.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DeviceInfoTable {
	
	int accnt_id;
	private String chip_id;
	private String serial_number;
	private String make;
	private String model;
	private String firmware;
	private String wifi_ssid;
	private String wifi_pwd;

}

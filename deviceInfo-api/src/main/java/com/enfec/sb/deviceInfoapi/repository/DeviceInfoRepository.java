package com.enfec.sb.deviceInfoapi.repository;

import com.enfec.sb.deviceInfoapi.model.DeviceInfoTable;

public interface DeviceInfoRepository {

	public Object getOrganizerInfo(int accnt_id);
	public int registerDevice(DeviceInfoTable deviceInfoTable);
	
	public int updateDevice(DeviceInfoTable deviceInfoTable);
	
}

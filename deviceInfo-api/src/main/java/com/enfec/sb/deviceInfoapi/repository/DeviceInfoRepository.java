package com.enfec.sb.deviceInfoapi.repository;

import com.enfec.sb.deviceInfoapi.model.OrganizerTable;

public interface DeviceInfoRepository {

	public Object getOrganizerInfo(int accnt_id);
	public int registerDevice(OrganizerTable deviceInfoTable);
	
	public int updateDevice(OrganizerTable deviceInfoTable);
	
}

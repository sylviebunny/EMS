package com.enfec.EMS.CustomerAPI.repository;

import com.enfec.EMS.CustomerAPI.model.CustomerTable;

public interface CustomerRepository {
	public Object getCustomer(String id);
	public int registerCustomer(CustomerTable customerTable);
	public int updateCustomer(CustomerTable customerTable);
	public int deleteCustomer(String id);
}

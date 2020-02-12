package com.enfec.EMS.CustomerAPI.model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Component
@Getter
@Setter
public class CustomerTable {
	private int id;
	private String name;
	private String email;
	private String psw;
	private String phone;

}

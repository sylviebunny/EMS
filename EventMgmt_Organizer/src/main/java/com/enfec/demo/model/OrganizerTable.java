package com.enfec.demo.model;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Component
@Getter
@Setter
@ApiModel
public class OrganizerTable {
	private int organizer_id;
	private String organizer_name; 
	private String email_address;
	private String password;
	private String other_details;
}

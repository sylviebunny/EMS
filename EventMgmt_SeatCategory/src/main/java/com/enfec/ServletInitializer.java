package com.enfec;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Spring Boot Servlet Initializer
* Class: ServletInitializer
*
************************************************/
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EventMgmtSeatCategoryApplication.class);
	}

}

package com.enfec.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * API for organizer
 */
@SpringBootApplication
public class EventMgmtApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EventMgmtApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(EventMgmtApplication.class, args);
	}

}

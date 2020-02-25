package com.enfec.sb.eventapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Event API application initializer
 * @author heidi huo
 *
 */
@SpringBootApplication
public class EventApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EventApiApplication.class, args);

	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EventApiApplication.class);
	}

}

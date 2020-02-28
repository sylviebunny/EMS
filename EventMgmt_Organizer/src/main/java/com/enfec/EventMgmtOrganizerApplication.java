package com.enfec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Organizer API
* Class: EventMgmtOrganizerApplication
*
************************************************/
@SpringBootApplication
@EnableSwagger2
public class EventMgmtOrganizerApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EventMgmtOrganizerApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(EventMgmtOrganizerApplication.class, args);
	}

	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.enfec")).build();   
    }

}

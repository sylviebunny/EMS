package com.enfec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/************************************************
*
* Author: Sylvia Zhao
* Assignment: Venue API
* Class: EventMgmtVenueApplication
*
************************************************/
@SpringBootApplication
@EnableSwagger2
public class EventMgmtVenueApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventMgmtVenueApplication.class, args);
	}

	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.enfec")).build();   
    }
}

package com.enfec.EMS.CustomerOrderAPI;

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
* Author: Chad Chai
* Assignment: Customer Order Application
* Class: CustomerOrderApiApplication
************************************************/
@SpringBootApplication
@EnableSwagger2
public class CustomerOrderApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CustomerOrderApiApplication.class, args);
	}
	
	@Bean
	public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.enfec")).build();  
    }
	
	
	 @Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		 return builder.sources(CustomerOrderApiApplication.class);
	 }

}

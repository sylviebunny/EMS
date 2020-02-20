package com.enfec.sb.refundapi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RefundApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RefundApiApplication.class, args);
		
	}
	
   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(RefundApiApplication.class);
   }

}

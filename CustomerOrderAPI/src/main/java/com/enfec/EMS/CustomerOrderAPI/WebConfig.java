package com.enfec.EMS.CustomerOrderAPI;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/************************************************
* Author: Chad Chai
* Assignment: Web Config
* Class: WebConfig
************************************************/

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	
	/**
	 * Web configuration for CORS policy
	 * Used for deploying to AWS and get connection with front-end
	 */
	@Override
    public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")

			.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",

				"Access-Control-Request-Headers", "X-Auth-Token")

			.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")

			.allowCredentials(true).maxAge(3600);
    }
	
	/**
	 * Web configuration for Swagger UI
	 * Used for deploying to AWS, if cannot get swagger ui page through AWS url
	 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");  
   }
}

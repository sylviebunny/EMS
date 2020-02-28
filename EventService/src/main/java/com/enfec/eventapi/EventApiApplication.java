package com.enfec.eventapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Event API application initializer
 * @author heidi huo
 *
 */
@SpringBootApplication
@EnableSwagger2
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

	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.enfec")).build();   
    }
}

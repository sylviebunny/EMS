package com.enfec.sb.refundapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * API Application for refund
 */
@SpringBootApplication
public class RefundApiApplication extends SpringBootServletInitializer {

    /**
     * Main function
     */
    public static void main(String[] args) {
        SpringApplication.run(RefundApiApplication.class, args);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RefundApiApplication.class);
    }

}

/**
 * 
 */
package com.enfec.eventapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/**
 * @author heidihuo
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * {@inheritDoc}
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .antMatcher("/**")
        .authorizeRequests().and()
        .httpBasic()
        .and()
        .authorizeRequests().anyRequest().authenticated().and().cors();
    }
    
    /**
     * Set user and password for authentication
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("{noop}***").roles("USER");
        auth.inMemoryAuthentication().withUser("ui").password("{noop}***").roles("USER");
    }
    
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    /**
     *Allow to load swagger ui page
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }
}

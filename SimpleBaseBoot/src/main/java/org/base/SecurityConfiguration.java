package org.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


@Configuration
@Import({SecurityAutoConfiguration.class,SecuritySettings.class})
@ComponentScan(basePackages="org.base")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	final private  Logger logger = LoggerFactory.getLogger( getClass() );
	final static private String HELLO_SECURE_URL="/helloWithSecurity" ;
	
	private static final String PASSWORD = "password";

	private static final String USER_NAME = "user";

	private static final String ROLE_USER = "USER";
	
	@Autowired
	SecuritySettings settings ;

	
	protected void configure ( HttpSecurity http )
			throws Exception {

		logger.info( "\n\n\n ************* loading security rules: {},{} \n\n\n", 
			settings.getTestVariable(), settings.getDn() );
		// @formatter:off

        http
            .authorizeRequests()
            	.antMatchers("/hello").permitAll()
            	
            	.antMatchers(HELLO_SECURE_URL)
            		.hasRole(ROLE_USER)
            		
            		
                //.antMatchers("/**").permitAll()
	            .anyRequest().authenticated()
                .and()
	                
            .formLogin()
                .permitAll()
                .defaultSuccessUrl( HELLO_SECURE_URL, true )
                .and()
                
             // for credential scenario
            .httpBasic()
            	.and()
	            	
            .logout()
                .permitAll();
    	

    	 
    	// @formatter:on
	}

	@Autowired
	public void configureGlobal ( AuthenticationManagerBuilder auth )
			throws Exception {
		// https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding
		auth
			.inMemoryAuthentication()
			.passwordEncoder(NoOpPasswordEncoder.getInstance())
			.withUser( USER_NAME ).password( PASSWORD ).roles( ROLE_USER );
	}
	

}

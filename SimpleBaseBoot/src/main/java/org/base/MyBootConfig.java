package org.base;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableCaching
@EnableRetry

@Import({ServletConfiguration.class, SecurityConfiguration.class})
public class MyBootConfig {

	final private  Logger logger = LoggerFactory.getLogger( getClass() );
	@PostConstruct
	public void showInitMessage () {
		
		logger.info( "GOt here" );
	}

}

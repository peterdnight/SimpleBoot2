package org.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties ( prefix = "demo" )
public class ServletConfiguration {

	final Logger logger = LoggerFactory.getLogger( getClass() );
	
	String testVariable="" ;
	
	@Bean
	public ServletWebServerFactory servletContainer () {
		logger.info( "\n\n\n\n Building a custom tomcat factory, testVariable: {} \n\n\n\n", testVariable );
		TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();


		// tomcatFactory.

		// tunnels through with no JSESSIONID
		// tomcat.addContextValves( createRemoteIpValves() );
		return tomcatFactory;

	}

	public String getTestVariable () {
		return testVariable;
	}

	public void setTestVariable ( String testVariable ) {
		this.testVariable = testVariable;
	}
}

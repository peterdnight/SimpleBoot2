package org.sample;

import org.base.MyBootAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@MyBootAnnotation
public class My_Application implements WebMvcConfigurer {

	final private static Logger logger = LoggerFactory.getLogger( My_Application.class );
	public static void main(String[] args) {
		SpringApplication.run(My_Application.class, args);
	}
	

	public void addResourceHandlers ( ResourceHandlerRegistry registry ) {

			logger.warn( "\n\n\n Desktop detected: Caching DISABLED \n\n\n" );
		int	ONE_YEAR_SECONDS = 0;


		VersionResourceResolver versionResolver = new VersionResourceResolver()
			// .addFixedVersionStrategy( version, "/**/*.js" ) //Enable this
			// if we use a JavaScript module loader
			.addContentVersionStrategy( "/**" );

		// A Handler With Versioning - note images in css files need to be
		// resolved.
		registry
			.addResourceHandler( "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg" )
			.addResourceLocations( "classpath:/static/", "classpath:/public/" )
			.setCachePeriod( ONE_YEAR_SECONDS )
			.resourceChain( true )
			.addResolver( versionResolver );

	}
}

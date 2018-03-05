package simple.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.assertj.core.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith ( SpringRunner.class )
@SpringBootTest ( classes = Disable_Security.SimpleApp.class , webEnvironment = WebEnvironment.RANDOM_PORT )
@ActiveProfiles ( "junit" )
@DirtiesContext
public class Disable_Security {

	final static private Logger logger = LoggerFactory.getLogger( Disable_Security.class );

	@BeforeClass
	// @Before
	static public void setUpBeforeClass ()
			throws Exception {

		System.out.println( "Starting logging" );
	}

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 
	 * Simple test app that excludes security autoconfiguration
	 *
	 */
	@SpringBootApplication ( //
			exclude = {
					SecurityAutoConfiguration.class
			} )
	public static class SimpleApp {

		@RestController
		static public class Hello {

			@GetMapping ( "/hi" )
			public String hi () {
				return "Hello" +
						LocalDateTime.now()
							.format( DateTimeFormatter
								.ofPattern( "HH:mm:ss,   MMMM d  uuuu " ) );
			}

			@Autowired
			ObjectMapper jsonMapper;

		} 

		// 
		@Configuration
		@ConditionalOnProperty ( "does.not.exist" )
		public static class MySecurity extends WebSecurityConfigurerAdapter {
			 public void configure ( WebSecurity web )
			 throws Exception {
			
			 logger.info( "\n\n\n ******* Ignoring all request ****** \n\n\n"
			 );
			
			 web.ignoring().anyRequest();
			
			 }
		}

	}

	@Test
	public void verify_context_does_not_include_security_beans () {

		logger.info( "beans loaded: {}", applicationContext.getBeanDefinitionCount() );

		logger.info( "beans loaded: {}", Arrays.asList( applicationContext.getBeanDefinitionNames() ) );

		// assertThat( applicationContext.getBeanDefinitionCount() )
		// .as( "Spring Bean count" )
		// .isGreaterThan( 200 );

		assertThat( applicationContext.containsBean( UserDetailsServiceAutoConfiguration.class.getName() ) )
			.as( "UserDetailsServiceAutoConfiguration is disabled" )
			.isFalse();

		assertThat( applicationContext.containsBean( SecurityAutoConfiguration.class.getName() ) )
			.as( "securityAutoConfiguration is disabled" )
			.isFalse();

		// Assert.assertFalse( true);

	}

	@LocalServerPort
	private int testPort;

	@Autowired
	RestTemplateBuilder restTemplateBuilder;

	@Test
	public void http_get_hi_from_simple_app ()
			throws Exception {
		String simpleUrl = "http://localhost:" + testPort + "/hi";

		logger.info( "hitting url: {}", simpleUrl );
		// mock does much validation.....

		TestRestTemplate restTemplate = new TestRestTemplate( restTemplateBuilder );

		ResponseEntity<String> response = restTemplate.getForEntity( simpleUrl, String.class );

		logger.info( "result:\n" + response );

		assertThat( response.getBody() )
			.as( "Http response from test" )
			.startsWith( "Hello" );
	}

}

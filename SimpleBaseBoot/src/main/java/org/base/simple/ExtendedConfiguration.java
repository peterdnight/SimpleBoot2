package org.base.simple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties ( prefix = "demo" )
public class ExtendedConfiguration {
	String testVariable="" ;

	public String getTestVariable () {
		return testVariable;
	}

	public void setTestVariable ( String testVariable ) {
		this.testVariable = testVariable;
	}
	
	@Value ( "${security.dir.dn:notUsed}" )
	private String dn;

	public String getDn () {
		return dn;
	}

	public void setDn ( String dn ) {
		this.dn = dn;
	}
}

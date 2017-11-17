package io.pivotal.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO-00: Start both "oauth2-github-auth-server-client" and
//          "oauth2-github-auth-server-resource-server" applications
// TODO-01: In the console of "oauth2-github-auth-server-client" application, 
//          observe that the access token obtained from the GitHub authorization
//          server gets displayed 
// TODO-02: Using that token, access the resource server directly using curl or postman
//          curl -H "Authorization: Bearer <Token>" http://localhost:8001/resource-server/resource-in-server
//          You should see "Message from resource server" message
// TODO-03: Access the resource server from the "oauth2-github-auth-server-client" application
//          http://localhost:8080/resource-in-client either via browser or curl/postman
//          You should see "Message from resource server" message - this verifies that
// TODO-04: Go to GitHub.com and create your own oauth2 application, which results in its
//          own "clientId" and "clientSecret". Use these new values in the the "application.yml"
//          file of the "oauth2-github-auth-server-client" application
@RestController
@EnableOAuth2Sso
public class HelloController extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private OAuth2ClientContext clientContext;
	
	@Autowired
    private OAuth2RestTemplate oauth2RestTemplate;	
	
	// Try to get token data without accessing remote microservice
	@GetMapping("hello")
	public String sayHello2() {
		OAuth2AccessToken oAuth2AccessToken = clientContext.getAccessToken();
		System.out.println("--->Token: " + oAuth2AccessToken.getValue());
		return "hello";
	}
	
	// Access remote microservice
	@GetMapping("resource-in-client")
	public String getResourceFromResourceServer() {	
		String resourceRetrieved 
		= oauth2RestTemplate.getForObject("http://localhost:8001/resource-server/resource-in-server", String.class);
		return resourceRetrieved;
	}
	
	// Configure security
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/login**")
			    .permitAll()
			.anyRequest()
			    .authenticated();
	}

}

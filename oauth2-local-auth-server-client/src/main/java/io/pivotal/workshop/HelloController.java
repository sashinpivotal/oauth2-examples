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

// TODO-10: Start "oauth2-local-auth-server-client" and
//          "oauth2-local-auth-server-resource-server", and 
//          "oauth2-local-auth-server" applications

// TODO-11: Using Chrome Incognito browser, access
//          http://localhost:9000/client/displaytoken and observe that the
//          the auth server prompts the username and password.  Enter
//          "username" and "userpassword" as credentials. Observe that
//          the auth server then prompts you to approve "scope.read" and
//          "scope.tell-joke".  Approve them.
//          Observe that the access token obtained from the local authorization
//          server gets displayed both in console and webpage

// TODO-12: Using that token, access the resource server directly using curl or postman
//          curl -H "Authorization: Bearer <Token>" http://localhost:9001/resource-server/resource-in-server
//          You should see "Message from resource server" message

// TODO-13: Using a new Chrome Incognito browser, access the resource server from the "oauth2-local-auth-server-client" application
//          accessing the URL of "http://localhost:9000/client/resource-in-client" either via browser 
//          or curl/postman.
//          You should see "client retrieved Message from resource server" message - this verifies that the
//          client application accesses the resource server application successfully using
//          the access token

// TODO-14: Access the userinfo from the local authorization server using curl or postman
//          curl -H "Authorization: Bearer <Token>" http://localhost:9002/auth-server/user
//          Observe that userinfo gets displayed in JSON format
@RestController
@EnableOAuth2Sso
public class HelloController extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private OAuth2ClientContext clientContext;
	
	@Autowired
    private OAuth2RestTemplate oauth2RestTemplate;	
	
	@GetMapping("/displaytoken")
	public String displayToken() {
		OAuth2AccessToken oAuth2AccessToken = clientContext.getAccessToken();
		System.out.println("--->Token: " + oAuth2AccessToken.getValue());
		return "Token: " + oAuth2AccessToken.getValue();
	}
	
	@GetMapping("/resource-in-client")
	public String getResourceFromResourceServer() {		
		String resourceRetrieved = oauth2RestTemplate.getForObject("http://localhost:9001/resource-server/resource-in-server", String.class);
		return "client retrieved " + resourceRetrieved;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			   .antMatchers("/", "/login**")
			   .permitAll()
			.anyRequest()
			   .authenticated();
	}

}

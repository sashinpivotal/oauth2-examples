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

// TODO-30: Start both "oauth2-github-auth-server-client" and
//          "oauth2-github-auth-server-resource-server" applications

// TODO-31: Using Chrome Incognito browser, access
//          http://localhost:8080/displaytoken and observe that the
//          the auth server prompts the username and password.  Enter
//          your GitHub username/password as credentials.
//          Observe that the access token obtained from the GitHub authorization
//          server gets displayed both in console and on a web page

// TODO-32: Using that token, access the resource server directly using curl or postman
//          curl -H "Authorization: Bearer <Token>" http://localhost:8001/resource-server/resource-in-server
//          You should see "Message from resource server" message

// TODO-33: Access the resource server from the "oauth2-github-auth-server-client" application
//          accessing the URL of "http://localhost:8080/resource-in-client" either via browser 
//          or curl/postman.
//          You should see "client retrieved Message from resource server" message - this verifies that the
//          client application accesses the resource server application successfully using
//          the access token

// TODO-34: Access the userinfo from the GitHub authorization server using curl or postman
//          curl -H "Authorization: Bearer <Token>" https://api.github.com/user
//          Observe that userinfo gets displayed in JSON format

// TODO-35: Go to GitHub.com and create your own OAuth2 application, which results in its
//          own "clientId" and "clientSecret". Use these new values in the the "application.yml"
//          file of the "oauth2-github-auth-server-client" application
//          The steps to follow to create your own OAuth2 application is as following:
//          -Log in to GitHub
//          -In upper right corner, click your profile picture
//          -Select "Settings" in the drop down menu
//          -Select "Developer settings" on the left
//          -Click "New OAuth app" button
//          -Fill in the Application name field with whatever name of your choice
//          -Fill in "Homepage URL" with http://localhost:8080
//          -Fill in "Authorization callback URL" with http://localhost:8080/login

@RestController
@EnableOAuth2Sso
public class HelloController extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private OAuth2ClientContext clientContext;
	
	@Autowired
    private OAuth2RestTemplate oauth2RestTemplate;	
	
	// Retrieve access token without accessing remote microservice
	@GetMapping("/displaytoken")
	public String displayToken() {
		OAuth2AccessToken oAuth2AccessToken = clientContext.getAccessToken();
		System.out.println("--->Token: " + oAuth2AccessToken.getValue());
		return "Token: " + oAuth2AccessToken.getValue();
	}
	
	// Access remote microservice
	@GetMapping("/resource-in-client")
	public String getResourceFromResourceServer() {	
		String resourceRetrieved 
		= oauth2RestTemplate.getForObject("http://localhost:8001/resource-server/resource-in-server", String.class);
		return "client retrieved " + resourceRetrieved;
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

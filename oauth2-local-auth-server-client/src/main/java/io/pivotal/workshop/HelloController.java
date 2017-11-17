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

// TODO-13: Now access the resource in the resource server via the
//         following link - make sure you restart your browser instances
//         in order to run it clean state or use Incognito window. When
//         asked for the username and password of "localhost:9002", enter
//         "username"/"userpassword" (without quotes) as credentials.
//   http://localhost:9000/client/resource-in-client

@RestController
@EnableOAuth2Sso
public class HelloController extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private OAuth2ClientContext clientContext;
	
	@Autowired
    private OAuth2RestTemplate oauth2RestTemplate;	
	
	@GetMapping("hello")
	public String sayHello2() {
		OAuth2AccessToken oAuth2AccessToken = clientContext.getAccessToken();
		System.out.println("--->Token: " + oAuth2AccessToken.getValue());
		return "hello";
	}
	
	@GetMapping("resource-in-client")
	public String getResourceFromResourceServer() {
		OAuth2AccessToken oAuth2AccessToken = clientContext.getAccessToken();
		System.out.println("--->Token: " + oAuth2AccessToken.getValue());
		
		String resourceRetrieved = oauth2RestTemplate.getForObject("http://localhost:9001/resource-server/resource-in-server", String.class);
		return resourceRetrieved;
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

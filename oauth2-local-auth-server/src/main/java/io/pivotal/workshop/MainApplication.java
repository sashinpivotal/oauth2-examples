package io.pivotal.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

// By adding @EnableAuthorizationServer, the following
// two endpoints are automatically added
//   ./oauth/token (token endpoint)
//   ./oauth/authorize (authorization endpoint)

//TODO-21: Verify the presence of the above endpoints using "./auth-server/mappings" 
//         actuator endpoint  using "username/userpassword" credential

//TODO-22: Run the following command to get access token from this auth server
//curl -X POST http://localhost:9002/auth-server/oauth/token -u myclient:myclientsecret -d grant_type=password -d username=username -d password=userpassword

//TODO-23: Once you get access token from the auth server,
//try access the resource server using the token using following command
//curl -i -H "Authorization: bearer <token>" http://localhost:9001/resource-server/resource-in-server

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
}

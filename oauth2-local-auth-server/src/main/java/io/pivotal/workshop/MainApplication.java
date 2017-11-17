package io.pivotal.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

// By adding @EnableAuthorizationServer, the following
// two endpoints are automatically added
//   ./oauth/token (token endpoint)
//   ./oauth/authorize (authorization endpoint)

//TODO-10: Start "oauth-local-auth-server", "oauth-local-auth-server-resource-server",
//         "oauth-local-auth-server-client" applications
//TODO-11: Run the following command to get access token from this auth server
//curl -X POST http://localhost:9002/auth-server/oauth/token -u myclient:myclientsecret -d grant_type=password -d username=username -d password=userpassword
@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
}

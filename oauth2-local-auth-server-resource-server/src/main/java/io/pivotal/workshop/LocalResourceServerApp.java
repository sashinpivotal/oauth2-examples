package io.pivotal.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class LocalResourceServerApp {

	public static void main(String[] args) {
		SpringApplication.run(LocalResourceServerApp.class, args);
	}

//	@Bean
//	public ResourceServerTokenServices tokenService() {
//		RemoteTokenServices tokenServices = new RemoteTokenServices();
//		tokenServices.setClientId("myresource");
//		tokenServices.setClientSecret("myresourcesecret");
//		tokenServices.setCheckTokenEndpointUrl("http://localhost:9002/auth-server/oauth/check_token");
//		return tokenServices;
//	}
}

package io.pivotal.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO-12: Once you get access token from the auth server,
// try access the resource server using the token using following command
// curl -i -H "Authorization: bearer <token>" http://localhost:9001/resource-server/resource-in-server
@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}

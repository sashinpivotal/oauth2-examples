package io.pivotal.workshop;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableResourceServer
public class ResourceController {
	
	@GetMapping("resource-in-server")
	public String sayHello2() {
		return "Message from resource server";
	}

}

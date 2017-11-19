package io.pivotal.workshop;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("/hello")
	public String sayHello2() {
		return "hello";
	}
	
	// This endpoint of the auth server is accessed by the resource server 
	// to verify access token it received from the client is valid
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}

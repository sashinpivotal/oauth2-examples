package io.pivotal.workshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
	
	@GetMapping("/resource-in-server")
	public String sayHello() {
		return "Message from resource server";
	}

}

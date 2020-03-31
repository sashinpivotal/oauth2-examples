package io.pivotal.workshop;

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
//          -Click "Register application"
//          -Observe that new "clientId" and "clientSecret" are generated for you

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@RestController
public class ClientController {

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    // Retrieve access token
    @GetMapping("/displaytoken")
    public String displayToken(Principal principal) {
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient("github", principal.getName());
        String tokenValue = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        System.out.println("--->Token: " + tokenValue);
        return "Token: " + tokenValue;
    }

    @Autowired
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    // Another way to retrieve access token
    @GetMapping("/displaytoken2")
    public String displayToken(HttpServletRequest httpServletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientRepository.loadAuthorizedClient("github", authentication, httpServletRequest);
        String tokenValue = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        System.out.println("--->Token: " + tokenValue);
        return "Token: " + tokenValue;
    }

    @Autowired
    private WebClient webClient;

    @GetMapping("/userinfo")
    public String profile(Principal principal) throws URISyntaxException {
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient("github", principal.getName());

        String uri = oAuth2AuthorizedClient.getClientRegistration()
                                           .getProviderDetails()
                                           .getUserInfoEndpoint()
                                           .getUri();

		String resourceRetrieved = webClient.get()
                 .uri(new URI(uri))
                 .attributes(clientRegistrationId("github"))
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();

		return  resourceRetrieved;
    }

	@GetMapping("/resource-in-client")
	public String getResourceFromResourceServer() {
		String resourceRetrieved =
				webClient.get()
						 .uri("http://localhost:8001/resource-server/resource-in-server")
						 .attributes(clientRegistrationId("github"))
						 .retrieve()
						 .bodyToMono(String.class)
						 .block();
		return "client retrieved " + resourceRetrieved;
	}

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/resource-in-client2")
	public String getResourceFromResourceServer2() {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8001/resource-server/resource-in-server", String.class);

		return "client retrieved " +responseEntity.getBody();
	}
}



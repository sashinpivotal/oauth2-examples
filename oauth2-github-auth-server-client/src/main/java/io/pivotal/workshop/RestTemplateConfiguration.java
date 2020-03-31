package io.pivotal.workshop;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    // If you want to use RestTemplate (instead of WebClient), you will have to
    // create custom RestTemplate, which sets the Authorization request header
    // with Bearer token.

    @Bean
    RestTemplate restTemplate(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {

        return new RestTemplateBuilder()
                .interceptors((request, body, execution) -> {

                    OAuth2AuthenticationToken oAuth2AuthenticationToken
                            = OAuth2AuthenticationToken.class.cast(SecurityContextHolder.getContext().getAuthentication());
                    OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient("github", oAuth2AuthenticationToken.getName());

                    request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + oAuth2AuthorizedClient.getAccessToken().getTokenValue());

                    return execution.execute(request, body);
                }).build();
    }
}

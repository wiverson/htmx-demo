package com.devhow.identity;


import com.devhow.identity.entity.User;
import com.devhow.identity.user.IdentityServiceException;
import com.devhow.identity.user.UserService;
import jakarta.mail.AuthenticationFailedException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"mail.test=true"})
public class WebSecurityConfigTests {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final String[] publicURLs = new String[]{"/", "/public/sign-in", "/public/forgot-password", "/public/sign-up", "/public/ping"};
    private final String[] loginRequiredURLs = new String[]{"/private/"};
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @LocalServerPort
    private int port;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void basicAccessChecks() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        for (String publicURL : publicURLs) {
            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort(publicURL),
                    HttpMethod.GET, entity, String.class);

            assertThat(response.getStatusCode().value()).isEqualTo(200).describedAs(publicURL);
        }
    }

    @Test
    public void basicLockAccessChecks() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        for (String privateURL : loginRequiredURLs) {
            ResponseEntity<String> response = restTemplate.exchange(
                    createURLWithPort(privateURL),
                    HttpMethod.GET, entity, String.class);

            assertThat(response.getBody()).contains("Sign In").describedAs(privateURL);
        }
    }

    @Test
    public void apiPing() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/public/ping"),
                HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200).describedAs("User Service Ping");
        assertThat(response.getBody()).contains("OK");
    }

    @Test
    public void loginTest() throws AuthenticationFailedException, IdentityServiceException {

        String password = "fancy-new-password";

        User user = UserUtils.setupUser(userService, password);

        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Lists.list(MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML));
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username", user.getUsername());
        parameters.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                createURLWithPort("/public/do-sign-in"), request, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(302);
        assertThat(response.getHeaders().get("Location")).doesNotContain("error=true");
    }

    @Test
    public void badLoginTest() throws AuthenticationFailedException, IdentityServiceException {

        String password = "fancy-new-password";

        User user = UserUtils.setupUser(userService, password);

        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username", user.getUsername());
        parameters.add("password", "garbage");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                createURLWithPort("/public/do-sign-in"), request, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(302);
        assertThat(response.getHeaders().get("Location").get(0)).contains("error=true");
    }
}
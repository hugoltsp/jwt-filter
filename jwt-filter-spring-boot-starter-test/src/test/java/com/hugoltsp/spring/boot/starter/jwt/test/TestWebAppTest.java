package com.hugoltsp.spring.boot.starter.jwt.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestWebAppTest {

    @LocalServerPort
    private int port;

    @Test
    public void non_public_url_should_return_401_when_unauthorized() {

        assertThrows(Unauthorized.class, () -> exchange("/protected"));
    }

    @Test
    public void public_url_should_return_2xx_when_unauthorized() {
        assertThat(exchange("/unprotected").getStatusCode()).isEqualTo(OK);
    }

    private ResponseEntity<String> exchange(String url) {
        return new RestTemplate().exchange(createUrl() + url, GET, null, String.class);
    }

    private String createUrl() {
        return "http://localhost:" + port;
    }
}
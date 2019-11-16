package com.hugoltsp.spring.boot.starter.jwt.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestWebAppTest {

    @LocalServerPort
    private int port;

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void non_public_url_should_return_401_when_unauthorized() {

        expectedException.expect(Unauthorized.class);
        exchange("/protected");
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
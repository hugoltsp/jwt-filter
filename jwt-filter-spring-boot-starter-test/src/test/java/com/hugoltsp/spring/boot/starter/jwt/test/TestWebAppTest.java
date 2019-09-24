package com.hugoltsp.spring.boot.starter.jwt.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestWebAppTest {

    @LocalServerPort
    private int port;

    @Test
    public void test() {

        ResponseEntity<String> exchange = new RestTemplate().exchange(createUrl() + "/unprotected", HttpMethod.GET, null, String.class);

    }

    private String createUrl() {

        return "http://localhost:" + port;
    }
}
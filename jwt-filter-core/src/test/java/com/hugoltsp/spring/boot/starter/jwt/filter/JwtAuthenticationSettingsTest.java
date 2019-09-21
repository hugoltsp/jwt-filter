package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class JwtAuthenticationSettingsTest {

    private static final String TEST_SECRET_KEY = "testingSecretKey";

    @Test
    public void getSecretKey() {
        JwtAuthenticationSettings jwtAuthenticationSettings = new JwtAuthenticationSettings();
        jwtAuthenticationSettings.setSecretKey(TEST_SECRET_KEY);

        assertThat(jwtAuthenticationSettings.getSecretKey()).isNotBlank();
    }

}
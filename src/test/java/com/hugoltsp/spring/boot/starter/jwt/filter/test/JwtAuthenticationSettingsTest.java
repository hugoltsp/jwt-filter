package com.hugoltsp.spring.boot.starter.jwt.filter.test;

import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings;
import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings.PublicResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

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
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

    @Test
    public void isPublic_should_return_FALSE_when_no_PublicResources_are_specified_and_http_method_is_not_OPTIONS() {
        JwtAuthenticationSettings jwtAuthenticationSettings = new JwtAuthenticationSettings();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");

        assertThat(jwtAuthenticationSettings.isPublic(request)).isFalse();
    }

    @Test
    public void isPublic_should_return_TRUE_when_http_method_is_OPTIONS() {
        JwtAuthenticationSettings jwtAuthenticationSettings = new JwtAuthenticationSettings();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(OPTIONS.name());
        request.setRequestURI("/test");

        assertThat(jwtAuthenticationSettings.isPublic(request)).isTrue();
    }

    @Test
    public void isPublic_should_return_TRUE_when_PublicResource_is_specified() {

        PublicResource publicResource = new PublicResource();
        publicResource.setMethod(POST);
        publicResource.setUrls(asList("/test"));

        JwtAuthenticationSettings jwtAuthenticationSettings = new JwtAuthenticationSettings();
        jwtAuthenticationSettings.setPublicResources(asList(publicResource));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(POST.name());
        request.setRequestURI("/test");

        assertThat(jwtAuthenticationSettings.isPublic(request)).isTrue();
    }

    @Test
    public void isPublic_should_return_FALSE_when_PublicResource_is_specified_and_method_differs_from_request() {

        PublicResource publicResource = new PublicResource();
        publicResource.setMethod(POST);
        publicResource.setUrls(asList("/test"));

        JwtAuthenticationSettings jwtAuthenticationSettings = new JwtAuthenticationSettings();
        jwtAuthenticationSettings.setPublicResources(asList(publicResource));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(GET.name());
        request.setRequestURI("/test");

        assertThat(jwtAuthenticationSettings.isPublic(request)).isFalse();
    }

    @Test
    public void isPublic_should_return_FALSE_when_PublicResource_is_specified_and_url_differs_from_request() {

        PublicResource publicResource = new PublicResource();
        publicResource.setMethod(POST);
        publicResource.setUrls(asList("/test"));

        JwtAuthenticationSettings jwtAuthenticationSettings = new JwtAuthenticationSettings();
        jwtAuthenticationSettings.setPublicResources(asList(publicResource));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(POST.name());
        request.setRequestURI("/user");

        assertThat(jwtAuthenticationSettings.isPublic(request)).isFalse();
    }


}
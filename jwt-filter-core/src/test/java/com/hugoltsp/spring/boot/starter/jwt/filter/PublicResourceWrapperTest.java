package com.hugoltsp.spring.boot.starter.jwt.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class PublicResourceWrapperTest {

    @Test
    public void isPublic_should_return_TRUE_when_PublicResource_is_specified() {

        PublicResource publicResource = new PublicResource();
        publicResource.setMethod(POST);
        publicResource.setUrls(asList("/test"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(POST.name());
        request.setRequestURI("/test");

        assertThat(new PublicResourceWrapper(publicResource).isPublic(new HttpRequest(request))).isTrue();
    }

    @Test
    public void isPublic_should_return_FALSE_when_PublicResource_is_specified_and_method_differs_from_request() {

        PublicResource publicResource = new PublicResource();
        publicResource.setMethod(POST);
        publicResource.setUrls(asList("/test"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(GET.name());
        request.setRequestURI("/test");

        assertThat(new PublicResourceWrapper(publicResource).isPublic(new HttpRequest(request))).isFalse();
    }

    @Test
    public void isPublic_should_return_FALSE_when_PublicResource_is_specified_and_url_differs_from_request() {

        PublicResource publicResource = new PublicResource();
        publicResource.setMethod(POST);
        publicResource.setUrls(asList("/test"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(POST.name());
        request.setRequestURI("/user");

        assertThat(new PublicResourceWrapper(publicResource).isPublic(new HttpRequest(request))).isFalse();
    }

}

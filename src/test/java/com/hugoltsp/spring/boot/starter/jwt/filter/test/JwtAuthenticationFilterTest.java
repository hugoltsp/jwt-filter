package com.hugoltsp.spring.boot.starter.jwt.filter.test;

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationFilter;
import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.setting.JwtAuthenticationSettings;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {

	@Mock
	private JwtAuthenticationSettings settings;

	@Mock
	private UserDetailsValidator userDetailsValidator;

	@Mock
	private UserDetailsFactory userDetailsFactory;

	@Mock
	private AuthenticationContextFactory authenticationContextFactory;

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Test
	public void should_return_200_when_request_is_deemed_public() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();

		when(settings.isPublic(request)).thenReturn(true);

		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain();

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		assertThat(response.getStatus()).isEqualTo(SC_OK);
	}

	@Test
	public void should_return_200_when_request_is_protected_and_user_is_authenticated()
			throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod(GET.name());
		request.setRequestURI("/test");
		request.addHeader(AUTHORIZATION,
				"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWIiLCJuYW1lIjoiSm9obiBEb2UifQ.jKexo6MlFW78w31biGfZGqaf3LRY3KZKMuXJFtkCJ6k");

		when(settings.isPublic(request)).thenReturn(false);
		when(settings.getSecretKey()).thenReturn("some-secret-key");
		when(userDetailsFactory.createByClaims(any())).thenReturn(Optional.empty());

		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain();

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		assertThat(response.getStatus()).isEqualTo(SC_OK);
	}

	@Test
	public void should_return_401_when_request_is_protected() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();

		when(settings.isPublic(request)).thenReturn(false);

		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain();

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		assertThat(response.getStatus()).isEqualTo(SC_UNAUTHORIZED);
	}

}

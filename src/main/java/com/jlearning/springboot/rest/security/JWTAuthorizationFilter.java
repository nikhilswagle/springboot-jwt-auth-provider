package com.jlearning.springboot.rest.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.jlearning.springboot.rest.security.util.SecurityConstants;

/**
 * Validate the token sent in the header of the requests.
 * @file JWTAuthorizationFilter.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 11:40:25 AM
 */
public class JWTAuthorizationFilter extends
		AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	
	public JWTAuthorizationFilter(RequestMatcher requestMatcher) {
		super(requestMatcher);
	}

	/**
	 * Invokes autheticate() method on the configured AuthenticationManager using authentication token
	 * Throws AuthenticationException if token validation fails
	 * Return valid Authentication object if token validation is successful
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		String token = request.getHeader(SecurityConstants.REQUEST_HEADER_PARAM_JWT);
		String merchant = request.getParameter("merchant");				
		logger.info("Validating token = "+token+" for merchant : "+merchant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(merchant, token, Collections.emptyList());		
		return this.getAuthenticationManager().authenticate(authentication);
	}
	
	/**
	 * This method is invoked if a valid Authentication object was returned by attemptAuthentication() method above.
	 * Sets the Authentication object to SecurityContext
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		logger.info("Successfully Authorized");
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}
	
	/**
	 * This method is invoked if a AuthenticationException was thrown by attemptAuthentication() method above.
	 * Clears SecurityContext and calls superclass implementation to return 401:Unauthorized Access response.
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		logger.info("Authorization Failed");
		SecurityContextHolder.clearContext();
		super.unsuccessfulAuthentication(request, response, failed);
	}

}

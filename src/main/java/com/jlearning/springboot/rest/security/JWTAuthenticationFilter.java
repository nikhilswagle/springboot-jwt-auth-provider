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
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.jlearning.springboot.rest.security.util.JWTUtil;
import com.jlearning.springboot.rest.security.util.SecurityConstants;

/**
 * Authenticates user and sends back a JWT token upon successful authentication.
 * @file JWTAuthenticationFilter.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 11:48:56 AM
 */
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
	// JWT Utility to generate token after successful authentication
	private JWTUtil jwt;

	public JWTAuthenticationFilter(RequestMatcher requestMatcher) {
		super(requestMatcher);
	}
	
	public JWTUtil getJwt() {
		return jwt;
	}

	public void setJwt(JWTUtil jwt) {
		this.jwt = jwt;
	}

	/**
	 * Reads accessId and secret sent in the request by the user. Authenticates user based on those credentials via configured AuthenticationManager.
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		String accessId = request.getHeader("accessId");
		String secret = request.getHeader("secret");
		logger.info("Authenticating for accessId = "+accessId+" and secret="+secret);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(accessId, secret, Collections.emptyList());		
		return this.getAuthenticationManager().authenticate(authentication);
	}
	
	/**
	 * This method is invoked if a valid Authentication object was returned by attemptAuthentication() method above.
	 * Builds JWT token and returns it in the response.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String accessId = request.getHeader("accessId");
		String token = jwt.buildJWT(accessId);
		response.addHeader(SecurityConstants.RESPONSE_HEADER_PARAM_JWT, token);
		logger.debug(token);
		chain.doFilter(request, response);
	}
}

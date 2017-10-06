package com.jlearning.springboot.rest.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.jlearning.springboot.rest.security.util.JWTUtil;

/**
 * Validates the token.  
 * @file JwtTokenAuthenticationManager.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 11:38:09 AM
 */
@Component(value="tokenAuthenticationManager")
public class JwtTokenAuthenticationManager implements AuthenticationManager {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationManager.class);
	
	@Autowired
	private JWTUtil jwtUtil;
	
	/**
	 * Throws AuthenticationException if token validation fails
	 * Return valid Authentication object if token validation is successful
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String token = authentication.getCredentials().toString();
		if(StringUtils.isEmpty(token)){
			logger.warn("Token is missing");
			throw new AuthenticationCredentialsNotFoundException("Token is missing");
		}
		boolean isValidToken = jwtUtil.validate(token);
		if(!isValidToken){
			logger.info("Token is Invalid");
			throw new BadCredentialsException("Token sent in the request is invalid");
		}
		return authentication;
	}
}

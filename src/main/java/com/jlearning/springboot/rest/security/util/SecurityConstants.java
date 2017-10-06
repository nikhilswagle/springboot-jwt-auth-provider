package com.jlearning.springboot.rest.security.util;

public interface SecurityConstants {

	public static final String RESPONSE_HEADER_PARAM_JWT = "jwt-token";
	
	public static final String REQUEST_HEADER_PARAM_JWT = "Authorization";
	
	public static final String AUTHENTICATION_PATH = "/rest/authenticate";
	
	public static final String AUTHORIZATION_PATH = "/rest/api/*";
	
	public static final String HAZELCAST_MAP_USER_SIGNING_KEYS = "USER_SIGNING_KEYS";
	
	public static final String HAZELCAST_MAP_USER_AUTH_CREDENTIALS = "USER_AUTH_CREDENTIALS";
	
	public static final String HAZELCAST_MAP_USER_JWT_DATA = "USER_JWT_DATA";
}

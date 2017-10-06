package com.jlearning.springboot.rest.service;

import java.security.Key;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jlearning.springboot.rest.security.model.JWT;
import com.jlearning.springboot.rest.security.model.JWTHeader;
import com.jlearning.springboot.rest.security.model.JWTPayload;
import com.jlearning.springboot.rest.security.model.UserCredentials;
import com.jlearning.springboot.rest.security.util.SecurityConstants;
import com.jlearning.springboot.rest.security.util.SecurityServiceUtil;

/**
 * Service to load user configuration from property file to hazelcast cluster 
 * @file PropertyFileLoadService.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 12:25:24 PM
 */
@Service("userConfigPropertyFileLoader")
@PropertySource("classpath:/app-config.properties")
public class PropertyFileLoadService implements UserConfigLoaderService {

	private static final Logger logger = LoggerFactory.getLogger(PropertyFileLoadService.class);
	
	@Autowired
	private HazelcastInstance hazelcast;
	
	@Autowired
	private SecurityServiceUtil securityUtil;
	
	@Autowired
	Environment env;
	
	@Override
	public void loadUserConfigs() {
		String[] userArr = env.getProperty("user.list").split(",");
		
		IMap<String, UserCredentials> userAuthCredentials = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_AUTH_CREDENTIALS);
		IMap<String, JWT> userJwtData = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_JWT_DATA);
		IMap<String, Key> userSigningKeys = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_SIGNING_KEYS);
		
		for(String user : userArr){
			// Load auth details
			loadUserAuthCredentials(userAuthCredentials, user);
			logger.info("Loaded user credentials for : "+user);
			
			// Load Jwt properties
			loadJwtData(userJwtData, user);
			logger.info("Loaded user specific JWT data for : "+user);
			
			// Load Signing keys
			loadSigningKeysForUsers(userSigningKeys, user);
			logger.info("Loaded signing keys for : "+user);
		}
	}
	
	private void loadUserAuthCredentials(IMap<String, UserCredentials> userAuthCredentials, String user) {		
		String accessId = env.getProperty("user.accessId."+normalize(user));
		String secret = env.getProperty("user.secret."+normalize(user));
		String userId = env.getProperty("user.propertyLookupId."+normalize(accessId));
		UserCredentials auth = new UserCredentials(accessId, secret, userId);
		userAuthCredentials.put(accessId, auth);
	}
	
	private void loadJwtData(IMap<String, JWT> userJwtData, String user){
		JWTHeader header = new JWTHeader(env.getProperty("jwt.header.type."+normalize(user)));
		JWTPayload payload = new JWTPayload();
		payload.setUserId(env.getProperty("jwt.payload.userId."+normalize(user)));
		payload.setSubject(env.getProperty("jwt.payload.subject."+normalize(user)));
		payload.setIssuer(env.getProperty("jwt.payload.issuer."+normalize(user)));
		payload.setAudience(env.getProperty("jwt.payload.audience."+normalize(user)));
		payload.setExpiryInSeconds(Integer.valueOf(env.getProperty("jwt.payload.expiryInSeconds."+normalize(user))));
		JWT request = new JWT(header, payload);			
		userJwtData.put(user, request);
	}
	
	private void loadSigningKeysForUsers(IMap<String, Key> merchSigningKeys, String user) {		
		merchSigningKeys.put(user, securityUtil.generateKey());
	}
	
	private String normalize(String str){
		return StringUtils.deleteWhitespace(str)
				   .toLowerCase();
	}

}

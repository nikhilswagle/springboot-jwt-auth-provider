package com.jlearning.springboot.rest.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jlearning.springboot.rest.security.model.UserCredentials;
import com.jlearning.springboot.rest.security.util.SecurityConstants;

/**
 * Retrieves user details from Hazelcast cluster.
 * @file UserDetailsServiceImpl.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 11:54:30 AM
 */
@Service(value="userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private HazelcastInstance hazelcast;
	
	@Override
	public UserDetails loadUserByUsername(String accessId)
			throws UsernameNotFoundException {
		IMap<String, UserCredentials> credentialMap = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_AUTH_CREDENTIALS);
		UserCredentials inMemoryCredentials = credentialMap.get(accessId);
		
		User user = null;
		if(null != inMemoryCredentials){
			logger.info("User Found in memory : "+inMemoryCredentials.getAccessId());
			user = new User(inMemoryCredentials.getAccessId(), inMemoryCredentials.getSecret(), Collections.emptyList());
		}
		return user;
	}
}

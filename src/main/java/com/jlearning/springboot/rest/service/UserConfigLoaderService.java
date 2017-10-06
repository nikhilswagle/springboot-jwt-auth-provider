package com.jlearning.springboot.rest.service;

import org.springframework.stereotype.Service;

/**
 * Service to load user configuration to hazelcast cluster
 * @file UserConfigLoaderService.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 12:24:43 PM
 */
@Service
public interface UserConfigLoaderService {

	public void loadUserConfigs();
}

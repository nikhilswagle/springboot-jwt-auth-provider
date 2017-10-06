package com.jlearning.springboot.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.jlearning.springboot.rest.service.UserConfigLoaderService;

@Component
public class ApplicationStartup implements
		ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Autowired
	@Qualifier("userConfigPropertyFileLoader")
	private UserConfigLoaderService configLoaderService;
		
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.info("Loding user configs");
		configLoaderService.loadUserConfigs();
	}	
}

package com.jlearning.springboot.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/rest")
public class SpringbootRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringbootRestController.class);
	
	@RequestMapping(method=RequestMethod.GET, path="/authenticate", produces=MediaType.TEXT_PLAIN_VALUE)
	public String authenticate(@RequestParam(name="accessId", required=true) String accessId, @RequestParam(name="secret", required=true) String secret){
		String response = "Successful authentication for accessId = "+accessId+" and secret="+secret;
		logger.info(response);
		return response;
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/api/get-user", produces=MediaType.TEXT_PLAIN_VALUE)
	public String getUserDetails(){
		String response = "Invoked getUserDetails()";
		logger.info(response);
		return response;
	}
}

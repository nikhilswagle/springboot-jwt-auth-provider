package com.jlearning.springboot.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/rest")
public class SpringbootRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringbootRestController.class);
	
	@RequestMapping(method=RequestMethod.GET, path="/authenticate", produces=MediaType.TEXT_PLAIN_VALUE)
	public String authenticate(HttpServletRequest request, HttpServletResponse response){
		String responseTxt = "Successful authentication for accessId = "+request.getHeader("accessId")+" and secret = "+request.getHeader("secret");
		logger.info(responseTxt);
		return responseTxt;
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/api/get-user", produces=MediaType.TEXT_PLAIN_VALUE)
	public String getUserDetails(){
		String response = "Invoked getUserDetails()";
		logger.info(response);
		return response;
	}
}

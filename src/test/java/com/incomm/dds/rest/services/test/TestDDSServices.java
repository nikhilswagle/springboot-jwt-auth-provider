package com.incomm.dds.rest.services.test;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jlearning.springboot.rest.security.model.JWT;
import com.jlearning.springboot.rest.security.model.JWTHeader;
import com.jlearning.springboot.rest.security.model.JWTPayload;
import com.jlearning.springboot.rest.security.util.JWTUtil;
import com.jlearning.springboot.rest.security.util.SecurityServiceUtil;

/**
 * 
 * @file TestDDSServices.java
 * @author nikhilswagle
 * @date Sep 29, 2017
 * @time 3:33:05 PM
 */
public class TestDDSServices {

	private static SecurityServiceUtil securityUtil;
	private static JWTUtil jwtUtil;
	private static SecretKey signingKey;
	private static String jwt;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		securityUtil = new SecurityServiceUtil();
		jwtUtil = new JWTUtil();
		signingKey = (SecretKey) securityUtil.generateKey(); 
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateKey() {
		SecretKey key = (SecretKey) securityUtil.generateKey();
		System.out.println("New key generated:"+key.getAlgorithm());
	}
	
	@Test
	public void testCreateJwt(){
		JWTHeader header = new JWTHeader("JWT");
		JWTPayload payload = new JWTPayload();
		payload.setAudience("EVERYONE");
		payload.setExpiryInSeconds(300);
		long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
		payload.setIssuedAt(now);
		payload.setIssuer("JWT AUTH PROVIDER (http://damodar-jlearning.blogspot.com)");
		payload.setSubject("JWT AUTHENTICATION");
		payload.setTokenId(String.valueOf(UUID.randomUUID()));
		JWT request = new JWT(header, payload);
		jwt = jwtUtil.createJWT(request, signingKey);
		System.out.println("JWT generated:"+jwt);
	}
	
	@Test
	public void testValidateJwt() {
		jwtUtil.validate(jwt);
	}
}

package com.jlearning.springboot.rest.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jlearning.springboot.rest.security.model.JWT;
import com.jlearning.springboot.rest.security.model.JWTPayload;
import com.jlearning.springboot.rest.security.model.UserCredentials;

@Component
public class JWTUtil {

	private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
	
	@Autowired
	private HazelcastInstance hazelcast;
	
	public String buildJWT(String accessId){
		logger.info("Retrieve authentication details for accessId : "+accessId);
		IMap<String, UserCredentials> credentialMap = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_AUTH_CREDENTIALS);
		UserCredentials inMemoryCredentials = credentialMap.get(accessId);
		
		String userId = inMemoryCredentials.getUserId();
		
		logger.info("Retrieve JWT data for user : "+userId);
		IMap<String, JWT> jwtMap = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_JWT_DATA);
		JWT jwtData = jwtMap.get(userId);
		
		// Set Issued At to current time.
		long nowMillis = System.currentTimeMillis();
	    jwtData.getPayload().setIssuedAt(new Date(nowMillis));
	    
	    // Set Expiration Date
	    if (jwtData.getPayload().getExpiryInSeconds() >= 0) {
	    	long expMillis = jwtData.getPayload().getIssuedAt().getTime() + (jwtData.getPayload().getExpiryInSeconds() * 1000);
	        Date exp = new Date(expMillis);
	        jwtData.getPayload().setExpirationDate(exp);
	    }
	    
	    // Set Unique Token Id
	    jwtData.getPayload().setTokenId(String.valueOf(UUID.randomUUID()));
		
		logger.info("Retrieve signing key for merchant : "+userId);
		IMap<String, Key> signingKeyMap = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_SIGNING_KEYS);
		Key signingKey = signingKeyMap.get(userId);
		
		return createJWT(jwtData, signingKey);
	}
	
	public String createJWT(JWT jwtData, Key signingKey){
		//The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;	    

	    System.out.println(jwtData.getPayload().toString());
	    JwtBuilder builder = Jwts.builder().setHeaderParam("type", jwtData.getHeader().getType())
						                .setPayload(jwtData.getPayload().toString())
						                .signWith(signatureAlgorithm, signingKey);
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	
	public boolean validate(String jwt){
		//This line will throw an exception if it is not a signed JWS (as expected)
		try{
			String[] jwtComponents = jwt.split("\\.");
			System.out.println(jwtComponents.length);
			String payloadBase64Url = jwtComponents[1];
			String payloadBase64 = new String(Base64.getUrlDecoder().decode(payloadBase64Url.getBytes("UTF8")));
			ObjectMapper mapper = new ObjectMapper();
			JWTPayload payload = mapper.readValue(payloadBase64, JWTPayload.class);
			
			logger.info("Retrieve signing key for user : "+payload.getUserId());
			IMap<String, Key> signingKeyMap = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_SIGNING_KEYS);
			Key signingKey = signingKeyMap.get(payload.getUserId());
			
			Claims claims = null;
			claims = Jwts.parser()
						 .setSigningKey(signingKey)
						 .parseClaimsJws(jwt).getBody();
			
			logger.info("Retrieve JWT data for user : "+payload.getUserId());
			IMap<String, JWT> jwtMap = hazelcast.getMap(SecurityConstants.HAZELCAST_MAP_USER_JWT_DATA);
			JWT request = jwtMap.get(payload.getUserId());
			
			logger.debug("Token ID: " + claims.getId());
			
		    if (null != claims) {		    	
			    if(request.getPayload().getSubject().equals(claims.getSubject()) &&
			    		request.getPayload().getIssuer().equals(claims.getIssuer()) &&
			    		request.getPayload().getAudience().equals(claims.getAudience())){
			    	return true;
			    }
		    }		
		}
		catch(Exception e){
			logger.error("Error occured while validating JWT", e);
		}
	    return false;
	}
}

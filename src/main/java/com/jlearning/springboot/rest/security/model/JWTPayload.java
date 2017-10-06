package com.jlearning.springboot.rest.security.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTPayload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7087131610819290923L;

	//iss: The issuer of the token
	@JsonProperty("iss")
	private String issuer;

	//sub: The subject of the token
	@JsonProperty("sub")
	private String subject;
	
	//aud: The audience of the token
	@JsonProperty("aud")
	private String audience;
	
	private Integer expiryInSeconds;
		
	//nbf: Defines the time before which the JWT MUST NOT be accepted for processing
	@JsonProperty(value="nbf", required=false)
	private String nbf;
	
	//iat: The time the JWT was issued. Can be used to determine the age of the JWT
	@JsonProperty("iat")
	private Date issuedAt;
	
	//exp: This will probably be the registered claim most often used. This will define the expiration in NumericDate value. The expiration MUST be after the current date/time.
	@JsonProperty("exp")
	private Date expirationDate;
	
	//jti: Unique identifier for the JWT. Can be used to prevent the JWT from being replayed. This is helpful for a one time use token.
	@JsonProperty("jti")
	private String tokenId;
	
	// Custom parameter for merchant id
	@JsonProperty("userId")
	private String userId;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Integer getExpiryInSeconds() {
		return expiryInSeconds;
	}

	public void setExpiryInSeconds(Integer expiryInSeconds) {
		this.expiryInSeconds = expiryInSeconds;
	}

	public String getNbf() {
		return nbf;
	}

	public void setNbf(String nbf) {
		this.nbf = nbf;
	}

	public Date getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString(){
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}

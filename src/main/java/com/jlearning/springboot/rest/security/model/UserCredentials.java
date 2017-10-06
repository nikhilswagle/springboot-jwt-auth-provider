package com.jlearning.springboot.rest.security.model;

import java.io.Serializable;

public class UserCredentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1061526609326752130L;

	public UserCredentials(String accessId, String secret, String userId){
		this.accessId = accessId;
		this.secret = secret;
		this.userId = userId;
	}
	
	private String accessId;
	
	private String secret;
	
	private String userId;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

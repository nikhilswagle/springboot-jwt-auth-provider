package com.jlearning.springboot.rest.security.model;

import java.io.Serializable;
import java.security.Key;

public class JWT implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1614836800401112614L;

	public JWT(){}
	
	public JWT(JWTHeader header, JWTPayload payload){
		this.header = header;
		this.payload = payload;
	}
	
	private JWTHeader header;
	
	private JWTPayload payload;
	
	private Key signingKey;

	public JWTHeader getHeader() {
		return header;
	}

	public void setHeader(JWTHeader header) {
		this.header = header;
	}

	public JWTPayload getPayload() {
		return payload;
	}

	public void setPayload(JWTPayload payload) {
		this.payload = payload;
	}

	public Key getSigningKey() {
		return signingKey;
	}

	public void setSigningKey(Key signingKey) {
		this.signingKey = signingKey;
	}
}

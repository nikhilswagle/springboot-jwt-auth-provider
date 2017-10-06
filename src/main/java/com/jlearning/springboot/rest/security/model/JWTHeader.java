package com.jlearning.springboot.rest.security.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTHeader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1379060276388783081L;

	public JWTHeader(){}
	
	public JWTHeader(String type) {
		this.type = type;
	}
	
	@JsonProperty("typ")
	private String type;
	
	// This will be auto populated by JWT API
	@JsonProperty(value="alg", required=false)
	private String algorithm;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
}

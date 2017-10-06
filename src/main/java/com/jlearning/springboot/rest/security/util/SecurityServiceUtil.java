package com.jlearning.springboot.rest.security.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class to generate secret key for signing JWT
 * @file SecurityServiceUtil.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 12:27:44 PM
 */
@Component
public class SecurityServiceUtil {
	
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(SecurityServiceUtil.class);
	/**
     * Default encryption algorithm to use if none is provided
     */
	public static final String ENCRYPTION_ALGORITHM = "HmacSHA256";
    
	/**
     * Method used to generate key for the keystore
     * @return
     */
    public Key generateKey(){
    	SecretKey key = null;
    	try {
            if(key == null){
	            KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
	            //kgen.init(128);
	            kgen.init(new SecureRandom());
	            key = kgen.generateKey();
	            if(key != null){
	            	logger.debug("New key generated:"+key.toString());
	            }	            	
            }
        } catch (final NoSuchAlgorithmException nsae){
            logger.error(ENCRYPTION_ALGORITHM+" algorithm not configured in JDK. Exception Message: "+nsae.getMessage());
        }
    	return key;
    }
}

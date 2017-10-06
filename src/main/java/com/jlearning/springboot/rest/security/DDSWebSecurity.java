package com.jlearning.springboot.rest.security;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jlearning.springboot.rest.security.util.JWTUtil;
import com.jlearning.springboot.rest.security.util.SecurityConstants;

/**
 * Security configuration class.
 * @file DDSWebSecurity.java
 * @author nikhilswagle
 * @date Oct 6, 2017
 * @time 11:21:39 AM
 */
@Configuration
@EnableWebSecurity(debug=true)
public class DDSWebSecurity extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(DDSWebSecurity.class);
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	@Qualifier("tokenAuthenticationManager")
	private AuthenticationManager tokenAuthenticationManager;
	
	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.GET, SecurityConstants.AUTHENTICATION_PATH).permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		logger.info("Security configuration completed");
	}

	/**
	 * This method is overriden because Authentication Filter below uses default AuthenticationManager by invoking authenticationManager() method of the superclass.
	 * Default AuthenticationManager uses AuthenticationManagerBuilder to inject user details required for authentication.
	 * So we need this override to configure AuthenticationManagerBuilder to use appropriate (in our case in-memory) UserDetailsService.
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",
				new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	/**
	 * Instantiate Authentication Filter to authenticate user and send back a JWT token upon successful authentication.
	 * We are initializing default AuthenticationManager by invoking authenticationManager() method of the superclass.
	 * This filter will be invoked upon receiving a request with "/rest/autheticate" path.
	 * @return
	 * @throws Exception
	 */
	private Filter authenticationFilter() throws Exception {
		JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(new AntPathRequestMatcher(SecurityConstants.AUTHENTICATION_PATH, HttpMethod.GET.toString()));
		authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setJwt(jwtUtil);
		return authenticationFilter;
	}
	
	/**
	 * Instantiate Authorization Filter to validate the token send in the header of the subsequent requests. Upon successful authorization the request will be forwarded to
	 * appropriate endpoint.
	 * We are initializing a custom AuthenticationManager that will validate the token.
	 * This filter will be invoked upon receiving a request with "/rest/api/*" path.
	 * @return
	 * @throws Exception
	 */
	private Filter authorizationFilter() throws Exception {
		JWTAuthorizationFilter authorizationFilter = new JWTAuthorizationFilter(new AntPathRequestMatcher(SecurityConstants.AUTHORIZATION_PATH, HttpMethod.GET.toString()));
		authorizationFilter.setAuthenticationManager(tokenAuthenticationManager);
		return authorizationFilter;
	}
}

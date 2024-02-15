package com.modris.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	  
	 @Bean
	    SecurityFilterChain
	            filterChain(HttpSecurity http)
	                    throws Exception {

	        http.oauth2ResourceServer(resourceServer -> resourceServer.jwt());

	        // State-less session (state in access token only)
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			// Disable CSRF because of state-less session-management
			http.csrf().disable();

			// Return 401 (unauthorized) instead of 302 (redirect to login) when
			// authorization is missing or invalid
			http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
				response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Restricted Content\"");
				response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
			});


	        http.authorizeHttpRequests()
	            .anyRequest().permitAll();


			return http.build();
	    }

	}
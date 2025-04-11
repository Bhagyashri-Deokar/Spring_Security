package com.example.registration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;


	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("admin").password("{noop}admin123") // no extra parentheses
				.roles("ADMIN").build());

		manager.createUser(User.withUsername("user").password("{noop}user123")
				.roles("USER")
				.build());

		return manager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) 
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/users/lookup").authenticated().anyRequest().permitAll())
				.httpBasic(Customizer.withDefaults()).exceptionHandling(ex -> 
					ex
					  .authenticationEntryPoint(authenticationEntryPoint));
		return http.build();
	}
	


}

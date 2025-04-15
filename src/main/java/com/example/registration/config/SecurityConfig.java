package com.example.registration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.registration.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private PasswordEncoder passwordEncoder; // Autowired here

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

//	@Bean
//	public UserDetailsService userDetailsService()
//	{
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//		manager.createUser(User.withUsername("admin").password(passwordEncoder.encode("admin123")) // no extra parentheses
//				.roles("ADMIN").build());
//
//		return manager;
//	} 
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) 
	        .authorizeHttpRequests(auth -> auth
	        		.requestMatchers("/api/users/register/admin").permitAll()
	        		.anyRequest().authenticated())
	        .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authenticationEntryPoint)); // 👈 Plug it here

	    return http.build();
	}


	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder).and().build();
	}

}

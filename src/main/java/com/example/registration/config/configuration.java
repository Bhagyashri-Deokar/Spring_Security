package com.example.registration.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class configuration 
{
	 @Bean
	    public ModelMapper modelMapper() {
	        return new ModelMapper();
	    }
	 
//	 @Bean
//	    public BCryptPasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }
}

package com.example.registration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.registration.config.CustomUserDetails;
import com.example.registration.entity.User;
import com.example.registration.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		logger.info("Attempting to fetch user by memberId: {}", memberId);

		// Fetch the user by memberId from the database
		User user = userRepository.findByMemberId(memberId).orElseThrow(() -> {
			logger.error("User not found with member ID: {}", memberId);
			return new UsernameNotFoundException("User not found with member ID: " + memberId);
		});

		logger.info("User found with memberId: {}", user.getMemberId());

//        return new org.springframework.security.core.userdetails.User(
//                user.getMemberId(),
//                user.getPassword(), // Spring Security automatically checks the encoded password
//                getAuthorities(user)
//        );
		UserDetails user1 = new CustomUserDetails(user);
		return user1;

	}

}

package com.example.registration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.registration.config.CustomUserDetails;
import com.example.registration.dto.ErrorResponse;
import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.entity.User;
import com.example.registration.exception.UserNotFoundException;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Autowired
	private final UserRepository userRepository;

	// For first user registration only
	@PostMapping("/register/admin")
	public ResponseEntity<Object> registerAdmin(@RequestBody @Valid UserRequestDTO dto) {
		if (userRepository.count() == 0) {
			UserResponseDTO registeredUser = userService.registerUser(dto);
			return ResponseEntity.ok(registeredUser); // Return the response after successful registration
		} else {
			ErrorResponse errorResponse = new ErrorResponse(401, "Unauthorized",
					"Admin registration is allowed only for the first user. For registering a normal user, please use the '/register' API call.");
			return ResponseEntity.status(401).body(errorResponse);
		}
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO dto,
			Authentication authentication) {
		return ResponseEntity.ok(userService.registerUser(dto));
	}

	@GetMapping("/lookup")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
	public ResponseEntity<UserResponseDTO> findUser(@RequestParam(required = false) String memberId,
			@RequestParam(required = false) String email, Authentication authentication) {
		System.out.println("Principal ID: " + ((CustomUserDetails) authentication.getPrincipal()).getId());

		
		if (memberId != null) {
			return userService.getUserByMemberId(memberId).map(ResponseEntity::ok)
					.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + memberId));
		}

		if (email != null) {
			return userService.getUserByEmail(email).map(ResponseEntity::ok)
					.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
		}

		throw new IllegalArgumentException("Please provide either 'id' or 'email' to look up a user.");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	   @PutMapping("/update")
	    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
	    public ResponseEntity<UserResponseDTO> updateUserDetails(
	             @RequestParam String memberId, 
	            @RequestBody @Valid UserRequestDTO dto,Authentication authentication) 
	   {
	        UserResponseDTO updatedUser = userService.updateUser(dto, memberId);
	        return ResponseEntity.ok(updatedUser);
	    }
	   
}

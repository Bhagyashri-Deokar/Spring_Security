package com.example.registration.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.exception.UserNotFoundException;
import com.example.registration.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
 
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO userRequestDTO)
    {
        return ResponseEntity.ok(userService.registerUser(userRequestDTO));
    }
    
    @GetMapping("/lookup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> findUser(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email) {

        if (id != null) {
            return userService.getUserByMemberId(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        }

        if (email != null) {
            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        }

        throw new IllegalArgumentException("Please provide either 'id' or 'email' to look up a user.");
    }


    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() 
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}

package com.example.registration.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.entity.User;

import jakarta.validation.Valid;

public interface UserService {
    Optional<UserResponseDTO> getUserByMemberId(String memberId);
    List<UserResponseDTO> getAllUsers();
	Optional<UserResponseDTO> getUserByEmail(String email);
	UserResponseDTO updateUser(@Valid UserRequestDTO dto, String memberId);
	UserResponseDTO registerUser(UserRequestDTO requestDTO);
 
}

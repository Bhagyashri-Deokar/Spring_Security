package com.example.registration.service;

import java.util.List;
import java.util.Optional;

import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
    Optional<UserResponseDTO> getUserByMemberId(Long memberId);
    List<UserResponseDTO> getAllUsers();
	Optional<UserResponseDTO> getUserByEmail(String email);
 
}

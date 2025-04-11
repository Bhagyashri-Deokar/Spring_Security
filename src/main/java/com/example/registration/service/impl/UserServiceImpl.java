package com.example.registration.service.impl;

import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.entity.User;
import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService { 

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
//    private final BCryptPasswordEncoder passwordEncoder; 

    @Override 
    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered: " + requestDTO.getEmail());
        }
 
        User user = modelMapper.map(requestDTO, User.class);
        user.setPassword((requestDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public Optional<UserResponseDTO> getUserByMemberId(Long id) {
        return userRepository.findByMemberId(id)
                .map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .toList();
    }

	@Override
	public Optional<UserResponseDTO> getUserByEmail(String email) {
	    return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserResponseDTO.class));
    
	} 

}

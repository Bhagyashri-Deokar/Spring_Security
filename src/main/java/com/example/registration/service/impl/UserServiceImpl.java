package com.example.registration.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.entity.User;
import com.example.registration.entity.User.Role;
import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserResponseDTO registerUser(UserRequestDTO requestDTO) 
	{
		if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) 
		{
			throw new UserAlreadyExistsException("Email already registered: " + requestDTO.getEmail());
		}

		System.out.println("User Email="+requestDTO.getEmail()+"User role ="+requestDTO.getRole());
		User user = modelMapper.map(requestDTO, User.class);
		user.generateMemberId();
		user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
				if (userRepository.count() == 0) {
			user.setRoles(Role.SUPER_ADMIN);
			user.setMoney(requestDTO.getMoney());
		} else if (!Objects.isNull(requestDTO.getRole()) && Arrays.asList("ADMIN", "admin").contains(requestDTO.getRole())) {
			user.setRoles(Role.ADMIN);
		} else {
			user.setRoles(Role.USER);

		}
		User savedUser = userRepository.save(user);
		return modelMapper.map(savedUser,UserResponseDTO.class);
		
	}

	

	@Override
    public Optional<UserResponseDTO> getUserByMemberId(String memberId) {
        return userRepository.findByMemberId(memberId)
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
		return userRepository.findByEmail(email).map(user -> modelMapper.map(user, UserResponseDTO.class));

	}



	@Override
	public UserResponseDTO updateUser(@Valid UserRequestDTO dto, String memberId) 
	{
		User user = userRepository.findByMemberId(memberId).orElseThrow(() -> {
			return new UsernameNotFoundException("User not found with member ID: " + memberId);
		});
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRoles(dto.getRole());
        User saveUser=userRepository.save(user);
        UserResponseDTO userResponse = new UserResponseDTO();
        BeanUtils.copyProperties(saveUser, userResponse);
        return userResponse;
    	
	}
}

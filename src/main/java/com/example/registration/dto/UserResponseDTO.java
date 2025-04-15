package com.example.registration.dto;

import com.example.registration.entity.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String memberId;
    private String name;
    private String email;
    private Role roles;
}
 
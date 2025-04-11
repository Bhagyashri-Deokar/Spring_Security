package com.example.registration.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long memberId;
    private String name;
    private String email;
}
 
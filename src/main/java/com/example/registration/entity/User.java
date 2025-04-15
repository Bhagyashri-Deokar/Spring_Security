package com.example.registration.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String memberId;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role roles;

    @Column(precision = 10, scale = 2)
    private BigDecimal money = BigDecimal.ZERO;

    
    public enum Role {
        USER, ADMIN, SUPER_ADMIN
    }

    // Utility method to generate a 10-digit memberId (can be called in service layer before saving)
    public void generateMemberId() {
        if (this.memberId == null) {
            this.memberId = generateUniqueMemberId();
        }
    }

    private String generateUniqueMemberId() {
        // Generates a 10-digit random number (as a String)
        return String.format("%010d", (long) (Math.random() * 1_000_000_0000L));
    }
}

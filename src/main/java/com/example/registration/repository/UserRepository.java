package com.example.registration.repository;

import com.example.registration.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> 
{
	boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
	Optional<User> findByMemberId(String memberId);
}

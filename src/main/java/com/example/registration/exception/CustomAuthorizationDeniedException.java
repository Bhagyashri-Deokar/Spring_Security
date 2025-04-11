package com.example.registration.exception;

@SuppressWarnings("serial")
public class CustomAuthorizationDeniedException extends RuntimeException {

	public CustomAuthorizationDeniedException(String message) {
        super(message);
    }}

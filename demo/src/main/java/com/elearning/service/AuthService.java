package com.elearning.service;

import com.elearning.DTO.*;
import com.elearning.entity.*;
import com.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service

public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<UserResponseDTO> login(LoginRequestDTO loginRequest) {
        try {
            // Decode base64 if needed (as mentioned in the document)
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            // Find user by username or email
            Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);

            if (userOpt.isEmpty()) {
                return ApiResponse.error("Invalid username or password", null);
            }

            User user = userOpt.get();

            // Simple password check (in production, use BCrypt)
            if (!user.getPassword().equals(password)) {
                return ApiResponse.error("Invalid username or password", null);
            }

            UserResponseDTO userResponse = new UserResponseDTO(
                    user.getId(), user.getUsername(), user.getEmail()
            );

            return ApiResponse.success("Login successful", userResponse);

        } catch (Exception e) {
            return ApiResponse.error("Login failed: " + e.getMessage(), null);
        }
    }
    @Transactional
    public ApiResponse<String> signup(SignupRequestDTO signupRequest) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                return ApiResponse.error("Username already exists", null);
            }

            // Check if email already exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ApiResponse.error("Email already exists", null);
            }

            // Create new user
            User user = new User(
                    signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword() // In production, use BCrypt to hash
            );

            userRepository.save(user);

            return ApiResponse.success("User registered successfully. Please login.");

        } catch (Exception e) {
            return ApiResponse.error("Registration failed: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> forgotPassword(String email, String newPassword) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ApiResponse.error("Email not found", null);
            }

            User user = userOpt.get();
            user.setPassword(newPassword); // In production, hash it
            userRepository.save(user);

            return ApiResponse.success("Password updated successfully");

        } catch (Exception e) {
            return ApiResponse.error("Failed to reset password: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ApiResponse.error("User not found", null);
            }

            User user = userOpt.get();

            // Verify old password
            if (!user.getPassword().equals(oldPassword)) {
                return ApiResponse.error("Current password is incorrect", null);
            }

            // Update password
            user.setPassword(newPassword); // In production, use BCrypt
            userRepository.save(user);

            return ApiResponse.success("Password changed successfully");

        } catch (Exception e) {
            return ApiResponse.error("Failed to change password: " + e.getMessage(), null);
        }
    }
}

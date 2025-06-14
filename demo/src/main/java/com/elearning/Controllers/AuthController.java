package com.elearning.Controllers;

import com.elearning.DTO.*;
import com.elearning.entity.TestSession;
import com.elearning.service.AuthService;
import com.elearning.service.ChapterService;
import com.elearning.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        ApiResponse<UserResponseDTO> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody SignupRequestDTO signupRequest) {
        ApiResponse<String> response = authService.signup(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String newPassword = body.get("newPassword");
        return ResponseEntity.ok(authService.forgotPassword(username, newPassword));
    }


    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String newPassword = body.get("newPassword");
        String oldPassword = body.get("oldPassword");
        ApiResponse<String> response = authService.changePassword(username, oldPassword , newPassword);
        return ResponseEntity.ok(response);
    }

}



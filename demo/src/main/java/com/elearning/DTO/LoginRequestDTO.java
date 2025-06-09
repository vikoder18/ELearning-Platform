package com.elearning.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public LoginRequestDTO() {
    }
    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

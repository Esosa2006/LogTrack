package com.example.LogTrack.models.dtos.authDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDto {
    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email is required!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
}

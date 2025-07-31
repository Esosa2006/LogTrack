package com.example.LogTrack.models.dtos.authDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupervisorSignUpRequest {
    @NotBlank(message = "Name is required!")
    private String name;
    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
    @NotBlank(message = "This field is required!")
    private String confirmPassword;

}

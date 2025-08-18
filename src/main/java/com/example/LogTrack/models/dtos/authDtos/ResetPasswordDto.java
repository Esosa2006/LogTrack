package com.example.LogTrack.models.dtos.authDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank(message = "This field cannot be empty!")
    private String newPassword;
    @NotBlank(message = "This field cannot be empty!")
    private String confirmNewPassword;
}

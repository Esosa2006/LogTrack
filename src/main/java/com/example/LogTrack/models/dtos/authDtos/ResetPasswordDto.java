package com.example.LogTrack.models.dtos.authDtos;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String newPassword;
    private String confirmNewPassword;
}

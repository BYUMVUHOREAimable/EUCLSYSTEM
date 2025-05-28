package com.bai.euclsystem.dtos.requests.auth;


import com.bai.euclsystem.annotations.ValidPassword;
import lombok.Data;

@Data
public class PasswordUpdateDTO {
    private String oldPassword;
    @ValidPassword(message = "Password should be strong")
    private String newPassword;
}
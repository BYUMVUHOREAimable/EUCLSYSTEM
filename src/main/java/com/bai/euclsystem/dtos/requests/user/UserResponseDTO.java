package com.bai.euclsystem.dtos.requests.user;



import com.bai.euclsystem.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private User user;
}
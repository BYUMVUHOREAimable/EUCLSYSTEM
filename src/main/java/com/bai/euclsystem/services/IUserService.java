package com.bai.euclsystem.services;

import com.bai.euclsystem.dtos.requests.auth.UpdateUserDTO;
import com.bai.euclsystem.dtos.requests.user.CreateAdminDTO;
import com.bai.euclsystem.dtos.requests.user.UserResponseDTO;
import com.bai.euclsystem.dtos.requests.user.UserRoleModificationDTO;
import com.bai.euclsystem.models.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    User findUserById(UUID userId);

    User getLoggedInUser();

    UserResponseDTO createAdmin(CreateAdminDTO createUserDTO);

    List<User> getUsers();

    UserResponseDTO getUserById(UUID uuid);

    UserResponseDTO updateUser(UUID userId, UpdateUserDTO updateUserDTO);

    UserResponseDTO addRoles(UUID userId, UserRoleModificationDTO userRoleModificationDTO);

    UserResponseDTO removeRoles(UUID userId, UserRoleModificationDTO userRoleModificationDTO);

    void deleteUser(UUID userId);
}

package com.bai.euclsystem.services;

import com.bai.euclsystem.dtos.requests.role.CreateRoleDTO;
import com.bai.euclsystem.dtos.response.role.RoleResponseDTO;
import com.bai.euclsystem.dtos.response.role.RolesResponseDTO;
import com.bai.euclsystem.enums.ERole;
import com.bai.euclsystem.models.Role;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IRoleService {
    public Role getRoleById(UUID roleId);

    public Role getRoleByName(ERole roleName);

    public void createRole(ERole roleName);

    public RoleResponseDTO createRole(CreateRoleDTO createRoleDTO);

    public RolesResponseDTO getRoles(Pageable pageable);

    public void deleteRole(UUID roleId);

    public boolean isRolePresent(ERole roleName);
}
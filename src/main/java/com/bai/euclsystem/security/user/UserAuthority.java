package com.bai.euclsystem.security.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

public class UserAuthority implements GrantedAuthority {

    private UUID userId;
    private String authority;

    public UserAuthority(UUID userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }

    public UUID getUserId() {
        return userId;
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}

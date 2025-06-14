package com.eg.blps1.dto;

import com.eg.blps1.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private Role role = Role.USER;
    private boolean isLandlord;
}

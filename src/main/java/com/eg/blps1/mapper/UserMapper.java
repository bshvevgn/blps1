package com.eg.blps1.mapper;

import com.eg.blps1.dto.LoginResponse;
import com.eg.blps1.model.User;
import com.eg.blps1.service.UserDetailsImpl;
import com.eg.blps1.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {
    private final JwtUtils jwtUtils;

    public LoginResponse mapToLoginResponse(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
        return new LoginResponse(jwt);
    }

    public LoginResponse mapToLoginResponse(User user) {
        String jwt = jwtUtils.generateJwtToken(user.getUsername());
        return new LoginResponse(jwt);
    }

}

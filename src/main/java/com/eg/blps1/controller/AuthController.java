package com.eg.blps1.controller;

import com.eg.blps1.dto.LoginRequest;
import com.eg.blps1.dto.LoginResponse;
import com.eg.blps1.dto.RegisterRequest;
import com.eg.blps1.mapper.UserMapper;
import com.eg.blps1.model.User;
import com.eg.blps1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        return userMapper.mapToLoginResponse(authentication);
    }

    @PostMapping("/signup")
    public LoginResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        return userMapper.mapToLoginResponse(user);
    }
}
package com.eg.blps1.controller;

import com.eg.blps1.dto.ApiResponse;
import com.eg.blps1.dto.LoginApiResponse;
import com.eg.blps1.dto.LoginRequest;
import com.eg.blps1.dto.RegisterRequest;
import com.eg.blps1.service.UserService;
import com.eg.blps1.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getRole(), request.isLandlord());

        ApiResponse response = new ApiResponse("success", "Пользователь зарегистрирован.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(authToken);
        String token = jwtUtil.generateToken(request.getUsername());

        LoginApiResponse response = new LoginApiResponse(token);
        return ResponseEntity.ok(response);
    }
}

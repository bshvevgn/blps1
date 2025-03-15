package com.eg.blps1.service;

import com.eg.blps1.dto.RegisterRequest;
import com.eg.blps1.exceptions.UsernameAlreadyExistException;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new UsernameAlreadyExistException("Пользователь с таким именем уже существует");
        }

        return userRepository.save(
                new User(
                        registerRequest.username(),
                        passwordEncoder.encode(registerRequest.password()),
                        registerRequest.role()
                )
        );
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден: " + username));
    }
}
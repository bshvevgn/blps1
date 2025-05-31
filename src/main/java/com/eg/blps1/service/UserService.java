package com.eg.blps1.service;

import com.eg.blps1.dto.RegisterRequest;
import com.eg.blps1.exceptions.UsernameAlreadyExistException;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.UserXmlRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserXmlRepository userXmlRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest) {
        if (userXmlRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new UsernameAlreadyExistException();
        }

        return userXmlRepository.save(
                new User(
                        registerRequest.username(),
                        passwordEncoder.encode(registerRequest.password()),
                        registerRequest.role()
                )
        );
    }

    public User findByUsername(String username) {
        return userXmlRepository.findByUsername(username)
                .orElseThrow(() -> new BpmnError("userNotFound", "Неверный логин или пароль"));
    }
}

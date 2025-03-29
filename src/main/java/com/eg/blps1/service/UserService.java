package com.eg.blps1.service;

import com.eg.blps1.dto.RegisterRequest;
import com.eg.blps1.exceptions.UsernameAlreadyExistException;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.UserXmlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserXmlRepository userXmlRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;

    public User registerUser(RegisterRequest registerRequest) {
        return transactionTemplate.execute(status -> {
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
        });
    }

    public User findByUsername(String username) {
        return userXmlRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден: " + username));
    }
}

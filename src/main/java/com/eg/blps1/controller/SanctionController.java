package com.eg.blps1.controller;

import com.eg.blps1.model.User;
import com.eg.blps1.service.SanctionService;
import com.eg.blps1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sanctions")
@RequiredArgsConstructor
public class SanctionController {

    private final SanctionService sanctionService;
    private final UserRepository userRepository;

    @PostMapping("/impose")
    public ResponseEntity<String> imposeSanction(@RequestParam String username, 
                                                 @RequestParam String reason, 
                                                 @RequestParam String expiresAt) {
        User user = userRepository.findByUsername(username).orElseThrow();
        LocalDateTime expirationTime = LocalDateTime.parse(expiresAt);

        sanctionService.imposeSanction(user, reason, expirationTime);

        return ResponseEntity.ok("Санкция применена.");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeSanction(@RequestParam String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        sanctionService.removeSanction(user);

        return ResponseEntity.ok("Санкция снята.");
    }
}

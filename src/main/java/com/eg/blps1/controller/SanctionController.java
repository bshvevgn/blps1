package com.eg.blps1.controller;

import com.eg.blps1.dto.ImposeSanctionDto;
import com.eg.blps1.dto.RemoveSanctionDto;
import com.eg.blps1.model.User;
import com.eg.blps1.service.SanctionService;
import com.eg.blps1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sanctions")
@RequiredArgsConstructor
public class SanctionController {

    private final SanctionService sanctionService;
    private final UserRepository userRepository;

    @PostMapping("/impose")
    public ResponseEntity<String> imposeSanction(@RequestBody ImposeSanctionDto imposeSanctionDto) {
        User user = userRepository.findByUsername(imposeSanctionDto.getUsername()).orElseThrow();

        sanctionService.imposeSanction(user, imposeSanctionDto.getReason(), imposeSanctionDto.getExpiresAt());

        return ResponseEntity.ok("Санкция применена.");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeSanction(@RequestBody RemoveSanctionDto removeSanctionDto) {
        User user = userRepository.findByUsername(removeSanctionDto.getUsername()).orElseThrow();

        sanctionService.removeSanction(user);

        return ResponseEntity.ok("Санкция снята.");
    }
}

package com.eg.blps1.controller;

import com.eg.blps1.dto.ApiResponse;
import com.eg.blps1.dto.ImposeSanctionDto;
import com.eg.blps1.dto.RemoveSanctionDto;
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
    public ResponseEntity<ApiResponse> imposeSanction(@RequestBody ImposeSanctionDto imposeSanctionDto) {
        User user = userRepository.findByUsername(imposeSanctionDto.getUsername()).orElseThrow();
        LocalDateTime expirationTime = LocalDateTime.parse(imposeSanctionDto.getExpiresAt());

        sanctionService.imposeSanction(user, imposeSanctionDto.getReason(), expirationTime);

        ApiResponse response = new ApiResponse("success", "Санкция применена.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove")
    public ResponseEntity<ApiResponse> removeSanction(@RequestBody RemoveSanctionDto removeSanctionDto) {
        User user = userRepository.findByUsername(removeSanctionDto.getUsername()).orElseThrow();

        sanctionService.removeSanction(user);

        ApiResponse response = new ApiResponse("success", "Санкция снята.");
        return ResponseEntity.ok(response);
    }
}

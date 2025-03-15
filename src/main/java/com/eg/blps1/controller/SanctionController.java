package com.eg.blps1.controller;

import com.eg.blps1.dto.ImposeSanctionDto;
import com.eg.blps1.dto.RemoveSanctionDto;
import com.eg.blps1.model.User;
import com.eg.blps1.service.SanctionService;
import com.eg.blps1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sanctions")
@RequiredArgsConstructor
public class SanctionController {

    private final SanctionService sanctionService;
    private final UserRepository userRepository;

    @PostMapping("/impose")
    @ResponseStatus(HttpStatus.OK)
    public void imposeSanction(@RequestBody ImposeSanctionDto imposeSanctionDto) {
        User user = userRepository.findByUsername(imposeSanctionDto.getUsername()).orElseThrow();

        sanctionService.imposeSanction(user, imposeSanctionDto.getReason(), imposeSanctionDto.getExpiresAt());
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public void removeSanction(@RequestBody RemoveSanctionDto removeSanctionDto) {
        User user = userRepository.findByUsername(removeSanctionDto.getUsername()).orElseThrow();

        sanctionService.removeSanction(user);
    }
}

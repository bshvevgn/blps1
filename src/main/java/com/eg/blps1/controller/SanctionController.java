package com.eg.blps1.controller;

import com.eg.blps1.dto.ImposeSanctionDto;
import com.eg.blps1.dto.RemoveSanctionDto;
import com.eg.blps1.service.SanctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sanctions")
@RequiredArgsConstructor
public class SanctionController {

    private final SanctionService sanctionService;

    @PostMapping("/impose")
    @ResponseStatus(HttpStatus.OK)
    public void imposeSanction(@Valid @RequestBody ImposeSanctionDto imposeSanctionDto) {
        sanctionService.imposeSanction(imposeSanctionDto);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public void removeSanction(@Valid @RequestBody RemoveSanctionDto removeSanctionDto) {
        sanctionService.remove(removeSanctionDto);
    }
}

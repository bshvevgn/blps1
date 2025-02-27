package com.eg.blps1.controller;

import com.eg.blps1.model.RequestStatus;
import com.eg.blps1.model.SanctionRequest;
import com.eg.blps1.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    // Создать новую заявку (доступно любому авторизованному пользователю)
    @PostMapping("/create")
    public ResponseEntity<SanctionRequest> createRequest(@RequestParam String description) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        SanctionRequest newRequest = requestService.createRequest(description, currentUsername);
        return ResponseEntity.ok(newRequest);
    }

    // Получить все заявки (например, для модератора)
    @GetMapping
    public ResponseEntity<List<SanctionRequest>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    // Модератор назначает заявку на себя
    @PostMapping("/assign")
    public ResponseEntity<SanctionRequest> assignRequest(@RequestParam Long requestId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        SanctionRequest assigned = requestService.assignRequest(requestId, currentUsername);
        return ResponseEntity.ok(assigned);
    }

    // Модератор обновляет статус заявки (APPROVED, REJECTED и т.д.)
    @PostMapping("/update-status")
    public ResponseEntity<SanctionRequest> updateStatus(@RequestParam Long requestId,
                                                        @RequestParam RequestStatus status) {
        SanctionRequest updated = requestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok(updated);
    }
}

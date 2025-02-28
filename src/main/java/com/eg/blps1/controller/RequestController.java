package com.eg.blps1.controller;

import com.eg.blps1.model.RequestStatus;
import com.eg.blps1.model.SanctionRequest;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.RequestRepository;
import com.eg.blps1.repository.UserRepository;
import com.eg.blps1.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;


    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestParam String description) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + currentUsername));

        long activeRequestsCount = requestRepository.countByCreatedByAndStatusIn(
                user, List.of(RequestStatus.CREATED, RequestStatus.UNDER_REVIEW));

        if (activeRequestsCount >= 5) {
            return ResponseEntity.status(403).body("Вы не можете создать новую заявку, пока у вас есть 5 активных заявок.");
        }

        SanctionRequest request = new SanctionRequest();
        request.setCreatedBy(user);
        request.setDescription(description);
        request.setStatus(RequestStatus.CREATED);

        requestRepository.save(request);
        return ResponseEntity.ok(request);
    }

    @GetMapping
    public ResponseEntity<List<SanctionRequest>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @PostMapping("/assign")
    public ResponseEntity<SanctionRequest> assignRequest(@RequestParam Long requestId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        SanctionRequest assigned = requestService.assignRequest(requestId, currentUsername);
        return ResponseEntity.ok(assigned);
    }

    @PostMapping("/update-status")
    public ResponseEntity<SanctionRequest> updateStatus(@RequestParam Long requestId,
                                                        @RequestParam RequestStatus status) {
        SanctionRequest updated = requestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok(updated);
    }
}

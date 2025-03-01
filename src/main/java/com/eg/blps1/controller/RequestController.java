package com.eg.blps1.controller;

import com.eg.blps1.dto.ApiResponse;
import com.eg.blps1.dto.AssignRequestDto;
import com.eg.blps1.dto.CreateRequestDto;
import com.eg.blps1.dto.UpdateStatusDto;
import com.eg.blps1.model.RequestStatus;
import com.eg.blps1.model.SanctionRequest;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.RequestRepository;
import com.eg.blps1.repository.UserRepository;
import com.eg.blps1.service.RequestService;
import com.eg.blps1.service.SanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final SanctionService sanctionService;

    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody CreateRequestDto requestDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + currentUsername));

        long activeRequestsCount = requestRepository.countByCreatedByAndStatusIn(
                user, List.of(RequestStatus.CREATED, RequestStatus.UNDER_REVIEW));

        if (activeRequestsCount >= 5) {
            ApiResponse response = new ApiResponse("error", "Вы не можете создать новую заявку, пока у вас есть 5 активных заявок.");
            return ResponseEntity.status(403).body(response);
        }

        SanctionRequest request = new SanctionRequest();
        request.setCreatedBy(user);
        request.setDescription(requestDto.getDescription());
        request.setStatus(RequestStatus.CREATED);

        requestRepository.save(request);
        return ResponseEntity.ok(request);
    }

    @GetMapping
    public ResponseEntity<List<SanctionRequest>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @PutMapping("/assign")
    public ResponseEntity<SanctionRequest> assignRequest(@RequestBody AssignRequestDto assignRequestDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        SanctionRequest assigned = requestService.assignRequest(assignRequestDto.getRequestId(), currentUsername);
        return ResponseEntity.ok(assigned);
    }

    @PutMapping("/update-status")
    public ResponseEntity<SanctionRequest> updateStatus(@RequestBody UpdateStatusDto updateStatusDto) {
        User user = userRepository.findById(updateStatusDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (updateStatusDto.getStatus() == RequestStatus.APPROVED) {
            LocalDateTime expirationTime = LocalDateTime.parse(updateStatusDto.getExpiresAt());
            sanctionService.imposeSanction(user, "Заявка принята, санкция наложена.", expirationTime);
        }

        SanctionRequest updated = requestService.updateRequestStatus(updateStatusDto.getRequestId(), updateStatusDto.getStatus());
        return ResponseEntity.ok(updated);
    }
}

package com.eg.blps1.service;

import com.eg.blps1.model.RequestStatus;
import com.eg.blps1.model.Role;
import com.eg.blps1.model.SanctionRequest;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final UserService userService;
    private final EmailService emailService;
    private final RequestRepository requestRepository;

    /**
     * Создать новую заявку
     */
    public SanctionRequest createRequest(String description, String creatorUsername) {
        User creator = userService.findByUsername(creatorUsername);
        SanctionRequest request = new SanctionRequest();
        request.setDescription(description);
        request.setCreatedBy(creator);
        // Поля createdAt и status установятся в @PrePersist
        return requestRepository.save(request);
    }

    /**
     * Получить все заявки
     */
    public List<SanctionRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    /**
     * Модератор назначает заявку на себя
     */
    public SanctionRequest assignRequest(Long requestId, String moderatorUsername) {
        SanctionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        if (request.getStatus() != RequestStatus.CREATED
                && request.getStatus() != RequestStatus.ASSIGNED) {
            throw new RuntimeException("Нельзя назначить заявку, у которой статус не CREATED/ASSIGNED");
        }

        User moderator = userService.findByUsername(moderatorUsername);
        if (moderator.getRole() != Role.MODERATOR) {
            throw new RuntimeException("Пользователь не является модератором");
        }

        request.setAssignedModerator(moderator);
        request.setStatus(RequestStatus.ASSIGNED); return requestRepository.save(request); }

    public SanctionRequest updateRequestStatus(Long requestId, RequestStatus status) {
        SanctionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        request.setStatus(status);
        SanctionRequest saved = requestRepository.save(request);

        emailService.sendStatusChangeMessage(saved.getCreatedBy(), saved);
        return saved;
    }
}

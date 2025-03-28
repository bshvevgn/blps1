package com.eg.blps1.service;

import com.eg.blps1.dto.ImposeSanctionDto;
import com.eg.blps1.dto.RemoveSanctionDto;
import com.eg.blps1.mapper.SanctionMapper;
import com.eg.blps1.model.Sanction;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SanctionService {

    private final SanctionMapper sanctionMapper;
    private final UserService userService;
    private final SanctionRepository sanctionRepository;
    private final TransactionTemplate transactionTemplate;

    public void imposeSanction(ImposeSanctionDto imposeSanctionDto) {
        User user = userService.findByUsername(imposeSanctionDto.username());
        imposeSanction(user, imposeSanctionDto.reason(), imposeSanctionDto.expiresAt());
    }

    public void imposeSanction(User user, String reason, Instant expiresAt) {
        transactionTemplate.execute(status -> {
            try {
                Sanction sanction = sanctionMapper.mapToEntity(user, reason, expiresAt);
                sanctionRepository.save(sanction);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
    }

    public void remove(RemoveSanctionDto removeSanctionDto) {
        transactionTemplate.execute(status -> {
            try {
                User user = userService.findByUsername(removeSanctionDto.username());
                sanctionRepository.deleteAllByUserAndExpiresAtAfter(user, Instant.now());
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
    }

    public boolean hasActiveSanction(User user) {
        return !sanctionRepository.findByUserAndExpiresAtAfter(user, Instant.now()).isEmpty();
    }
}

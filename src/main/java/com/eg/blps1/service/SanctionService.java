package com.eg.blps1.service;

import com.eg.blps1.model.Sanction;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanctionService {

    private final SanctionRepository sanctionRepository;

    public void imposeSanction(User user, String reason, LocalDateTime expiresAt) {
        Sanction sanction = new Sanction();
        sanction.setUser(user);
        sanction.setReason(reason);
        sanction.setExpiresAt(expiresAt);
        sanctionRepository.save(sanction);
    }

    public void removeSanction(User user) {
        List<Sanction> sanctions = sanctionRepository.findByUserAndExpiresAtAfter(user, LocalDateTime.now());
        for (Sanction sanction : sanctions) {
            sanctionRepository.delete(sanction);
        }
    }

    public boolean hasActiveSanction(User user) {
        return !sanctionRepository.findByUserAndExpiresAtAfter(user, LocalDateTime.now()).isEmpty();
    }
}

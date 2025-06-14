package com.eg.blps1.repository;

import com.eg.blps1.model.Sanction;
import com.eg.blps1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanctionRepository extends JpaRepository<Sanction, Long> {
    List<Sanction> findByUserAndExpiresAtAfter(User user, java.time.LocalDateTime now);
}

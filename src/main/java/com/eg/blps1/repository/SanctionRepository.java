package com.eg.blps1.repository;

import com.eg.blps1.model.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long> {
    List<Sanction> findByUsernameAndExpiresAtAfter(String username, Instant now);
    void deleteAllByUsernameAndExpiresAtAfter(String username, Instant now);
}

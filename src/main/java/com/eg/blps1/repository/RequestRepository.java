package com.eg.blps1.repository;

import com.eg.blps1.model.RequestStatus;
import com.eg.blps1.model.SanctionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<SanctionRequest, Long> {
    List<SanctionRequest> findByStatus(RequestStatus status);
}

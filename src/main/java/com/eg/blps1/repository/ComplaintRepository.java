package com.eg.blps1.repository;

import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.ComplaintStatus;
import com.eg.blps1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStatus(ComplaintStatus status);

    long countByApplicantAndStatusIn(String applicant, List<ComplaintStatus> statuses);
}

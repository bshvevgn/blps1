package com.eg.blps1.service;

import com.eg.blps1.exceptions.SpamComplaintsException;
import com.eg.blps1.model.ComplaintStatus;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ComplaintRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class SpamService {
    private final ComplaintRepository complaintRepository;
    private final TransactionTemplate transactionTemplate;

    public void checkSpamComplaints(User applicant) {
        long activeRequestsCount = complaintRepository.countByApplicantAndStatusIn(
                applicant, List.of(ComplaintStatus.CREATED, ComplaintStatus.ASSIGNED)
        );

        if (activeRequestsCount >= 5) {
            throw new SpamComplaintsException("Вы не можете создать новую заявку, пока у вас не меньше 5 активных заявок.");
        }
    }
}

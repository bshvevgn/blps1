package com.eg.blps1.service;

import com.eg.blps1.dto.AssignModeratorRequest;
import com.eg.blps1.dto.ComplaintRequest;
import com.eg.blps1.dto.UpdateStatusRequest;
import com.eg.blps1.exceptions.ApplicantMatchesDefendantException;
import com.eg.blps1.exceptions.ComplaintNotFoundException;
import com.eg.blps1.exceptions.ModeratorNotAssignedComplaintException;
import com.eg.blps1.mapper.ComplaintMapper;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.ComplaintStatus;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ComplaintRepository;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintMapper complaintMapper;
    private final UserService userService;
    private final SpamService spamService;
    private final EmailService emailService;
    private final SanctionService sanctionService;
    private final ComplaintRepository complaintRepository;

    public Complaint create(ComplaintRequest complaintRequest) {
        User applicant = CommonUtils.getUserIdFromSecurityContext();
        User defendant = userService.findByUsername(complaintRequest.defendant());
        if (Objects.equals(applicant.getUsername(), defendant.getUsername())) {
            throw new ApplicantMatchesDefendantException();
        }
        spamService.checkSpamComplaints(applicant);

        Complaint complaint = complaintMapper.mapToEntity(complaintRequest, applicant, defendant);
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAll() {
        return complaintRepository.findAll();
    }

    public Complaint assignModerator(AssignModeratorRequest assignModeratorRequest) {
        Complaint complaint = getById(assignModeratorRequest.complaintId());

        if (complaint.getStatus() != ComplaintStatus.CREATED && complaint.getStatus() != ComplaintStatus.ASSIGNED) {
            throw new RuntimeException("Нельзя назначить заявку, у которой статус не CREATED/ASSIGNED");
        }
        User moderator = CommonUtils.getUserIdFromSecurityContext();
        complaintMapper.enrichToAssignModerator(complaint, moderator);
        return complaintRepository.save(complaint);
    }

    @Transactional
    public Complaint updateComplaintStatus(UpdateStatusRequest updateStatusRequest) {
        Complaint complaint = getById(updateStatusRequest.complaintId());
        User moderator = CommonUtils.getUserIdFromSecurityContext();

        if (complaint.getStatus() != ComplaintStatus.ASSIGNED || !complaint.getModerator().getUsername().equals(moderator.getUsername())) {
            throw new ModeratorNotAssignedComplaintException();
        }
        if (updateStatusRequest.status() == ComplaintStatus.APPROVED) {
            sanctionService.imposeSanction(complaint.getDefendant(), "Заявка принята, санкция наложена.", updateStatusRequest.expiresAt());
        }

        complaintMapper.mergeToStatus(complaint, updateStatusRequest.status());
        Complaint saved = complaintRepository.save(complaint);
        emailService.sendStatusChangeMessage(saved.getApplicant(), saved);
        return saved;
    }

    private Complaint getById(Long complaintId) {
        return complaintRepository.findById(complaintId).orElseThrow(() -> new ComplaintNotFoundException(complaintId));

    }
}

package com.eg.blps1.service;

import com.eg.blps1.dto.AssignModeratorRequest;
import com.eg.blps1.dto.ComplaintRequest;
import com.eg.blps1.dto.UpdateStatusRequest;
import com.eg.blps1.exceptions.ApplicantMatchesDefendantException;
import com.eg.blps1.exceptions.ComplaintNotFoundException;
import com.eg.blps1.exceptions.IncorrectRoleIndicationException;
import com.eg.blps1.exceptions.ModeratorNotAssignedComplaintException;
import com.eg.blps1.mapper.ComplaintMapper;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.ComplaintStatus;
import com.eg.blps1.model.RoleEnum;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ComplaintRepository;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

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
    private final TransactionTemplate transactionTemplate;

    public Complaint create(ComplaintRequest complaintRequest) {
        return transactionTemplate.execute(status -> {
            User applicant = CommonUtils.getUserFromSecurityContext();
            User defendant = userService.findByUsername(complaintRequest.defendant());
            if (Objects.equals(applicant.getUsername(), defendant.getUsername())) {
                throw new ApplicantMatchesDefendantException();
            }
            if (!((applicant.getRole() == RoleEnum.ROLE_USER && defendant.getRole() == RoleEnum.ROLE_LANDLORD) ||
                    (applicant.getRole() == RoleEnum.ROLE_LANDLORD && defendant.getRole() == RoleEnum.ROLE_USER))) {
                throw new IncorrectRoleIndicationException();
            }
            spamService.checkSpamComplaints(applicant);

            Complaint complaint = complaintMapper.mapToEntity(complaintRequest, applicant, defendant);
            return complaintRepository.save(complaint);
        });
    }

    public List<Complaint> getAll() {
        return complaintRepository.findAll();
    }

    public Complaint assignModerator(AssignModeratorRequest assignModeratorRequest) {
        Complaint complaint = getById(assignModeratorRequest.complaintId());

        if (complaint.getStatus() != ComplaintStatus.CREATED && complaint.getStatus() != ComplaintStatus.ASSIGNED) {
            throw new RuntimeException("Нельзя назначить заявку, у которой статус не CREATED/ASSIGNED");
        }
        User moderator = CommonUtils.getUserFromSecurityContext();
        complaintMapper.enrichToAssignModerator(complaint, moderator);
        return complaintRepository.save(complaint);
    }

    public Complaint updateComplaintStatus(UpdateStatusRequest updateStatusRequest) {
        return transactionTemplate.execute(status -> {
            Complaint complaint = getById(updateStatusRequest.complaintId());
            User moderator = CommonUtils.getUserFromSecurityContext();

            if (complaint.getStatus() != ComplaintStatus.ASSIGNED || !complaint.getModerator().equals(moderator.getUsername())) {
                throw new ModeratorNotAssignedComplaintException();
            }
            if (updateStatusRequest.status() == ComplaintStatus.APPROVED) {
                sanctionService.imposeSanction(complaint.getDefendant(), "Заявка принята, санкция наложена.", updateStatusRequest.expiresAt());
            }

            complaintMapper.mergeToStatus(complaint, updateStatusRequest.status());
            Complaint saved = complaintRepository.save(complaint);
            emailService.sendStatusChangeMessage(saved.getApplicant(), saved);
            return saved;
        });
    }

    private Complaint getById(Long complaintId) {
        return complaintRepository.findById(complaintId).orElseThrow(() -> new ComplaintNotFoundException(complaintId));
    }
}

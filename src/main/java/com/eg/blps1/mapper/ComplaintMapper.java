package com.eg.blps1.mapper;

import com.eg.blps1.dto.ComplaintRequest;
import com.eg.blps1.dto.ComplaintResponse;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.ComplaintStatus;
import com.eg.blps1.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComplaintMapper {
    public Complaint mapToEntity(ComplaintRequest complaintRequest, User applicant, User defendant) {
        Complaint complaint = new Complaint();
        complaint.setTitle(complaintRequest.title());
        complaint.setDescription(complaintRequest.description());
        complaint.setApplicant(applicant);
        complaint.setDefendant(defendant);
        return complaint;
    }

    public Complaint enrichToAssignModerator(Complaint complaint, User moderator) {
        complaint.setModerator(moderator);
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        return complaint;
    }

    public Complaint mergeToStatus(Complaint complaint, ComplaintStatus status) {
        complaint.setStatus(status);
        return complaint;
    }

    public ComplaintResponse mapToComplaintResponse(Complaint complaint) {
        return new ComplaintResponse(
                complaint.getId(),
                complaint.getTitle(),
                complaint.getDescription(),
                complaint.getStatus(),
                complaint.getApplicant().getUsername(),
                complaint.getDefendant().getUsername(),
                complaint.getCreatedAt()
        );
    }

    public List<ComplaintResponse> mapToListComplaint(List<Complaint> complaints) {
        return complaints.stream().map(this::mapToComplaintResponse).toList();
    }
}

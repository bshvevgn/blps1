package com.eg.blps1.controller;

import com.eg.blps1.dto.AssignModeratorRequest;
import com.eg.blps1.dto.ComplaintRequest;
import com.eg.blps1.dto.ComplaintResponse;
import com.eg.blps1.dto.UpdateStatusRequest;
import com.eg.blps1.mapper.ComplaintMapper;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaint")
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintMapper complaintMapper;
    private final ComplaintService complaintService;

    @PostMapping("/create")
    public ComplaintResponse createRequest(@Valid @RequestBody ComplaintRequest complaintRequest) {
        Complaint complaint = complaintService.create(complaintRequest);
        return complaintMapper.mapToComplaintResponse(complaint);
    }

    @GetMapping
    public List<ComplaintResponse> getAllRequests() {
        List<Complaint> complaints = complaintService.getAll();
        return complaintMapper.mapToListComplaint(complaints);
    }

    @PatchMapping("/assign")
    @ResponseStatus(HttpStatus.OK)
    public void assignRequest(@Valid @RequestBody AssignModeratorRequest assignModeratorRequest) {
        complaintService.assignModerator(assignModeratorRequest);
    }

    @PatchMapping("/status")
    public ComplaintResponse updateStatus(@Valid @RequestBody UpdateStatusRequest updateStatusRequest) {
        Complaint complaint = complaintService.updateComplaintStatus(updateStatusRequest);
        return complaintMapper.mapToComplaintResponse(complaint);
    }
}

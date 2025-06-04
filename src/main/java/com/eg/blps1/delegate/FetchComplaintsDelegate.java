package com.eg.blps1.delegate;

import com.eg.blps1.dto.ComplaintResponse;
import com.eg.blps1.mapper.ComplaintMapper;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.service.ComplaintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchComplaintsDelegate implements JavaDelegate {

    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<Complaint> listings = complaintService.getAll();
        List<ComplaintResponse> response = complaintMapper.mapToListComplaint(listings);

        String json = objectMapper.writeValueAsString(response);

        execution.setVariable("complaints", json);
    }
}

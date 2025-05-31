package com.eg.blps1.delegate;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.service.UserService;
import com.eg.blps1.utils.CommonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListingCreatorDelegate implements JavaDelegate {

    private final UserService userService;
    private final ListingMapper listingMapper;
    private final ListingRepository listingRepository;
    private final Validator validator;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        String address = (String) execution.getVariable("address");
        String priceRaw = String.valueOf(execution.getVariable("price"));
        String note = (String) execution.getVariable("note");

        double price;
        try {
            price = Double.parseDouble(priceRaw);
        } catch (NumberFormatException e) {
            throw new BpmnError("validationError", "Цена должна быть числом");
        }

        ListingRequest request = new ListingRequest(address, price, note);

        Set<ConstraintViolation<ListingRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new BpmnError("validationError", errorMessage);
        }

        User user = userService.findByUsername(username);
        Listing listing = listingMapper.mapToEntity(request, user);

        try {
            Listing saved = listingRepository.save(listing);
            if (saved.getId() != null) {
                execution.setVariable("statusMessage", "Объявление успешно добавлено");
            } else {
                throw new BpmnError("dbError", "Ошибка при добавлении объявления");
            }
        } catch (Exception e) {
            throw new BpmnError("dbError", "Ошибка при добавлении объявления: " + e.getMessage());
        }
    }
}


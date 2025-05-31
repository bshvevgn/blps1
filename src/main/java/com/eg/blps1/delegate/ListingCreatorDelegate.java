package com.eg.blps1.delegate;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.service.UserService;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListingCreatorDelegate implements JavaDelegate {

    private final ListingMapper listingMapper;
    private final ListingRepository listingRepository;
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution) {
        String address = (String) execution.getVariable("address");
        String priceRaw = String.valueOf(execution.getVariable("price"));
        String note = (String) execution.getVariable("note");

        if (address == null || address.trim().isEmpty() ||
                priceRaw == null || priceRaw.trim().isEmpty() ||
                note == null || note.trim().isEmpty()) {
            throw new BpmnError("validationError", "Все поля должны быть заполнены");
        }

        double price;
        try {
            price = Double.parseDouble(priceRaw);
        } catch (NumberFormatException e) {
            throw new BpmnError("validationError", "Цена должна быть числом");
        }

        String username = (String) execution.getVariable("username");


        ListingRequest request = new ListingRequest(address, price, note);
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

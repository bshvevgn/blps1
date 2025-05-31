package com.eg.blps1.delegate;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.service.UserService;
import lombok.RequiredArgsConstructor;
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
        String username = (String) execution.getVariable("username");
        String address = (String) execution.getVariable("address");
        String note = (String) execution.getVariable("note");
        double price = (Double) execution.getVariable("price");

        User user = userService.findByUsername(username);
        ListingRequest request = new ListingRequest(address, price, note);
        Listing listing = listingMapper.mapToEntity(request, user);

        listingRepository.save(listing);
    }
}

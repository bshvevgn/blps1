package com.eg.blps1.delegate;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.service.UserService;
import com.eg.blps1.utils.CommonUtils;
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
        String address = (String) execution.getVariable("address");
        Double price = (Double) execution.getVariable("price");
        String note = (String) execution.getVariable("note");

        String username = (String) execution.getVariable("username");


        ListingRequest request = new ListingRequest(address, price, note);
        User user = userService.findByUsername(username);

        Listing listing = listingMapper.mapToEntity(request, user);

        listingRepository.save(listing);
    }
}

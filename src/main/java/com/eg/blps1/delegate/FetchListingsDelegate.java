package com.eg.blps1.delegate;

import com.eg.blps1.dto.ListingResponse;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.service.ListingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FetchListingsDelegate implements JavaDelegate {

    private final ListingService listingService;
    private final ListingMapper listingMapper;
    private final ObjectMapper objectMapper;

    public FetchListingsDelegate(ListingService listingService,
                                 ListingMapper listingMapper,
                                 ObjectMapper objectMapper) {
        this.listingService = listingService;
        this.listingMapper = listingMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<Listing> listings = listingService.getAll();
        List<ListingResponse> response = listingMapper.mapToListListing(listings);

        String json = objectMapper.writeValueAsString(response);

        execution.setVariable("listings", json);
    }
}

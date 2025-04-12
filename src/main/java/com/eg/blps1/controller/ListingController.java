package com.eg.blps1.controller;

import com.eg.blps1.dto.ComplaintResponse;
import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.dto.ListingResponse;
import com.eg.blps1.mapper.ComplaintMapper;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.Listing;
import com.eg.blps1.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;
    private final ListingMapper listingMapper;


    @GetMapping
    public List<ListingResponse> getAllRequests() {
        List<Listing> listings = listingService.getAll();
        return listingMapper.mapToListListing(listings);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createListing(@Valid @RequestBody ListingRequest request) {
        listingService.create(request);
    }
}

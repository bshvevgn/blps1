package com.eg.blps1.controller;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createListing(@Valid @RequestBody ListingRequest request) {
        listingService.create(request);
    }
}

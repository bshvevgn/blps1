package com.eg.blps1.controller;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.exceptions.ActiveSanctionException;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.repository.UserRepository;
import com.eg.blps1.service.SanctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final SanctionService sanctionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createListing(@Valid @RequestBody ListingRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (sanctionService.hasActiveSanction(user)) {
            throw new ActiveSanctionException();
        }

        Listing listing = new Listing(request.address(), request.price(), request.note(), user);
        listingRepository.save(listing);
    }
}

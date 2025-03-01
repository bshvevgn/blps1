package com.eg.blps1.controller;

import com.eg.blps1.dto.ApiResponse;
import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.repository.UserRepository;
import com.eg.blps1.service.SanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> createListing(@RequestBody ListingRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!user.isLandlord()) {
            ApiResponse response = new ApiResponse("error", "Вы не арендодатель.");
            return ResponseEntity.status(403).body(response);
        }

        if (sanctionService.hasActiveSanction(user)) {
            ApiResponse response = new ApiResponse("error", "Вы не можете размещать объявления из-за санкции.");
            return ResponseEntity.status(403).body(response);
        }

        Listing listing = new Listing(request.getAddress(), request.getPrice(), request.getNote(), user);
        listingRepository.save(listing);

        ApiResponse response = new ApiResponse("success", "Объявление размещено.");
        return ResponseEntity.ok(response);
    }
}

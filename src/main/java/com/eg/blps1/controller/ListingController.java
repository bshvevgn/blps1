package com.eg.blps1.controller;

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
    public ResponseEntity<String> createListing(@RequestParam String address,
                                                @RequestParam double price,
                                                @RequestParam String note) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!user.isLandlord()) {
            return ResponseEntity.status(403).body("Вы не арендодатель.");
        }

        if (sanctionService.hasActiveSanction(user)) {
            return ResponseEntity.status(403).body("Вы не можете размещать объявления из-за санкции.");
        }

        Listing listing = new Listing(address, price, note, user);
        listingRepository.save(listing);

        return ResponseEntity.ok("Объявление размещено.");
    }
}

package com.eg.blps1.controller;

import com.eg.blps1.dto.ApiResponse;
import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.repository.UserRepository;
import com.eg.blps1.service.SanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final SanctionService sanctionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> bookListing(@RequestBody BookingRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Listing listing = listingRepository.findById(request.getListingId()).orElseThrow();

        if (sanctionService.hasActiveSanction(user)) {
            ApiResponse response = new ApiResponse("error", "Вы не можете бронировать из-за санкции.");
            return ResponseEntity.status(403).body(response);
        }

        Booking booking = new Booking(user, listing);
        bookingRepository.save(booking);

        ApiResponse response = new ApiResponse("success", "Бронирование успешно.");
        return ResponseEntity.ok(response);
    }
}

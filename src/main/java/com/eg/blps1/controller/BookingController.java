package com.eg.blps1.controller;

import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingservice;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void bookListing(@Valid @RequestBody BookingRequest request) {
        bookingservice.create(request);
    }
}

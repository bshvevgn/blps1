package com.eg.blps1.service;

import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.exceptions.ActiveSanctionException;
import com.eg.blps1.exceptions.ListingNotFoundException;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookingService {
    private final SanctionService sanctionService;
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;

    public Booking create(BookingRequest request) {
        User user = CommonUtils.getUserFromSecurityContext();
        Listing listing = listingRepository.findById(request.listingId()).orElseThrow(
                () -> new ListingNotFoundException(request.listingId())
        );

        if (sanctionService.hasActiveSanction(user)) throw new ActiveSanctionException();

        Booking booking = new Booking(user, listing);
        return bookingRepository.save(booking);
    }
}

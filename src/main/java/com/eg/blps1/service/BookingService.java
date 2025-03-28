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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


@Service
@RequiredArgsConstructor
public class BookingService {
    private final SanctionService sanctionService;
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;

    private final TransactionTemplate transactionTemplate; // Добавили TransactionTemplate

    public Booking create(BookingRequest request) {
        return transactionTemplate.execute(status -> {
            User user = CommonUtils.getUserFromSecurityContext();
            Listing listing = listingRepository.findById(request.listingId()).orElseThrow(
                    () -> new ListingNotFoundException(request.listingId())
            );

            if (sanctionService.hasActiveSanction(user)) throw new ActiveSanctionException();

            Booking booking = new Booking(user, listing);
            return bookingRepository.save(booking);
        });
    }
}

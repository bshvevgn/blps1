package com.eg.blps1.service;

import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.exceptions.ActiveSanctionException;
import com.eg.blps1.exceptions.BookingConflictException;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final SanctionService sanctionService;
    private final BookingRepository bookingRepository;
    private final ListingService listingService;
    private final TransactionTemplate transactionTemplate;

    public Booking create(BookingRequest request) {
        return transactionTemplate.execute(status -> {
            User user = CommonUtils.getUserFromSecurityContext();
            if (sanctionService.hasActiveSanction(user)) throw new ActiveSanctionException();

            Listing listing = listingService.findById(request.listingId());
            boolean isConflictBooking = bookingRepository.existsByListingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    listing, request.endDate(), request.startDate()
            );
            if (isConflictBooking) throw new BookingConflictException("Объявление уже забронировано в указанный период.");

            Booking booking = new Booking(user.getUsername(), listing, request.startDate(), request.endDate());
            return bookingRepository.save(booking);
        });
    }
}

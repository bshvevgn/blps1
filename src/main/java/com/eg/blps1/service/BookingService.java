package com.eg.blps1.service;

import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.client.dto.DebitResponse;
import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.exceptions.ActiveSanctionException;
import com.eg.blps1.exceptions.BookingConflictException;
import com.eg.blps1.exceptions.PaymentException;
import com.eg.blps1.mapper.BankMapper;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BankMapper bankMapper;
    private final BankService bankService;
    private final SanctionService sanctionService;
    private final ListingService listingService;
    private final BookingRepository bookingRepository;
    private final TransactionTemplate transactionTemplate;

    public Booking create(BookingRequest request) {
        User user = CommonUtils.getUserFromSecurityContext();
        if (sanctionService.hasActiveSanction(user)) throw new ActiveSanctionException("Вы не можете бронировать помещение из-за действующей санкции.");

        Listing listing = listingService.findById(request.listingId());
        boolean isConflictBooking = bookingRepository.existsByListingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                listing, request.endDate(), request.startDate()
        );
        if (isConflictBooking) throw new BookingConflictException();

        AtomicReference<DebitResponse> debitResponseRef = new AtomicReference<>();
        try {
            return transactionTemplate.execute(status -> {
                Booking booking = new Booking(user.getUsername(), listing, request.startDate(), request.endDate());
                booking = bookingRepository.save(booking);

                DebitRequest debitRequest = bankMapper.mapToDebitRequest(request, listing);
                debitResponseRef.set(bankService.debit(debitRequest));
                return booking;
            });
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            DebitResponse debitResponse = debitResponseRef.get();
            if (debitResponse != null) {
                bankService.refund(debitResponse.transactionId());
                throw new PaymentException();
            }
            throw ex;
        }
    }
}

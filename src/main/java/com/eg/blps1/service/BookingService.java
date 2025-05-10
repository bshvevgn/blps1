package com.eg.blps1.service;

import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.client.dto.DebitResponse;
import com.eg.blps1.config.kafka.KafkaProperty;
import com.eg.blps1.dto.BookingReportDto;
import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.exceptions.ActiveSanctionException;
import com.eg.blps1.exceptions.BookingConflictException;
import com.eg.blps1.exceptions.PaymentException;
import com.eg.blps1.mapper.BankMapper;
import com.eg.blps1.mapper.KafkaMapper;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.utils.CommonUtils;
import com.eg.blps1.utils.SerializationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final ObjectMapper objectMapper;
    private final BankMapper bankMapper;
    private final KafkaMapper kafkaMapper;
    private final BankService bankService;
    private final SanctionService sanctionService;
    private final ListingService listingService;
    private final KafkaService kafkaService;
    private final OutboxService outboxService;
    private final BookingRepository bookingRepository;
    private final TransactionTemplate transactionTemplate;
    private final KafkaProperty kafkaProperty;

    public Booking create(BookingRequest request) {
        log.info("Try create booking for listingId=[{}]", request.listingId());
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

                BookingReportDto bookingReportDto = kafkaMapper.mapToBookingReport(booking);
                String payload = SerializationUtils.getString(objectMapper, bookingReportDto);
                outboxService.create(kafkaProperty.getTopics().getBookingReport().getName(), payload);
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

    public List<Booking> getBookingsBeforeDate(LocalDate startDate) {
        log.info("Getting bookings before start date=[{}]", startDate);
        return bookingRepository.findBookingsByStartDateBefore(startDate);
    }
}

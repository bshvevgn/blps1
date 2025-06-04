package com.eg.blps1.delegate;

import com.eg.blps1.config.kafka.KafkaProperty;
import com.eg.blps1.dto.DigestReportDto;
import com.eg.blps1.dto.ListingAvailableDatesDto;
import com.eg.blps1.mapper.KafkaMapper;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.service.BookingService;
import com.eg.blps1.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
class LandlordDigestDelegate implements JavaDelegate {
    private final BookingService bookingService;
    private final KafkaMapper kafkaMapper;
    private final KafkaService kafkaService;
    private final KafkaProperty kafkaProperty;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Generate landlord digest..");
        LocalDate now = LocalDate.now();
        LocalDate until = now.plusWeeks(4);

        List<Booking> bookings = bookingService.getBookingsBeforeDate(until);
        Map<String, Set<Listing>> landlordListings = new HashMap<>();
        Map<Long, List<Booking>> listingBookings = new HashMap<>();
        for (Booking booking : bookings) {
            landlordListings
                    .computeIfAbsent(booking.getListing().getUsername(), k -> new HashSet<>())
                    .add(booking.getListing());

            listingBookings
                    .computeIfAbsent(booking.getListing().getId(), k -> new ArrayList<>())
                    .add(booking);
        }

        for (String username : landlordListings.keySet()) {
            List<ListingAvailableDatesDto> listingsAvailableDates = new ArrayList<>();

            for (Listing listing : landlordListings.get(username)) {
                Set<LocalDate> bookingDates = new HashSet<>();

                for (Booking booking : listingBookings.get(listing.getId())) {
                    LocalDate date = booking.getStartDate();
                    while (!date.isAfter(booking.getEndDate()) && date.isBefore(until)) {
                        bookingDates.add(date);
                        date = date.plusDays(1);
                    }
                }
                List<LocalDate> availableDates = new ArrayList<>();
                for (LocalDate date = now; date.isBefore(until); date = date.plusDays(1)) {
                    if (!bookingDates.contains(date)) {
                        availableDates.add(date);
                    }
                }
                listingsAvailableDates.add(new ListingAvailableDatesDto(listing.getAddress(), availableDates));
            }
            DigestReportDto digestReportDto = kafkaMapper.mapToDigestReport(username, listingsAvailableDates);
            kafkaService.sendMessage(kafkaProperty.getTopics().getDigestReport().getName(), digestReportDto);
        }
    }
}

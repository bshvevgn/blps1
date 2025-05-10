package com.eg.blps1.mapper;

import com.eg.blps1.dto.BookingReportDto;
import com.eg.blps1.dto.DigestReportDto;
import com.eg.blps1.dto.ListingAvailableDatesDto;
import com.eg.blps1.model.Booking;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class KafkaMapper {

    public DigestReportDto mapToDigestReport(String username, List<ListingAvailableDatesDto> listingsAvailableDates) {
        return new DigestReportDto(username, listingsAvailableDates);
    }

    public BookingReportDto mapToBookingReport(Booking booking) {
        long daysBetween = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;

        return new BookingReportDto(
                booking.getListing().getAddress(),
                booking.getStartDate(),
                booking.getEndDate(),
                BigDecimal.valueOf(daysBetween * booking.getListing().getPrice()),
                booking.getUsername()
        );
    }
}

package com.eg.blps1.mapper;

import com.eg.blps1.client.dto.DebitRequest;
import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.model.Listing;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class BankMapper {

    public DebitRequest mapToDebitRequest(BookingRequest request, Listing listing) {
        long daysBetween = ChronoUnit.DAYS.between(request.startDate(), request.endDate());

        return new DebitRequest(
                BigDecimal.valueOf(daysBetween * listing.getPrice()),
                request.cardNumber(),
                request.expirationDate(),
                request.cvv()
        );
    }
}

package com.eg.blps1.delegate;

import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.service.*;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SaveBookingDelegate implements JavaDelegate {

    private final BookingRepository bookingRepository;
    private final ListingService listingService;
    private final CancelBookingDelegate cancelBookingDelegate;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            Long listingId = Long.parseLong((String) execution.getVariable("listingId"));
            LocalDate startDate = LocalDate.parse((String) execution.getVariable("startDate"));
            LocalDate endDate = LocalDate.parse((String) execution.getVariable("endDate"));
            String cardNumber = (String) execution.getVariable("cardNumber");
            String expirationDate = (String) execution.getVariable("expirationDate");
            String cvv = (String) execution.getVariable("cvv");
            String username = (String) execution.getVariable("username");

            BookingRequest request = new BookingRequest(listingId, startDate, endDate, cardNumber, expirationDate, cvv);
            Listing listing = listingService.findById(request.listingId());

            Booking booking = new Booking(username, listing, request.startDate(), request.endDate());
            booking = bookingRepository.save(booking);

            execution.setVariable("booking", booking);
            execution.setVariable("bookingId", booking.getId());

        } catch (Exception e) {
            try {
                cancelBookingDelegate.execute(execution);
            } catch (Exception cancelError) {
                System.err.println("Ошибка при отмене бронирования: " + cancelError.getMessage());
            }

            throw new BpmnError("saveBookingFailed", "Ошибка при сохранении бронирования: " + e.getMessage());
        }
    }
}
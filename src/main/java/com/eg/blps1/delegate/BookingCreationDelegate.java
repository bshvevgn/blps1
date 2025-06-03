package com.eg.blps1.delegate;

import com.eg.blps1.dto.BookingRequest;
import com.eg.blps1.model.Listing;
import com.eg.blps1.repository.BookingRepository;
import com.eg.blps1.service.ListingService;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eg.blps1.delegate.util.DateConverter.convertToLocalDate;

@Component
@RequiredArgsConstructor
public class BookingCreationDelegate implements JavaDelegate {

    private final BookingRepository bookingRepository;
    private final ListingService listingService;
    private final Validator validator;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Long listingId = Long.parseLong((String) execution.getVariable("listingId"));
            LocalDate startDate = convertToLocalDate(execution.getVariable("startDate"), formatter);
            LocalDate endDate = convertToLocalDate(execution.getVariable("endDate"), formatter);
            String cardNumber = (String) execution.getVariable("cardNumber");
            String expirationDate = (String) execution.getVariable("expirationDate");
            String cvv = (String) execution.getVariable("cvv");

            BookingRequest bookingData = new BookingRequest(listingId, startDate, endDate, cardNumber, expirationDate, cvv);

            Set<ConstraintViolation<BookingRequest>> violations = validator.validate(bookingData);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("; "));
                throw new BpmnError("validationError", errorMessage);
            }

            Listing listing = listingService.findById(listingId);
            boolean isConflict = bookingRepository.existsByListingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    listing, endDate, startDate
            );
            if (isConflict) throw new BpmnError("bookingConflictError", "Конфликт бронирования, выберите другие даты");

        } catch (NumberFormatException | DateTimeParseException e) {
            throw new BpmnError("validationError", "Проверьте введённые данные");
        } catch (Exception e) {
            throw new BpmnError("bookingError", e.getMessage());
        }
    }
}


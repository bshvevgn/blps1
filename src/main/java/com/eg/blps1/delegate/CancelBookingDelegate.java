package com.eg.blps1.delegate;

import com.eg.blps1.model.Booking;
import com.eg.blps1.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelBookingDelegate implements JavaDelegate {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void execute(DelegateExecution execution) {
        Long bookingId = (Long) execution.getVariable("bookingId");

        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new BpmnError("notFound", "Бронирование не найдено"));

            bookingRepository.delete(booking);
        } catch (Exception e) {
            throw new BpmnError("dbError", "Ошибка отмены бронирования: " + e.getMessage());
        }
    }
}

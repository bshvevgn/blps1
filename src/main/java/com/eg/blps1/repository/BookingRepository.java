package com.eg.blps1.repository;

import com.eg.blps1.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Можно добавить методы для поиска бронирований, например, по пользователю или объявлению
}

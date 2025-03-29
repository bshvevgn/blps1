package com.eg.blps1.repository;

import com.eg.blps1.model.Booking;
import com.eg.blps1.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByListing(Listing listing);

    boolean existsByListingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Listing listing, LocalDate endDate, LocalDate startDate);
}

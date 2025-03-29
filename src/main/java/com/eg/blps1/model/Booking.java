package com.eg.blps1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    private LocalDate startDate;
    private LocalDate endDate;

    public Booking(String username, Listing listing, LocalDate startDate, LocalDate endDate) {
        this.username = username;
        this.listing = listing;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

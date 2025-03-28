package com.eg.blps1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "listing")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private double price;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Listing(String address, double price, String note, User user) {
        this.address = address;
        this.price = price;
        this.note = note;
        this.user = user;
    }
}

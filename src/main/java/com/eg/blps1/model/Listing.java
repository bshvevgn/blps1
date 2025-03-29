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
    private String username;

    public Listing(String address, double price, String note, String username) {
        this.address = address;
        this.price = price;
        this.note = note;
        this.username = username;
    }
}

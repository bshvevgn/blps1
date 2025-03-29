package com.eg.blps1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "sanctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String reason;
    private Instant expiresAt;

    public Sanction(String username, String reason, Instant expiresAt) {
        this.username = username;
        this.reason = reason;
        this.expiresAt = expiresAt;
    }
}

package com.eg.blps1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String reason;
    private Instant expiresAt;

    public Sanction(User user, String reason, Instant expiresAt) {
        this.user = user;
        this.reason = reason;
        this.expiresAt = expiresAt;
    }
}

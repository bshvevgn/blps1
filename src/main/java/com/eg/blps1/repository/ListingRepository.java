package com.eg.blps1.repository;

import com.eg.blps1.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    // Можно добавить методы для поиска объявлений по различным критериям
}

package com.eg.blps1.exceptions;

public class ListingNotFoundException extends CustomException {
    public ListingNotFoundException(Long listingId) {
        super("Объявления таким id={" + listingId + "} не найдено");
    }
}

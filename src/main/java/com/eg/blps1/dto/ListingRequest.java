package com.eg.blps1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingRequest {
    private String address;
    private double price;
    private String note;
}

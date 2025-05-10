package com.eg.blps1.dto;

import java.util.List;

public record DigestReportDto(
        String email,
        List<ListingAvailableDatesDto> listings
) {}

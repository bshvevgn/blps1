package com.eg.blps1.mapper;

import com.eg.blps1.dto.ComplaintResponse;
import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.dto.ListingResponse;
import com.eg.blps1.model.Complaint;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListingMapper {

    public Listing mapToEntity(ListingRequest request, User user) {
        return new Listing(request.address(), request.price(), request.note(), user.getUsername());
    }

    public ListingResponse mapToListingResponse(Listing listing) {
        return new ListingResponse(
                listing.getId(),
                listing.getAddress(),
                listing.getPrice(),
                listing.getNote()
        );
    }

    public List<ListingResponse> mapToListListing(List<Listing> listings) {
        return listings.stream().map(this::mapToListingResponse).toList();
    }
}

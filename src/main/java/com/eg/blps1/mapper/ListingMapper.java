package com.eg.blps1.mapper;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListingMapper {

    public Listing mapToEntity(ListingRequest request, User user) {
        return new Listing(request.address(), request.price(), request.note(), user.getUsername());
    }
}

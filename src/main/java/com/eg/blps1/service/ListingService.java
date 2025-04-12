package com.eg.blps1.service;

import com.eg.blps1.dto.ListingRequest;
import com.eg.blps1.exceptions.ActiveSanctionException;
import com.eg.blps1.exceptions.ListingNotFoundException;
import com.eg.blps1.mapper.ListingMapper;
import com.eg.blps1.model.Listing;
import com.eg.blps1.model.User;
import com.eg.blps1.repository.ListingRepository;
import com.eg.blps1.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {
    private final ListingMapper listingMapper;
    private final SanctionService sanctionService;
    private final ListingRepository listingRepository;

    public Listing create(ListingRequest request) {
        User user = CommonUtils.getUserFromSecurityContext();
        if (sanctionService.hasActiveSanction(user)) throw new ActiveSanctionException("Вы не можете размещать объявления из-за действующей санкции.");

        Listing listing = listingMapper.mapToEntity(request, user);
        return listingRepository.save(listing);
    }

    public List<Listing> getAll(){
        return listingRepository.findAll();
    }

    public Listing findById(Long listingId) {
        return listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));
    }
}

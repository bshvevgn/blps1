package com.eg.blps1.delegate.util;

import com.eg.blps1.model.RoleEnum;

import java.util.EnumSet;
import java.util.Map;

public class PoolAccessMap {
    public static final Map<String, EnumSet<RoleEnum>> POOL_ACCESS = Map.of(
        "listing_creation_process", EnumSet.of(RoleEnum.ROLE_LANDLORD, RoleEnum.ROLE_ADMIN),
        "booking_creation_process", EnumSet.of(RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN),
        "complaint_application_creation_process", EnumSet.of(RoleEnum.ROLE_USER, RoleEnum.ROLE_LANDLORD, RoleEnum.ROLE_ADMIN),
        "impose_sanction_process", EnumSet.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR),
        "remove_sanction_process", EnumSet.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR),
        "get_listings_process", EnumSet.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR, RoleEnum.ROLE_LANDLORD, RoleEnum.ROLE_USER),
        "get_complaints_process", EnumSet.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_MODERATOR)
    );
}

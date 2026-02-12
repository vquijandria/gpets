package com.gpets.gpetsapi.dto;

public record PetResponse(
        String id,
        String name,
        String species,
        String status,
        String ownerId,
        LastLocationResponse lastLocation
) {
    public record LastLocationResponse(
            Double lat,
            Double lng,
            Integer accuracy,
            Long updatedAt
    ) {}
}

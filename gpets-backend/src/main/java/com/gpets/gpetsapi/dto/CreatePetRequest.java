package com.gpets.gpetsapi.dto;

public record CreatePetRequest(
        String name,
        String species,
        String status,
        Double lat,
        Double lng,
        Integer accuracy
) {}

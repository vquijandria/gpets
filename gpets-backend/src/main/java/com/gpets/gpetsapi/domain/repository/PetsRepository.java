package com.gpets.gpetsapi.domain.repository;

import com.gpets.gpetsapi.domain.model.Pet;

import java.util.Map;
import java.util.Optional;

public interface PetsRepository {
    Map<String, Pet> findAll() throws Exception;
    Optional<Pet> findById(String petId) throws Exception;

    void updateLocation(String petId, double lat, double lng, long now) throws Exception;
}

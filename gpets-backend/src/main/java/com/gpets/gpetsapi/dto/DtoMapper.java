package com.gpets.gpetsapi.dto;

import com.gpets.gpetsapi.domain.model.Owner;
import com.gpets.gpetsapi.domain.model.Pet;

public class DtoMapper {

    public static PetSummaryResponse toPetSummary(String id, Pet pet) {
        PetSummaryResponse dto = new PetSummaryResponse();
        dto.id = id;
        dto.name = pet.name();
        dto.species = pet.species().name();
        dto.status = pet.status().name();
        dto.ownerId = pet.ownerId();

        if (pet.lastLocation() != null) {
            PetSummaryResponse.LocationResponse loc = new PetSummaryResponse.LocationResponse();
            loc.lat = pet.lastLocation().point().lat();
            loc.lng = pet.lastLocation().point().lng();
            loc.updatedAt = pet.lastLocation().updatedAt();
            dto.lastLocation = loc;
        }

        return dto;
    }

    public static PetDetailResponse toPetDetail(String id, Pet pet) {
        PetDetailResponse dto = new PetDetailResponse();
        dto.id = id;
        dto.name = pet.name();
        dto.species = pet.species().name();
        dto.breed = pet.breed();
        dto.photoUrl = pet.photoUrl();
        dto.status = pet.status().name();
        dto.ownerId = pet.ownerId();
        dto.createdAt = pet.createdAt();
        dto.updatedAt = pet.updatedAt();

        if (pet.lastLocation() != null) {
            PetDetailResponse.LocationResponse loc = new PetDetailResponse.LocationResponse();
            loc.lat = pet.lastLocation().point().lat();
            loc.lng = pet.lastLocation().point().lng();
            loc.updatedAt = pet.lastLocation().updatedAt();
            dto.lastLocation = loc;
        }

        return dto;
    }

    public static OwnerResponse toOwner(Owner owner) {
        OwnerResponse dto = new OwnerResponse();
        dto.uid = owner.uid();
        dto.fullName = owner.fullName();
        dto.email = owner.email();
        dto.phone = owner.phone();
        dto.address = owner.address();
        dto.createdAt = owner.createdAt();
        dto.updatedAt = owner.updatedAt();
        return dto;
    }
}

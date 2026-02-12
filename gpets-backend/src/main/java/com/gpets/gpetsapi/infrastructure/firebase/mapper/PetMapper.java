package com.gpets.gpetsapi.infrastructure.firebase.mapper;

/*
Este mapper convierte entre PetRecord (persistencia en Firebase Realtime Database) y Pet (modelo de dominio).
Se usa por los repositorios Firebase para transformar los datos leídos/escritos en /pets/{petId}.
Mapea los campos vigentes: id, name, species, status, ownerId y lastLocation (lat, lng, updatedAt).
*/

import com.gpets.gpetsapi.domain.model.Pet;
import com.gpets.gpetsapi.domain.model.PetSpecies;
import com.gpets.gpetsapi.domain.model.PetStatus;
import com.gpets.gpetsapi.infrastructure.firebase.record.PetRecord;

public class PetMapper {

    public static Pet toDomain(String id, PetRecord r) {
        if (r == null) return null;

        long now = System.currentTimeMillis();

        PetSpecies species = safeSpecies(r.species);
        Pet pet = new Pet(id, r.name, species, now);

        if (r.status != null) {
            pet.changeStatus(safeStatus(r.status), now);
        }

        if (r.ownerId != null && !r.ownerId.isBlank()) {
            pet.assignOwner(r.ownerId, now);
        }

        if (r.lastLocation != null && r.lastLocation.lat != null && r.lastLocation.lng != null) {
            long locAt = r.lastLocation.updatedAt != null ? r.lastLocation.updatedAt : now;



            pet.updateLocation(r.lastLocation.lat, r.lastLocation.lng, locAt);
        }

        return pet;
    }

    public static PetRecord toRecord(Pet pet) {
        PetRecord r = new PetRecord();
        r.id = pet.id();
        r.name = pet.name();
        r.species = pet.species() != null ? pet.species().name() : PetSpecies.OTHER.name();
        r.status = pet.status() != null ? pet.status().name() : PetStatus.ACTIVE.name();
        r.ownerId = pet.ownerId();

        if (pet.lastLocation() != null) {
            PetRecord.LastLocationRecord ll = new PetRecord.LastLocationRecord();
            ll.lat = pet.lastLocation().point().lat();
            ll.lng = pet.lastLocation().point().lng();
            ll.updatedAt = pet.lastLocation().updatedAt();
            r.lastLocation = ll;
        }

        return r;
    }

    private static PetSpecies safeSpecies(String s) {
        try {
            return s == null ? PetSpecies.OTHER : PetSpecies.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return PetSpecies.OTHER;
        }
    }

    private static PetStatus safeStatus(String s) {
        try {
            return s == null ? PetStatus.ACTIVE : PetStatus.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return PetStatus.ACTIVE;
        }
    }
}

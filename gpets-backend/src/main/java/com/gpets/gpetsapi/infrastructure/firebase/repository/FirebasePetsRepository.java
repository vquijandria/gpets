package com.gpets.gpetsapi.infrastructure.firebase.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.gpets.gpetsapi.domain.model.Pet;
import com.gpets.gpetsapi.domain.repository.PetsRepository;
import com.gpets.gpetsapi.infrastructure.firebase.mapper.PetMapper;
import com.gpets.gpetsapi.infrastructure.firebase.record.PetRecord;
import com.gpets.gpetsapi.infrastructure.firebase.service.RealtimeDbService;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FirebasePetsRepository implements PetsRepository {

    private final RealtimeDbService db;

    public FirebasePetsRepository(RealtimeDbService db) {
        this.db = db;
    }

    @Override
    public Map<String, Pet> findAll() throws Exception {

        GenericTypeIndicator<Map<String, PetRecord>> t = new GenericTypeIndicator<>() {};
        Map<String, PetRecord> raw = db.get("/pets", t);

        if (raw == null) return Map.of();

        Map<String, Pet> result = new LinkedHashMap<>();
        for (var entry : raw.entrySet()) {
            String petId = entry.getKey();
            PetRecord record = entry.getValue();
            Pet pet = PetMapper.toDomain(petId, record);
            if (pet != null) result.put(petId, pet);
        }

        return result;
    }

    @Override
    public Optional<Pet> findById(String petId) throws Exception {
        PetRecord record = db.get("/pets/" + petId, PetRecord.class);
        return Optional.ofNullable(PetMapper.toDomain(petId, record));
    }

    @Override
    public void updateLocation(String petId, double lat, double lng, long now) throws Exception {

        Map<String, Object> lastLoc = new HashMap<>();
        lastLoc.put("lat", lat);
        lastLoc.put("lng", lng);
        lastLoc.put("updatedAt", now);

        Map<String, Object> updates = new HashMap<>();
        updates.put("lastLocation", lastLoc);

        db.update("/pets/" + petId, updates);


        Map<String, Object> history = new HashMap<>();
        history.put("lat", lat);
        history.put("lng", lng);
        history.put("createdAt", now);

        DatabaseReference pushed = db.push("/petLocations/" + petId);
        pushed.setValueAsync(history);
    }


    public Pet createForOwner(String ownerUid, String name, String species, String status,
                              double lat, double lng, long now) throws Exception {

        String petId = "pet_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        PetRecord record = new PetRecord();
        record.id = petId;
        record.name = name;
        record.species = species;
        record.status = status;
        record.ownerId = ownerUid;

        PetRecord.LastLocationRecord loc = new PetRecord.LastLocationRecord();
        loc.lat = lat;
        loc.lng = lng;
        loc.updatedAt = now;
        record.lastLocation = loc;

        db.set("/pets/" + petId, record);


        db.set("/petsByOwner/" + ownerUid + "/" + petId, true);

        return PetMapper.toDomain(petId, record);
    }


    public Pet claimPet(String ownerUid, String petId) throws Exception {
        PetRecord record = db.get("/pets/" + petId, PetRecord.class);
        if (record == null) return null;

        record.ownerId = ownerUid;


        db.update("/pets/" + petId, Map.of("ownerId", ownerUid));


        db.set("/petsByOwner/" + ownerUid + "/" + petId, true);

        return PetMapper.toDomain(petId, record);
    }


    public Optional<PetRecord> findRecordById(String petId) throws Exception {
        return Optional.ofNullable(db.get("/pets/" + petId, PetRecord.class));
    }
}

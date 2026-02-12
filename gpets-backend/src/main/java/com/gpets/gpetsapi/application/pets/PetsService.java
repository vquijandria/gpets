package com.gpets.gpetsapi.application.pets;

/*
Este servicio de aplicación orquesta los casos de uso del bounded context "Pets".
Se comunica con:
- PetsRepository (capa domain): lectura de mascotas, detalle y actualización de ubicación.
- FirebasePetsRepository (capa infrastructure): operaciones específicas de Firebase RTDB para crear mascotas y reclamar/vincular mascotas, además de leer el PetRecord crudo para validar ownership.
- RealtimeDbService (capa infrastructure): lectura directa de /owners/{uid} para asegurar que el usuario autenticado tenga perfil de owner registrado (requisito de negocio).
Reglas clave:
- ownerId de una mascota SIEMPRE es el UID del usuario autenticado (nunca se acepta ownerId desde el frontend).
- Para crear una mascota: el owner debe estar registrado y el pet queda asociado al uid automáticamente.
- Para reclamar/vincular una mascota: si ya tiene owner distinto al uid, se rechaza; si no tiene owner o ya es del uid, se asocia.
*/

import com.gpets.gpetsapi.domain.model.Pet;
import com.gpets.gpetsapi.domain.repository.PetsRepository;
import com.gpets.gpetsapi.dto.LocationUpdateRequest;
import com.gpets.gpetsapi.infrastructure.firebase.record.PetRecord;
import com.gpets.gpetsapi.infrastructure.firebase.repository.FirebasePetsRepository;
import com.gpets.gpetsapi.infrastructure.firebase.service.RealtimeDbService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PetsService {

    private final PetsRepository petsRepository;
    private final FirebasePetsRepository firebasePetsRepository;
    private final RealtimeDbService db;

    public PetsService(PetsRepository petsRepository, FirebasePetsRepository firebasePetsRepository, RealtimeDbService db) {
        this.petsRepository = petsRepository;
        this.firebasePetsRepository = firebasePetsRepository;
        this.db = db;
    }

    public Map<String, Pet> list(String species, String status, String ownerId) throws Exception {
        Map<String, Pet> all = petsRepository.findAll();

        species = normalize(species);
        status = normalize(status);
        ownerId = normalize(ownerId);

        Map<String, Pet> result = new LinkedHashMap<>();
        for (var e : all.entrySet()) {
            Pet p = e.getValue();

            if (species != null && p.species() != null && !p.species().name().equalsIgnoreCase(species)) continue;
            if (status != null && p.status() != null && !p.status().name().equalsIgnoreCase(status)) continue;
            if (ownerId != null && (p.ownerId() == null || !p.ownerId().equals(ownerId))) continue;

            result.put(e.getKey(), p);
        }
        return result;
    }

    public Pet detail(String id) throws Exception {
        return petsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("pet_not_found"));
    }

    public Map<String, Object> updateLocation(String petId, LocationUpdateRequest body) throws Exception {
        petsRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("pet_not_found"));

        long now = Instant.now().toEpochMilli();
        petsRepository.updateLocation(petId, body.lat, body.lng, now);

        return Map.of("ok", true, "updatedAt", now);
    }

    public Pet createForOwner(String ownerUid, String name, String species, String status,
                              double lat, double lng, Integer accuracy) throws Exception {

        ensureOwnerRegistered(ownerUid);

        long now = Instant.now().toEpochMilli();
        return firebasePetsRepository.createForOwner(ownerUid, name, species, status, lat, lng, now);
    }

    public Pet claim(String ownerUid, String petId) throws Exception {
        ensureOwnerRegistered(ownerUid);

        PetRecord record = firebasePetsRepository.findRecordById(petId)
                .orElseThrow(() -> new NoSuchElementException("pet_not_found"));

        if (record.ownerId != null && !record.ownerId.isBlank() && !record.ownerId.equals(ownerUid)) {
            throw new IllegalStateException("pet_already_claimed");
        }

        Pet updated = firebasePetsRepository.claimPet(ownerUid, petId);
        if (updated == null) throw new NoSuchElementException("pet_not_found");
        return updated;
    }

    private void ensureOwnerRegistered(String uid) throws Exception {
        Object owner = db.get("/owners/" + uid, Object.class);
        if (owner == null) throw new IllegalStateException("owner_not_registered");
    }

    private String normalize(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;

        if (s.equalsIgnoreCase("null")) return null;
        if (s.equalsIgnoreCase("undefined")) return null;
        if (s.equalsIgnoreCase("ownerId")) return null;
        if (s.equalsIgnoreCase("species")) return null;
        if (s.equalsIgnoreCase("status")) return null;

        return s;
    }
}

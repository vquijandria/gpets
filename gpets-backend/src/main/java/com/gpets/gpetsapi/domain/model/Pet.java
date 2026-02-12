package com.gpets.gpetsapi.domain.model;

import com.gpets.gpetsapi.domain.valueobject.GeoPoint;
import com.gpets.gpetsapi.domain.valueobject.PetLocation;

public class Pet {

    private final String id;
    private String name;
    private PetSpecies species;
    private String breed;
    private String photoUrl;
    private PetStatus status;
    private String ownerId; // uid o null
    private PetLocation lastLocation;
    private final long createdAt;
    private long updatedAt;

    public Pet(String id, String name, PetSpecies species, long createdAt) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("pet_id_required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("pet_name_required");
        if (species == null) throw new IllegalArgumentException("pet_species_required");
        if (createdAt <= 0) throw new IllegalArgumentException("createdAt_required");

        this.id = id;
        this.name = name;
        this.species = species;
        this.status = PetStatus.ACTIVE;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public void changeStatus(PetStatus status, long now) {
        if (status == null) throw new IllegalArgumentException("status_required");
        touch(now);
        this.status = status;
    }

    public void assignOwner(String ownerId, long now) {
        touch(now);
        this.ownerId = (ownerId != null && ownerId.isBlank()) ? null : ownerId;
    }

    public void updateLocation(double lat, double lng, long now) {
        touch(now);
        this.lastLocation = new PetLocation(
                new GeoPoint(lat, lng),
                now
        );
    }

    private void touch(long now) {
        if (now <= 0) throw new IllegalArgumentException("now_required");
        this.updatedAt = now;
    }

    // Getters
    public String id() { return id; }
    public String name() { return name; }
    public PetSpecies species() { return species; }
    public String breed() { return breed; }
    public String photoUrl() { return photoUrl; }
    public PetStatus status() { return status; }
    public String ownerId() { return ownerId; }
    public PetLocation lastLocation() { return lastLocation; }
    public long createdAt() { return createdAt; }
    public long updatedAt() { return updatedAt; }

    public void setBreed(String breed, long now) {
        touch(now);
        this.breed = breed;
    }

    public void setPhotoUrl(String photoUrl, long now) {
        touch(now);
        this.photoUrl = photoUrl;
    }
}

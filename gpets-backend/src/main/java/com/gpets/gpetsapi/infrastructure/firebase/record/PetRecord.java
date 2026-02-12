package com.gpets.gpetsapi.infrastructure.firebase.record;

public class PetRecord {

    public String id;
    public String name;
    public String species;
    public String status;


    public String ownerId;

    public LastLocationRecord lastLocation;

    public PetRecord() {

    }

    public static class LastLocationRecord {

        public Double lat;
        public Double lng;
        public Long updatedAt;

        public LastLocationRecord() {

        }
    }
}

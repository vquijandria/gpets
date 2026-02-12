package com.gpets.gpetsapi.dto;

public class PetDetailResponse {
    public String id;
    public String name;
    public String species;
    public String breed;
    public String photoUrl;
    public String status;
    public String ownerId;
    public LocationResponse lastLocation;
    public Long createdAt;
    public Long updatedAt;

    public static class LocationResponse {
        public Double lat;
        public Double lng;
        public Double accuracy;
        public Long updatedAt;
    }
}

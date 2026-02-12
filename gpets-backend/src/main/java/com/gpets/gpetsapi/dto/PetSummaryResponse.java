package com.gpets.gpetsapi.dto;

public class PetSummaryResponse {
    public String id;
    public String name;
    public String species;
    public String status;
    public String ownerId;
    public LocationResponse lastLocation;

    public static class LocationResponse {
        public Double lat;
        public Double lng;
        public Double accuracy;
        public Long updatedAt;
    }
}

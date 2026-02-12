package com.gpets.gpetsapi.dto;

import jakarta.validation.constraints.NotNull;

public class LocationUpdateRequest {

    @NotNull(message = "lat_is_required")
    public Double lat;

    @NotNull(message = "lng_is_required")
    public Double lng;

}

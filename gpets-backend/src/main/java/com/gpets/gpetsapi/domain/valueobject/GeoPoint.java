package com.gpets.gpetsapi.domain.valueobject;

public class GeoPoint {
    private final double lat;
    private final double lng;

    public GeoPoint(double lat, double lng) {
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("lat_out_of_range");
        if (lng < -180 || lng > 180) throw new IllegalArgumentException("lng_out_of_range");
        this.lat = lat;
        this.lng = lng;
    }

    public double lat() { return lat; }
    public double lng() { return lng; }
}

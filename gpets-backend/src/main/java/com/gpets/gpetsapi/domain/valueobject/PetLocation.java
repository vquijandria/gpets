package com.gpets.gpetsapi.domain.valueobject;

public class PetLocation {
    private final GeoPoint point;
    private final long updatedAt;

    public PetLocation(GeoPoint point, long updatedAt) {
        if (point == null) throw new IllegalArgumentException("point_required");
        if (updatedAt <= 0) throw new IllegalArgumentException("updatedAt_required");
        this.point = point;
        this.updatedAt = updatedAt;
    }

    public GeoPoint point() { return point; }
    public long updatedAt() { return updatedAt; }
}

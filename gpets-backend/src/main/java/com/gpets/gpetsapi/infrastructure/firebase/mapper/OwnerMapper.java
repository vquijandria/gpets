package com.gpets.gpetsapi.infrastructure.firebase.mapper;

import com.gpets.gpetsapi.domain.model.Owner;
import com.gpets.gpetsapi.infrastructure.firebase.record.OwnerRecord;

public class OwnerMapper {

    public static Owner toDomain(OwnerRecord r) {
        if (r == null) return null;

        long createdAt = r.createdAt != null ? r.createdAt : System.currentTimeMillis();


        Owner owner = new Owner(r.uid, r.email, createdAt);


        if (r.fullName != null && r.phone != null && r.address != null) {
            long now = r.updatedAt != null ? r.updatedAt : createdAt;
            owner.completeProfile(r.fullName, r.phone, r.address, now);
        }

        return owner;
    }

    public static OwnerRecord toRecord(Owner o) {
        OwnerRecord r = new OwnerRecord();

        r.uid = o.uid();
        r.email = o.email();
        r.fullName = o.fullName();
        r.phone = o.phone();
        r.address = o.address();
        r.createdAt = o.createdAt();
        r.updatedAt = o.updatedAt();
        return r;
    }
}

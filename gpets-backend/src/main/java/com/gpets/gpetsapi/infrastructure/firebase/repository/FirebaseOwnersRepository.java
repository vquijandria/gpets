package com.gpets.gpetsapi.infrastructure.firebase.repository;

import com.gpets.gpetsapi.domain.model.Owner;
import com.gpets.gpetsapi.domain.repository.OwnersRepository;
import com.gpets.gpetsapi.infrastructure.firebase.mapper.OwnerMapper;
import com.gpets.gpetsapi.infrastructure.firebase.record.OwnerRecord;
import com.gpets.gpetsapi.infrastructure.firebase.service.RealtimeDbService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FirebaseOwnersRepository implements OwnersRepository {

    private final RealtimeDbService db;

    public FirebaseOwnersRepository(RealtimeDbService db) {
        this.db = db;
    }

    @Override
    public void save(Owner owner) throws Exception {
        OwnerRecord record = OwnerMapper.toRecord(owner);
        db.set("/owners/" + owner.uid(), record);
    }

    @Override
    public Optional<Owner> findByUid(String uid) throws Exception {
        OwnerRecord record = db.get("/owners/" + uid, OwnerRecord.class);
        return Optional.ofNullable(OwnerMapper.toDomain(record));
    }
}

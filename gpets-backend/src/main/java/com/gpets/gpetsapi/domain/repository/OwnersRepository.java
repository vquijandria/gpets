package com.gpets.gpetsapi.domain.repository;

import com.gpets.gpetsapi.domain.model.Owner;

import java.util.Optional;

public interface OwnersRepository {
    void save(Owner owner) throws Exception;
    Optional<Owner> findByUid(String uid) throws Exception;
}

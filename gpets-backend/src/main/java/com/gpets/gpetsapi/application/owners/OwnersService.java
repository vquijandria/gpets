package com.gpets.gpetsapi.application.owners;

import com.gpets.gpetsapi.domain.model.Owner;
import com.gpets.gpetsapi.domain.repository.OwnersRepository;
import com.gpets.gpetsapi.dto.OwnerCreateRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
public class OwnersService {

    private final OwnersRepository ownersRepository;

    public OwnersService(OwnersRepository ownersRepository) {
        this.ownersRepository = ownersRepository;
    }

    public Owner register(String uid, String email, String nameFromToken, OwnerCreateRequest body) throws Exception {
        long now = Instant.now().toEpochMilli();


        Owner owner = new Owner(uid, email, now);

        String fullName = (body.fullName != null && !body.fullName.isBlank())
                ? body.fullName
                : nameFromToken;


        owner.completeProfile(fullName, body.phone, body.address, now);

        ownersRepository.save(owner);
        return owner;
    }

    public Owner me(String uid) throws Exception {
        return ownersRepository.findByUid(uid)
                .orElseThrow(() -> new NoSuchElementException("owner_not_registered"));
    }
}

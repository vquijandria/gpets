package com.gpets.gpetsapi.controller;

import com.gpets.gpetsapi.application.owners.OwnersService;
import com.gpets.gpetsapi.dto.OwnerCreateRequest;
import com.gpets.gpetsapi.dto.OwnerResponse;
import com.gpets.gpetsapi.dto.DtoMapper;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
public class OwnersController {

    private final OwnersService ownersService;

    public OwnersController(OwnersService ownersService) {
        this.ownersService = ownersService;
    }

    @PostMapping
    public OwnerResponse register(
            @Valid @RequestBody OwnerCreateRequest body,
            HttpServletRequest request
    ) throws Exception {

        String uid = (String) request.getAttribute("uid");
        String email = (String) request.getAttribute("email");
        String name = (String) request.getAttribute("name");

        var owner = ownersService.register(uid, email, name, body);
        return DtoMapper.toOwner(owner);
    }

    @GetMapping("/me")
    public OwnerResponse me(HttpServletRequest request) throws Exception {
        String uid = (String) request.getAttribute("uid");
        var owner = ownersService.me(uid);
        return DtoMapper.toOwner(owner);
    }
}

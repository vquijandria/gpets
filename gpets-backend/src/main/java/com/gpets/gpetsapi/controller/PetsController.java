package com.gpets.gpetsapi.controller;

/*
Este controller expone los endpoints REST del bounded context "Pets".
Se comunica exclusivamente con la capa de aplicación (PetsService),
la cual encapsula la lógica de negocio y coordina el acceso a repositorios.
Responsabilidades:
- Listar y obtener detalle de mascotas.
- Actualizar ubicación.
- Crear nuevas mascotas bajo el owner autenticado.
- Permitir reclamar/vincular una mascota existente al owner autenticado.
El UID del usuario autenticado se obtiene desde Authentication (inyectado por Spring Security),
garantizando que ownerId siempre sea el UID del token y nunca provenga del frontend.
*/

import com.gpets.gpetsapi.application.pets.PetsService;
import com.gpets.gpetsapi.dto.DtoMapper;
import com.gpets.gpetsapi.dto.LocationUpdateRequest;
import com.gpets.gpetsapi.dto.PetDetailResponse;
import com.gpets.gpetsapi.dto.PetSummaryResponse;
import com.gpets.gpetsapi.dto.CreatePetRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pets")
public class PetsController {

    private final PetsService petsService;

    public PetsController(PetsService petsService) {
        this.petsService = petsService;
    }

    @GetMapping
    public Map<String, PetSummaryResponse> list(
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ownerId
    ) throws Exception {

        var pets = petsService.list(species, status, ownerId);

        Map<String, PetSummaryResponse> out = new LinkedHashMap<>();
        for (var entry : pets.entrySet()) {
            String petId = entry.getKey();
            out.put(petId, DtoMapper.toPetSummary(petId, entry.getValue()));
        }

        return out;
    }

    @GetMapping("/{id}")
    public PetDetailResponse detail(@PathVariable String id) throws Exception {
        var pet = petsService.detail(id);
        return DtoMapper.toPetDetail(id, pet);
    }

    @PostMapping("/{id}/location")
    public Map<String, Object> updateLocation(
            @PathVariable String id,
            @Valid @RequestBody LocationUpdateRequest body
    ) throws Exception {
        return petsService.updateLocation(id, body);
    }

    @PostMapping
    public ResponseEntity<?> create(
            Authentication authentication,
            @Valid @RequestBody CreatePetRequest body
    ) throws Exception {

        String uid = (String) authentication.getPrincipal();

        try {
            var pet = petsService.createForOwner(
                    uid,
                    body.name(),
                    body.species(),
                    body.status(),
                    body.lat(),
                    body.lng(),
                    body.accuracy()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(DtoMapper.toPetDetail(pet.id(), pet));

        } catch (IllegalStateException e) {
            if ("owner_not_registered".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "owner_not_registered"));
            }
            if ("pet_already_claimed".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "pet_already_claimed"));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<?> claim(
            Authentication authentication,
            @PathVariable String id
    ) throws Exception {

        String uid = (String) authentication.getPrincipal();

        try {
            var pet = petsService.claim(uid, id);
            return ResponseEntity.ok(DtoMapper.toPetDetail(id, pet));

        } catch (NoSuchElementException e) {
            if ("pet_not_found".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "pet_not_found"));
            }
            throw e;
        } catch (IllegalStateException e) {
            if ("owner_not_registered".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "owner_not_registered"));
            }
            if ("pet_already_claimed".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "pet_already_claimed"));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

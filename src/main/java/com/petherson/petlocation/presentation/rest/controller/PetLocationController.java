package com.petherson.petlocation.presentation.rest.controller;

import com.petherson.petlocation.application.port.in.ResolvePetLocationUseCase;
import com.petherson.petlocation.domain.model.PetLocation;
import com.petherson.petlocation.presentation.rest.dto.PetLocationRequest;
import com.petherson.petlocation.presentation.rest.dto.PetLocationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pets/locations")
@RequiredArgsConstructor
@Tag(name = "Pet Location", description = "Endpoints for pet location tracking")
public class PetLocationController {

    private final ResolvePetLocationUseCase resolvePetLocationUseCase;

    @Operation(summary = "Receive and resolve pet location")
    @PostMapping
    public ResponseEntity<PetLocationResponse> receiveLocation(@Valid @RequestBody PetLocationRequest request) {
        PetLocation domainModel = PetLocation.builder()
                .sensorId(request.getSensorId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .timestamp(request.getTimestamp())
                .build();

        PetLocation resolved = resolvePetLocationUseCase.execute(domainModel);

        return ResponseEntity.ok(mapToResponse(resolved));
    }

    private PetLocationResponse mapToResponse(PetLocation petLocation) {
        return PetLocationResponse.builder()
                .sensorId(petLocation.getSensorId())
                .timestamp(petLocation.getTimestamp())
                .coordinates(PetLocationResponse.Coordinates.builder()
                        .latitude(petLocation.getLatitude())
                        .longitude(petLocation.getLongitude())
                        .build())
                .location(PetLocationResponse.Location.builder()
                        .country(petLocation.getCountry())
                        .state(petLocation.getState())
                        .city(petLocation.getCity())
                        .neighborhood(petLocation.getNeighborhood())
                        .streetAddress(petLocation.getStreetAddress())
                        .build())
                .provider(petLocation.getProvider())
                .build();
    }
}

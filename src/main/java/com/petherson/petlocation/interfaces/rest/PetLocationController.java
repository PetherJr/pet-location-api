package com.petherson.petlocation.interfaces.rest;

import com.petherson.petlocation.application.port.in.ReceiveLocationUseCase;
import com.petherson.petlocation.domain.model.Address;
import com.petherson.petlocation.domain.model.Coordinate;
import com.petherson.petlocation.domain.model.Pet;
import com.petherson.petlocation.interfaces.rest.dto.LocationRequestDTO;
import com.petherson.petlocation.interfaces.rest.dto.PetLocationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pets")
public class PetLocationController {

    private final ReceiveLocationUseCase receiveLocationUseCase;

    public PetLocationController(ReceiveLocationUseCase receiveLocationUseCase) {
        this.receiveLocationUseCase = receiveLocationUseCase;
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<PetLocationResponseDTO> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody LocationRequestDTO request) {
        
        Coordinate coordinate = new Coordinate(request.getLatitude(), request.getLongitude());
        Pet pet = receiveLocationUseCase.execute(id, coordinate, request.getTimestamp());

        return ResponseEntity.ok(mapToResponse(pet));
    }

    private PetLocationResponseDTO mapToResponse(Pet pet) {
        PetLocationResponseDTO response = new PetLocationResponseDTO();
        response.setId(pet.getId());
        response.setName(pet.getName());
        if (pet.getLastLocation() != null) {
            response.setLatitude(pet.getLastLocation().getCoordinate().getLatitude());
            response.setLongitude(pet.getLastLocation().getCoordinate().getLongitude());
            Address addr = pet.getLastLocation().getAddress();
            response.setCountry(addr.country());
            response.setState(addr.state());
            response.setCity(addr.city());
            response.setNeighborhood(addr.neighborhood());
            response.setStreetAddress(addr.streetAddress());
            response.setTimestamp(pet.getLastLocation().getTimestamp());
        }
        return response;
    }
}

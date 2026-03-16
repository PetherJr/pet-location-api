package com.petherson.petlocation.application.usecase;

import com.petherson.petlocation.application.port.in.ReceiveLocationUseCase;
import com.petherson.petlocation.application.port.out.GeocodingClient;
import com.petherson.petlocation.application.port.out.PetRepository;
import com.petherson.petlocation.domain.model.Coordinate;
import com.petherson.petlocation.domain.model.Location;
import com.petherson.petlocation.domain.model.Pet;
import com.petherson.petlocation.domain.model.Address;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Application Service that orchestrates the Receive Location use case.
 */
@Service
public class ReceiveLocationService implements ReceiveLocationUseCase {

    private final PetRepository petRepository;
    private final GeocodingClient geocodingClient;
    private final Counter locationUpdateCounter;

    public ReceiveLocationService(PetRepository petRepository, GeocodingClient geocodingClient, MeterRegistry registry) {
        this.petRepository = petRepository;
        this.geocodingClient = geocodingClient;
        this.locationUpdateCounter = Counter.builder("pet.location.updates")
                .description("Total number of pet location updates processed")
                .register(registry);
    }

    @Override
    @Transactional
    public Pet execute(UUID petId, Coordinate coordinate, LocalDateTime timestamp) {
        locationUpdateCounter.increment();
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found: " + petId));

        Address address = geocodingClient.getAddress(coordinate)
                .orElse(Address.ofLabel("Unknown address"));

        Location location = new Location(coordinate, address, timestamp != null ? timestamp : LocalDateTime.now());
        pet.updateLocation(location);

        petRepository.save(pet);
        return pet;
    }
}

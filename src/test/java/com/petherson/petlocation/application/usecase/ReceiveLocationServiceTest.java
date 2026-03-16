package com.petherson.petlocation.application.usecase;

import com.petherson.petlocation.application.port.out.GeocodingClient;
import com.petherson.petlocation.application.port.out.PetRepository;
import com.petherson.petlocation.domain.model.Address;
import com.petherson.petlocation.domain.model.Coordinate;
import com.petherson.petlocation.domain.model.Pet;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReceiveLocationServiceTest {

    private PetRepository petRepository;
    private GeocodingClient geocodingClient;
    private MeterRegistry meterRegistry;
    private ReceiveLocationService service;

    @BeforeEach
    void setUp() {
        petRepository = Mockito.mock(PetRepository.class);
        geocodingClient = Mockito.mock(GeocodingClient.class);
        meterRegistry = new SimpleMeterRegistry();
        service = new ReceiveLocationService(petRepository, geocodingClient, meterRegistry);
    }

    @Test
    void execute_ShouldUpdatePetLocation_WhenPetExists() {
        // Arrange
        UUID petId = UUID.randomUUID();
        Pet pet = new Pet(petId, "Rex");
        Coordinate coord = new Coordinate(-23.5505, -46.6333);
        Address address = new Address("Brasil", "SP", "São Paulo", "Sé", "Mock Street");
        LocalDateTime timestamp = LocalDateTime.now();

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(geocodingClient.getAddress(coord)).thenReturn(Optional.of(address));

        // Act
        Pet updatedPet = service.execute(petId, coord, timestamp);

        // Assert
        assertEquals(address, updatedPet.getLastLocation().getAddress());
        assertEquals(coord, updatedPet.getLastLocation().getCoordinate());
        assertEquals(timestamp, updatedPet.getLastLocation().getTimestamp());
        verify(petRepository).save(pet);
    }

    @Test
    void execute_ShouldThrowException_WhenPetDoesNotExist() {
        // Arrange
        UUID petId = UUID.randomUUID();
        Coordinate coord = new Coordinate(0, 0);

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(petId, coord, LocalDateTime.now()));
        verify(petRepository, never()).save(any());
    }
}

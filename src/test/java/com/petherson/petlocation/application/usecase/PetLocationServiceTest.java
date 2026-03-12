package com.petherson.petlocation.application.usecase;

import com.petherson.petlocation.application.port.out.GeocodingProvider;
import com.petherson.petlocation.domain.model.PetLocation;
import com.petherson.petlocation.domain.repository.PetLocationRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetLocationServiceTest {

    @Mock
    private GeocodingProvider geocodingProvider;
    @Mock
    private PetLocationRepository petLocationRepository;
    @Mock
    private Counter endpointCallCounter;
    @Mock
    private Timer geocodingTimer;
    @Mock
    private Counter geocodingFailureCounter;

    private PetLocationService petLocationService;

    @BeforeEach
    void setUp() {
        petLocationService = new PetLocationService(
                geocodingProvider,
                petLocationRepository,
                endpointCallCounter,
                geocodingTimer,
                geocodingFailureCounter
        );

        // Mocking timer record method globally (lenient)
        lenient().when(geocodingTimer.record(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<PetLocation> supplier = invocation.getArgument(0);
            return supplier.get();
        });
    }

    @Test
    void execute_ShouldResolveAndSaveLocation() {
        // Given
        PetLocation input = PetLocation.builder()
                .sensorId("SEN-123")
                .latitude(-23.561684)
                .longitude(-46.656139)
                .timestamp(OffsetDateTime.now())
                .build();

        PetLocation resolvedFromProvider = PetLocation.builder()
                .country("Brasil")
                .state("SP")
                .city("São Paulo")
                .neighborhood("Bela Vista")
                .streetAddress("Av. Paulista")
                .build();

        when(geocodingProvider.reverseGeocode(anyDouble(), anyDouble())).thenReturn(resolvedFromProvider);
        when(geocodingProvider.getProviderName()).thenReturn("positionstack");
        when(petLocationRepository.save(any(PetLocation.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        PetLocation result = petLocationService.execute(input);

        // Then
        assertEquals("SEN-123", result.getSensorId());
        assertEquals("Brasil", result.getCountry());
        assertEquals("positionstack", result.getProvider());
        verify(endpointCallCounter).increment();
        verify(petLocationRepository).save(any(PetLocation.class));
        verify(geocodingTimer).record(any(Supplier.class));
    }

    @Test
    void execute_ShouldHandleGeocodingFailure() {
        // Given
        PetLocation input = PetLocation.builder()
                .sensorId("SEN-123")
                .latitude(-23.561684)
                .longitude(-46.656139)
                .timestamp(OffsetDateTime.now())
                .build();

        // Override the global lenient stubbing specifically for this test
        lenient().when(geocodingTimer.record(any(Supplier.class))).thenThrow(new RuntimeException("API Error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> petLocationService.execute(input));
        assertEquals("API Error", exception.getMessage());

        verify(endpointCallCounter).increment();
        verify(geocodingFailureCounter).increment();
        verify(petLocationRepository, never()).save(any());
    }
}

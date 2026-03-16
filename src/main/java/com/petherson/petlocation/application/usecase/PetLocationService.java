package com.petherson.petlocation.application.usecase;

import com.petherson.petlocation.application.port.in.ResolvePetLocationUseCase;
import com.petherson.petlocation.application.port.out.GeocodingProvider;
import com.petherson.petlocation.domain.model.PetLocation;
import com.petherson.petlocation.domain.repository.PetLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetLocationService implements ResolvePetLocationUseCase {

    private final GeocodingProvider geocodingProvider;
    private final PetLocationRepository petLocationRepository;
    private final Counter endpointCallCounter;
    private final Timer geocodingTimer;
    private final Counter geocodingFailureCounter;

    @Override
    public PetLocation execute(PetLocation location) {
        endpointCallCounter.increment();
        log.info("Resolving location for sensorId: {}", location.getSensorId());
        
        PetLocation resolved;
        try {
            resolved = geocodingTimer.record(() -> geocodingProvider.reverseGeocode(location.getLatitude(), location.getLongitude()));
        } catch (Exception e) {
            geocodingFailureCounter.increment();
            throw e;
        }
        
        resolved.setSensorId(location.getSensorId());
        resolved.setTimestamp(location.getTimestamp());
        resolved.setLatitude(location.getLatitude());
        resolved.setLongitude(location.getLongitude());
        resolved.setProvider(geocodingProvider.getProviderName());
        
        log.info("Persisting location event for sensorId: {}", location.getSensorId());
        return petLocationRepository.save(resolved);
    }
}

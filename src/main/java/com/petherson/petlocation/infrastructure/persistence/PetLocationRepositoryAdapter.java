package com.petherson.petlocation.infrastructure.persistence;

import com.petherson.petlocation.domain.model.PetLocation;
import com.petherson.petlocation.domain.repository.PetLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetLocationRepositoryAdapter implements PetLocationRepository {

    private final JpaPetLocationRepository jpaRepository;

    @Override
    public PetLocation save(PetLocation petLocation) {
        PetLocationEntity entity = PetLocationEntity.builder()
                .sensorId(petLocation.getSensorId())
                .latitude(petLocation.getLatitude())
                .longitude(petLocation.getLongitude())
                .timestamp(petLocation.getTimestamp())
                .country(petLocation.getCountry())
                .state(petLocation.getState())
                .city(petLocation.getCity())
                .neighborhood(petLocation.getNeighborhood())
                .streetAddress(petLocation.getStreetAddress())
                .provider(petLocation.getProvider())
                .build();
        
        PetLocationEntity saved = jpaRepository.save(entity);
        
        return petLocation; // Or map 'saved' back to domain if ID is needed
    }
}

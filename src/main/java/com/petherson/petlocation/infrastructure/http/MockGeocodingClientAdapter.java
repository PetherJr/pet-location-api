package com.petherson.petlocation.infrastructure.http;

import com.petherson.petlocation.application.port.out.GeocodingClient;
import com.petherson.petlocation.domain.model.Address;
import com.petherson.petlocation.domain.model.Coordinate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(name = "geocoding.provider", havingValue = "mock", matchIfMissing = true)
public class MockGeocodingClientAdapter implements GeocodingClient {

    @Override
    public Optional<Address> getAddress(Coordinate coordinate) {
        return Optional.of(new Address(
            "Brasil",
            "SP",
            "São Paulo",
            "Sé",
            "Street at " + coordinate.getLatitude() + ", " + coordinate.getLongitude()
        ));
    }
}

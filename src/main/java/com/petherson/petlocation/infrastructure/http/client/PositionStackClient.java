package com.petherson.petlocation.infrastructure.http.client;

import com.petherson.petlocation.application.port.out.GeocodingProvider;
import com.petherson.petlocation.domain.model.PetLocation;
import com.petherson.petlocation.infrastructure.http.dto.PositionStackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * Implementation of GeocodingProvider for PositionStack.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PositionStackClient implements GeocodingProvider {

    private final RestTemplate restTemplate;

    @Value("${positionstack.api.key}")
    private String apiKey;

    @Value("${positionstack.api.url:http://api.positionstack.com/v1/reverse}")
    private String apiUrl;

    @Override
    public PetLocation reverseGeocode(Double latitude, Double longitude) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access_key", apiKey)
                .queryParam("query", String.format("%f,%f", latitude, longitude))
                .toUriString();

        log.debug("Calling PositionStack API for coordinates: {}, {}", latitude, longitude);

        try {
            PositionStackResponse response = restTemplate.getForObject(url, PositionStackResponse.class);
            return mapToDomain(response);
        } catch (Exception e) {
            log.error("Failed to call PositionStack API", e);
            throw new RuntimeException("Error communicating with geocoding provider: " + e.getMessage(), e);
        }
    }

    @Override
    public String getProviderName() {
        return "positionstack";
    }

    private PetLocation mapToDomain(PositionStackResponse response) {
        return Optional.ofNullable(response)
                .map(PositionStackResponse::getData)
                .filter(data -> !data.isEmpty())
                .map(dataList -> dataList.get(0))
                .map(data -> PetLocation.builder()
                        .country(data.getCountry())
                        .state(data.getRegionCode())
                        .city(data.getCity())
                        .neighborhood(data.getNeighborhood())
                        .streetAddress(data.getStreet())
                        .build())
                .orElse(PetLocation.builder().build());
    }
}

package com.petherson.petlocation.infrastructure.http;

import com.petherson.petlocation.application.port.out.GeocodingClient;
import com.petherson.petlocation.domain.model.Address;
import com.petherson.petlocation.domain.model.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "geocoding.provider", havingValue = "positionstack")
public class PositionStackClientAdapter implements GeocodingClient {

    private final String apiKey;
    private final String baseUrl = "http://api.positionstack.com/v1/reverse";
    private final RestTemplate restTemplate;

    public PositionStackClientAdapter(@Value("${geocoding.api.key:}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Address> getAddress(Coordinate coordinate) {
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("SUA_CHAVE_AQUI")) {
            return Optional.empty();
        }

        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("access_key", apiKey)
                    .queryParam("query", coordinate.getLatitude() + "," + coordinate.getLongitude())
                    .build()
                    .toUriString();

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                if (!data.isEmpty()) {
                    Map<String, Object> first = data.get(0);
                    return Optional.of(new Address(
                        (String) first.get("country"),
                        (String) first.get("region"),
                        (String) first.get("city"),
                        (String) first.get("neighbourhood"),
                        (String) first.get("label")
                    ));
                }
            }
        } catch (Exception e) {
            // No logs to keep it simple, but in production we'd log the error
        }
        return Optional.empty();
    }
}

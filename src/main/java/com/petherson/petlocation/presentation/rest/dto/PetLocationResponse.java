package com.petherson.petlocation.presentation.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PetLocationResponse {
    private String sensorId;
    private OffsetDateTime timestamp;
    private Coordinates coordinates;
    private Location location;
    private String provider;

    @Data
    @Builder
    public static class Coordinates {
        private Double latitude;
        private Double longitude;
    }

    @Data
    @Builder
    public static class Location {
        private String country;
        private String state;
        private String city;
        private String neighborhood;
        private String streetAddress;
    }
}

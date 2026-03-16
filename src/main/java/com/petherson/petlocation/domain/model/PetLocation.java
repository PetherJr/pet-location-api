package com.petherson.petlocation.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Domain object representing a pet location event.
 * Contains both the sensor data and the resolved physical address.
 */
@Data
@Builder
public class PetLocation {
    private String sensorId;
    private Double latitude;
    private Double longitude;
    private OffsetDateTime timestamp;
    
    // Resolved address details
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String streetAddress;
    
    private String provider;
}

package com.petherson.petlocation.application.port.out;

import com.petherson.petlocation.domain.model.PetLocation;

/**
 * Interface for reverse geocoding services.
 */
public interface GeocodingProvider {
    /**
     * Resolves physical address from coordinates.
     * @param latitude Latitude
     * @param longitude Longitude
     * @return PetLocation with populated address fields, or null values for missing fields.
     */
    PetLocation reverseGeocode(Double latitude, Double longitude);
    
    String getProviderName();
}

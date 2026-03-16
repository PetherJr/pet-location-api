package com.petherson.petlocation.application.port.out;

import com.petherson.petlocation.domain.model.PetLocation;

public interface GeocodingProvider {
    PetLocation reverseGeocode(Double latitude, Double longitude);
    
    String getProviderName();
}

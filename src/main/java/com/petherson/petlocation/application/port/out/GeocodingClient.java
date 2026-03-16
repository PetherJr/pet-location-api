package com.petherson.petlocation.application.port.out;

import com.petherson.petlocation.domain.model.Address;
import com.petherson.petlocation.domain.model.Coordinate;
import java.util.Optional;

/**
 * Output Port (Interface) for external geocoding services.
 */
public interface GeocodingClient {
    Optional<Address> getAddress(Coordinate coordinate);
}

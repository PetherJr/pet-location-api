package com.petherson.petlocation.domain.model;

import java.time.LocalDateTime;

/**
 * Entity representing a geographic location with an address.
 */
public class Location {
    private final Coordinate coordinate;
    private final Address address;
    private final LocalDateTime timestamp;

    public Location(Coordinate coordinate, Address address, LocalDateTime timestamp) {
        this.coordinate = coordinate;
        this.address = address;
        this.timestamp = timestamp;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

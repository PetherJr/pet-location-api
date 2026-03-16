package com.petherson.petlocation.domain.model;

import java.util.UUID;

/**
 * Aggregate Root representing a Pet and its tracking information.
 */
public class Pet {
    private final UUID id;
    private final String name;
    private Location lastLocation;
    private final java.util.List<DomainEvent> events = new java.util.ArrayList<>();

    public Pet(UUID id, String name) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be null or blank");
        this.id = id;
        this.name = name;
    }

    public void updateLocation(Location location) {
        this.lastLocation = location;
        this.events.add(new PetLocationUpdatedEvent(id, location.getCoordinate(), location.getTimestamp()));
    }

    public java.util.List<DomainEvent> getEvents() {
        return java.util.Collections.unmodifiableList(events);
    }

    public void clearEvents() {
        events.clear();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLastLocation() {
        return lastLocation;
    }
}

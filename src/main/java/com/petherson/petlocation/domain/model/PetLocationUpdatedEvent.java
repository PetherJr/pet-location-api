package com.petherson.petlocation.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PetLocationUpdatedEvent(
    UUID petId,
    Coordinate coordinate,
    LocalDateTime occurredOn
) implements DomainEvent {}

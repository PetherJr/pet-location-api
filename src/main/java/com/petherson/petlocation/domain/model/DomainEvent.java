package com.petherson.petlocation.domain.model;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
}

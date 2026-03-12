package com.petherson.petlocation.domain.repository;

import com.petherson.petlocation.domain.model.PetLocation;

/**
 * Interface for persisting pet location events.
 */
public interface PetLocationRepository {
    PetLocation save(PetLocation petLocation);
}

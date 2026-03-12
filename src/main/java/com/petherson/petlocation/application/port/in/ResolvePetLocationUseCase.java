package com.petherson.petlocation.application.port.in;

import com.petherson.petlocation.domain.model.PetLocation;

/**
 * Port for resolving and saving pet location.
 */
public interface ResolvePetLocationUseCase {
    PetLocation execute(PetLocation location);
}

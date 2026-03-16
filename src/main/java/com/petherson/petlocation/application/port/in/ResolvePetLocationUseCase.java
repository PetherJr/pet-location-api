package com.petherson.petlocation.application.port.in;

import com.petherson.petlocation.domain.model.PetLocation;

public interface ResolvePetLocationUseCase {
    PetLocation execute(PetLocation location);
}

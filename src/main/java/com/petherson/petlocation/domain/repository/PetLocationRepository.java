package com.petherson.petlocation.domain.repository;

import com.petherson.petlocation.domain.model.PetLocation;

public interface PetLocationRepository {
    PetLocation save(PetLocation petLocation);
}

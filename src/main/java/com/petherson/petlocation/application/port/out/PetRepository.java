package com.petherson.petlocation.application.port.out;

import com.petherson.petlocation.domain.model.Pet;
import java.util.Optional;
import java.util.UUID;

/**
 * Output Port (Interface) for Pet persistence operations.
 */
public interface PetRepository {
    Optional<Pet> findById(UUID id);
    void save(Pet pet);
}

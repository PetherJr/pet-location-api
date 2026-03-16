package com.petherson.petlocation.application.port.in;

import com.petherson.petlocation.domain.model.Coordinate;
import com.petherson.petlocation.domain.model.Pet;
import java.util.UUID;

/**
 * Input Port (Interface) for the Receive Location Use Case.
 */
public interface ReceiveLocationUseCase {
    Pet execute(UUID petId, Coordinate coordinate, java.time.LocalDateTime timestamp);
}

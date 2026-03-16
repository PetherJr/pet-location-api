package com.petherson.petlocation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataPetRepository extends JpaRepository<PetJpaEntity, UUID> {
}

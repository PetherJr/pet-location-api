package com.petherson.petlocation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPetLocationRepository extends JpaRepository<PetLocationEntity, Long> {
}

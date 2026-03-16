package com.petherson.petlocation.infrastructure.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpringDataPetRepository repository;

    public DataInitializer(SpringDataPetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        // Seeding a test pet with the same ID used in the example
        UUID testId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        if (repository.findById(testId).isEmpty()) {
            repository.save(new PetJpaEntity(testId, "Rex (Test Pet)"));
            System.out.println(">>> Seeded test pet: Rex (ID: " + testId + ")");
        }
    }
}

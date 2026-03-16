package com.petherson.petlocation.infrastructure.persistence;

import com.petherson.petlocation.application.port.out.PetRepository;
import com.petherson.petlocation.domain.model.Address;
import com.petherson.petlocation.domain.model.Coordinate;
import com.petherson.petlocation.domain.model.Location;
import com.petherson.petlocation.domain.model.Pet;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class H2PetRepositoryAdapter implements PetRepository {

    private final SpringDataPetRepository repository;

    public H2PetRepositoryAdapter(SpringDataPetRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Pet> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void save(Pet pet) {
        repository.save(toJpaEntity(pet));
    }

    private Pet toDomain(PetJpaEntity entity) {
        Pet pet = new Pet(entity.getId(), entity.getName());
        if (entity.getLastLocation() != null) {
            LocationJpaEntity loc = entity.getLastLocation();
            pet.updateLocation(new Location(
                    new Coordinate(loc.getLatitude(), loc.getLongitude()),
                    new Address(loc.getCountry(), loc.getState(), loc.getCity(), loc.getNeighborhood(), loc.getStreetAddress()),
                    loc.getTimestamp()
            ));
        }
        return pet;
    }

    private PetJpaEntity toJpaEntity(Pet pet) {
        PetJpaEntity entity = new PetJpaEntity(pet.getId(), pet.getName());
        if (pet.getLastLocation() != null) {
            Location loc = pet.getLastLocation();
            LocationJpaEntity locEntity = new LocationJpaEntity();
            locEntity.setLatitude(loc.getCoordinate().getLatitude());
            locEntity.setLongitude(loc.getCoordinate().getLongitude());
            Address addr = loc.getAddress();
            locEntity.setCountry(addr.country());
            locEntity.setState(addr.state());
            locEntity.setCity(addr.city());
            locEntity.setNeighborhood(addr.neighborhood());
            locEntity.setStreetAddress(addr.streetAddress());
            locEntity.setTimestamp(loc.getTimestamp());
            entity.setLastLocation(locEntity);
        }
        return entity;
    }
}

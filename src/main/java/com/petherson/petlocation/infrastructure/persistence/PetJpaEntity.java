package com.petherson.petlocation.infrastructure.persistence;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "pets")
public class PetJpaEntity {

    @Id
    private UUID id;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_location_id")
    private LocationJpaEntity lastLocation;

    public PetJpaEntity() {}

    public PetJpaEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocationJpaEntity getLastLocation() { return lastLocation; }
    public void setLastLocation(LocationJpaEntity lastLocation) { this.lastLocation = lastLocation; }
}

package com.petherson.petlocation.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "pet_location_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetLocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String sensorId;
    private Double latitude;
    private Double longitude;
    private OffsetDateTime timestamp;
    
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String streetAddress;
    
    private String provider;
}

package com.petherson.petlocation.interfaces.rest;

import tools.jackson.databind.ObjectMapper;
import com.petherson.petlocation.infrastructure.persistence.PetJpaEntity;
import com.petherson.petlocation.infrastructure.persistence.SpringDataPetRepository;
import com.petherson.petlocation.interfaces.rest.dto.LocationRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetLocationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataPetRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateLocation_ShouldReturnUpdatedPet() throws Exception {
        // Arrange
        UUID petId = UUID.randomUUID();
        repository.save(new PetJpaEntity(petId, "Fluffy"));

        LocationRequestDTO request = new LocationRequestDTO();
        request.setLatitude(-15.7801);
        request.setLongitude(-47.9292);

        // Act & Assert
        mockMvc.perform(post("/api/pets/" + petId + "/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        // Verify database state
        PetJpaEntity updated = repository.findById(petId).orElseThrow();
        assertNotNull(updated.getLastLocation());
        assertEquals(-15.7801, updated.getLastLocation().getLatitude());
    }
}

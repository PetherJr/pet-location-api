package com.petherson.petlocation.presentation.rest.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8089)
class PetLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("positionstack.api.url", () -> "http://localhost:8089/reverse");
        registry.add("positionstack.api.key", () -> "test-key");
    }

    @Test
    void receiveLocation_ShouldReturnResolvedLocation() throws Exception {
        String requestBody = """
                {
                  "sensorId": "SEN-123",
                  "latitude": -23.561684,
                  "longitude": -46.656139,
                  "timestamp": "2026-03-11T10:15:30Z"
                }
                """;

        String mockResponse = """
                {
                  "data": [
                    {
                      "latitude": -23.561684,
                      "longitude": -46.656139,
                      "country": "Brazil",
                      "region_code": "SP",
                      "city": "São Paulo",
                      "neighbourhood": "Bela Vista",
                      "street": "Avenida Paulista"
                    }
                  ]
                }
                """;

        stubFor(get(urlPathEqualTo("/reverse"))
                .withQueryParam("access_key", equalTo("test-key"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));

        mockMvc.perform(post("/api/v1/pets/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorId").value("SEN-123"))
                .andExpect(jsonPath("$.location.city").value("São Paulo"))
                .andExpect(jsonPath("$.location.state").value("SP"))
                .andExpect(jsonPath("$.provider").value("positionstack"));
    }

    @Test
    void receiveLocation_ShouldReturnPartialData_WhenProviderReturnsMissingFields() throws Exception {
        String requestBody = """
                {
                  "sensorId": "SEN-123",
                  "latitude": -23.561684,
                  "longitude": -46.656139,
                  "timestamp": "2026-03-11T10:15:30Z"
                }
                """;

        String mockResponse = """
                {
                  "data": [
                    {
                      "latitude": -23.561684,
                      "longitude": -46.656139,
                      "country": "Brazil"
                    }
                  ]
                }
                """;

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));

        mockMvc.perform(post("/api/v1/pets/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.country").value("Brazil"))
                .andExpect(jsonPath("$.location.city").isEmpty());
    }

    @Test
    void receiveLocation_ShouldReturn504_WhenProviderTimeouts() throws Exception {
        String requestBody = """
                {
                  "sensorId": "SEN-123",
                  "latitude": -23.561684,
                  "longitude": -46.656139,
                  "timestamp": "2026-03-11T10:15:30Z"
                }
                """;

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse()
                        .withFixedDelay(6000)));
        
        mockMvc.perform(post("/api/v1/pets/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.title").value("Provider Timeout"));
    }

    @Test
    void receiveLocation_ShouldReturn400_WhenInvalidRequest() throws Exception {
        String invalidRequestBody = """
                {
                  "sensorId": "",
                  "latitude": -100.0,
                  "longitude": -46.656139,
                  "timestamp": "invalid-date"
                }
                """;

        mockMvc.perform(post("/api/v1/pets/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }
}

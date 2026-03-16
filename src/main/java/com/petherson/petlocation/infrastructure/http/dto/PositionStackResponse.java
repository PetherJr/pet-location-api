package com.petherson.petlocation.infrastructure.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PositionStackResponse {
    private List<PositionStackData> data;

    @Data
    public static class PositionStackData {
        private Double latitude;
        private Double longitude;
        @JsonProperty("label")
        private String label;
        @JsonProperty("name")
        private String name;
        @JsonProperty("type")
        private String type;
        @JsonProperty("number")
        private String houseNumber;
        @JsonProperty("street")
        private String street;
        @JsonProperty("neighbourhood")
        private String neighborhood;
        @JsonProperty("city")
        private String city;
        @JsonProperty("locality")
        private String locality;
        @JsonProperty("county")
        private String county;
        @JsonProperty("administrative_area")
        private String administrativeArea;
        @JsonProperty("region")
        private String region;
        @JsonProperty("region_code")
        private String regionCode;
        @JsonProperty("country")
        private String country;
        @JsonProperty("country_code")
        private String countryCode;
        @JsonProperty("postal_code")
        private String postalCode;
    }
}

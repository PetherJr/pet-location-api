package com.petherson.petlocation.interfaces.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class LocationRequestDTO {

    @Min(-90) @Max(90)
    private double latitude;

    @Min(-180) @Max(180)
    private double longitude;

    private java.time.LocalDateTime timestamp;

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public java.time.LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(java.time.LocalDateTime timestamp) { this.timestamp = timestamp; }
}
